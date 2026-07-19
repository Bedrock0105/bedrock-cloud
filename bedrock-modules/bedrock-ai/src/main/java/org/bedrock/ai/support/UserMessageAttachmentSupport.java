package org.bedrock.ai.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bedrock.ai.constant.AiConstant;
import org.bedrock.ai.constant.PromptConstant;
import org.bedrock.ai.dto.UserMessageAttachment;
import org.bedrock.ai.factory.documet.DocumentReaderFactory;
import org.bedrock.ai.vo.AiModelCheckVO;
import org.bedrock.common.code.util.MediaTypeUtil;
import org.bedrock.common.code.util.NumberUtil;
import org.bedrock.common.code.util.ResourceUtil;
import org.bedrock.common.code.util.StringUtil;
import org.springframework.ai.content.Media;
import org.springframework.ai.document.Document;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户消息附件解析工具。
 * <p>
 * 供两处共用，保证行为一致：
 * <ul>
 *   <li>当轮增强 — {@link org.bedrock.ai.advisor.prompt.AiPromptAugmenter}</li>
 *   <li>历史还原 — {@link org.bedrock.ai.service.impl.AiChatMessageServiceImpl} 的 {@code get}/{@code toChatEntry}</li>
 * </ul>
 * 处理规则（与 RAG 无关，RAG 由增强器单独拼接且不进历史）：
 * <ul>
 *   <li>模型支持多模态 <b>且</b> 附件为图片 → 构建 {@link Media}（{@code name}=文件名），正文写占位说明</li>
 *   <li>模型不支持多模态且为图片 → 正文写明无法解析，不抽文本、不进 Media</li>
 *   <li>非图片（PDF/文档等）→ {@link DocumentReaderFactory} 抽文本，写入 {@code <attachment>} 标签</li>
 * </ul>
 *
 * @see PromptConstant#ATTACHMENT_HINT
 * @see PromptConstant#ATTACHMENT_MEDIA_HINT
 * @see PromptConstant#ATTACHMENT_IMAGE_UNSUPPORTED_HINT
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserMessageAttachmentSupport {

    /**
     * 按 URL / 类型读取文档正文（非图片附件）
     */
    private final DocumentReaderFactory readerFactory;

    /**
     * 从 context 中的模型详情判断当前会话模型是否支持多模态。
     *
     * @param context Advisor / ChatHistory 共享上下文，需含 {@link AiConstant#CTX_MODEL_DETAIL}
     * @return {@code true} 表示支持多模态（图片可走 Media）
     */
    public boolean resolveSupportMultimodal(Map<String, Object> context) {
        Object modelDetail = context.get(AiConstant.CTX_MODEL_DETAIL);
        if (modelDetail instanceof AiModelCheckVO aiModelCheckVO) {
            return NumberUtil.toInt(aiModelCheckVO.getSupportMultimodal(), 0) == 1;
        }
        return false;
    }

    /**
     * 将附件列表规范为 {@link UserMessageAttachment}。
     * <p>
     *
     * @param raw 原始列表（可为 null、空、已是 record、或 Map 结构）
     * @return 规范化后的附件列表，不会为 null
     */
    public List<UserMessageAttachment> normalizeAttachments(List<?> raw) {
        return raw.stream()
                .filter(UserMessageAttachment.class::isInstance)
                .map(UserMessageAttachment.class::cast)
                .toList();
    }

    /**
     * 解析附件，生成 Media 列表与正文侧 attachment XML。
     * <p>
     * 当轮增强与历史还原均调用本方法，避免两套逻辑漂移。
     *
     * @param list              附件原始列表
     * @param supportMultimodal 当前模型是否多模态
     * @return media 列表 + attachment 标签字符串（无附件时 media 为空列表、xml 为空串）
     */
    public AttachmentBuildResult buildMediaAndTextBlocks(List<UserMessageAttachment> list, boolean supportMultimodal) {
        if (list.isEmpty()) {
            return new AttachmentBuildResult(List.of(), "");
        }
        List<Media> mediaList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (UserMessageAttachment attachment : list) {
            MediaType mediaType = MediaTypeUtil.getMediaType(attachment.fileName());
            boolean image = MediaTypeUtil.isImage(mediaType.toString());
            if (supportMultimodal && image) {
                Media media = toImageMedia(attachment, mediaType);
                if (media != null) {
                    mediaList.add(media);
                }
                // 正文仅占位，真实图像在 UserMessage.media 中，fileName 与 Media.name 对齐
                appendAttachmentTag(sb, attachment.fileName(), PromptConstant.ATTACHMENT_MEDIA_HINT);
                continue;
            }
            // 非多模态图片：无法解析，勿走文档抽取
            if (image) {
                appendAttachmentTag(sb, attachment.fileName(), PromptConstant.ATTACHMENT_IMAGE_UNSUPPORTED_HINT);
                continue;
            }
            // 非图片：抽文本拼进正文
            List<Document> documentList = readerFactory.readDocuments(attachment.url());
            String body = documentList.stream()
                    .map(Document::getText)
                    .filter(text -> text != null && !text.isBlank())
                    .collect(Collectors.joining(System.lineSeparator()));
            appendAttachmentTag(sb, attachment.fileName(), body);
        }
        return new AttachmentBuildResult(mediaList, sb.toString());
    }

    /**
     * 将附件 XML 块追加到用户正文末尾；无 XML 时原样返回。
     * <p>
     * 追加顺序：原正文 → {@link PromptConstant#ATTACHMENT_HINT} → attachment 标签块。
     *
     * @param content       用户原始正文，可为 null
     * @param attachmentXml {@link #buildMediaAndTextBlocks} 产出的标签串
     * @return 拼接后的完整正文
     */
    public String appendAttachmentToContent(String content, String attachmentXml) {
        if (StringUtil.isBlank(attachmentXml)) {
            return content;
        }
        String base = content == null ? "" : content;
        return base
                + System.lineSeparator()
                + PromptConstant.ATTACHMENT_HINT
                + System.lineSeparator()
                + attachmentXml;
    }

    /**
     * 追加单个 {@code <attachment fileName="...">...</attachment>} 节点。
     *
     * @param sb       输出缓冲
     * @param fileName 附件文件名（写入属性，并与 Media.name 对齐）
     * @param body     标签内文本（图片占位说明或文档抽取正文）
     */
    private void appendAttachmentTag(StringBuilder sb, String fileName, String body) {
        sb.append("<attachment fileName=\"").append(fileName).append("\">")
                .append(System.lineSeparator())
                .append(body == null ? "" : body)
                .append(System.lineSeparator())
                .append("</attachment>");
    }

    /**
     * 按 URL 加载图片资源并构建 Spring AI {@link Media}。
     *
     * @param attachment 附件元数据（fileName、url）
     * @param mediaType  根据文件名推断的 MIME
     * @return 成功返回 Media；读资源失败返回 null（已打错误日志）
     */
    private Media toImageMedia(UserMessageAttachment attachment, MediaType mediaType) {
        try {
            Resource resource = ResourceUtil.getResource(attachment.url());
            return Media.builder()
                    .mimeType(mediaType)
                    .data(resource)
                    .name(attachment.fileName())
                    .build();
        } catch (IOException e) {
            log.error("Error reading resource: {}", attachment.url(), e);
            return null;
        }
    }

    /**
     * 附件解析结果。
     *
     * @param mediaList     多模态图片 Media；无图时为 empty list（勿传 null）
     * @param attachmentXml 拼入 user 文本的 attachment 标签块；无附件时为空串
     */
    public record AttachmentBuildResult(List<Media> mediaList, String attachmentXml) {

    }
}
