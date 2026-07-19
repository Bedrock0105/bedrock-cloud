package org.bedrock.ai.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户消息附件元数据。
 * <p>
 * 落库于 {@code bedrock_ai_chat_message.attachments}（text 存 JSON）；
 * 当轮增强与历史还原时由 {@link org.bedrock.ai.support.UserMessageAttachmentSupport} 解析为
 * {@link org.springframework.ai.content.Media} 或正文中的 {@code <attachment>} 文本块。
 *
 * @param fileName 原始文件名（多模态时与 {@code Media.name} 对齐）
 * @param url      附件可访问地址（本地路径或远程 URL，供读资源 / 抽文档）
 */
public record UserMessageAttachment(String fileName, String url) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
}
