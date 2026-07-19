package org.bedrock.ai.advisor.prompt;

import lombok.RequiredArgsConstructor;
import org.bedrock.ai.constant.AiConstant;
import org.bedrock.ai.dto.UserMessageAttachment;
import org.bedrock.ai.entity.AiChatMessage;
import org.bedrock.ai.mapper.AiChatMessageMapper;
import org.bedrock.ai.support.UserMessageAttachmentSupport;
import org.bedrock.common.ai.advisor.history.ChatHistoryStore;
import org.bedrock.common.ai.advisor.prompt.PromptAugmenter;
import org.bedrock.common.code.util.NumberUtil;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.bedrock.common.ai.advisor.knowledge.KnowledgeRetriever.KNOWLEDGE_RETRIEVED_DOCUMENTS;

/**
 * 业务侧提示词增强实现：当轮拼接 RAG 检索结果与用户附件，并落库备查字段。
 * <p>
 * 职责划分：
 * <ul>
 *   <li>{@link #needAugmentation} — 有知识库结果或附件，且本轮尚未增强时才执行</li>
 *   <li>{@link #documents} — 收集 RAG 文档；将 {@code chunk_ids}、{@code attachments} 写入 user 消息行
 *       （只存 ID / 附件元数据，<b>RAG 正文不进历史</b>）</li>
 *   <li>{@link #augment} — 委托 {@link UserMessageAttachmentSupport} 把附件写入 UserMessage（Media / 正文标签），
 *       再由父接口把 RAG 文档拼成上下文字符串</li>
 * </ul>
 * <p>
 * ToolCall 二次走链：{@link org.springframework.ai.chat.client.advisor.ToolCallAdvisor} 会再次执行 Advisor；
 * 首轮已增强后 context 写入 {@link #PROMPT_AUGMENTED}，二次 {@link #needAugmentation} 返回 false，避免重复拼 RAG/附件。
 * 多轮历史中的附件由 {@link org.bedrock.ai.service.impl.AiChatMessageServiceImpl#get} 按同一套 Support 逻辑还原，
 * RAG 每轮按新问题重新检索，不按历史 {@code chunk_ids} 回灌。
 *
 * @see PromptAugmenter
 * @see UserMessageAttachmentSupport
 */
@Component
@RequiredArgsConstructor
public class AiPromptAugmenter implements PromptAugmenter {

    /**
     * 本轮 prompt 已增强标记（写入 ChatClient context）。
     * <p>
     * ToolCallAdvisor 二次走链时若再拼 RAG/附件会重复占 token、干扰工具结果；
     * 历史附件走 {@link ChatHistoryStore.ChatEntry#media()} 与正文标签还原，与本标记无关。
     */
    private static final String PROMPT_AUGMENTED = "prompt_augmented";

    /**
     * 附件解析（与历史还原共用）
     */
    private final UserMessageAttachmentSupport attachmentSupport;

    /**
     * 回写 user 消息上的 chunkIds / attachmentList
     */
    private final AiChatMessageMapper aiChatMessageMapper;

    /**
     * 是否需要增强：默认「有知识库检索结果」或「有用户附件」，且本轮尚未打过 {@link #PROMPT_AUGMENTED}。
     *
     * @param prompt  当前 prompt
     * @param context Advisor 共享上下文
     * @return {@code true} 时后续会调用 {@link #documents} / {@link #augment}
     */
    @Override
    public boolean needAugmentation(Prompt prompt, Map<String, Object> context) {
        return (PromptAugmenter.super.needAugmentation(prompt, context) || context.containsKey(AiConstant.CTX_USER_ATTACHMENT))
                && context.get(PROMPT_AUGMENTED) == null;
    }

    /**
     * 收集 RAG 文档，并落库分片 ID、附件列表。
     * <p>
     * 依赖 {@link ChatHistoryStore#USER_MESSAGE_ID} 定位本轮预插入的 user 行；
     * ID 缺失或非 user 角色时仅走父接口默认收集，不落库。
     * 落库内容为备查元数据，不把检索正文写入 {@code content}。
     *
     * @param prompt  当前 prompt
     * @param context Advisor 共享上下文（含检索结果、附件、消息 ID）
     * @return 按来源分组的文档列表，供后续 {@link #join} / {@link #augment} 使用
     */
    @Override
    public List<List<Document>> documents(Prompt prompt, Map<String, Object> context) {
        Object userMessageIdObj = context.get(ChatHistoryStore.USER_MESSAGE_ID);
        // 无预插入 user 消息 ID 时只走默认收集，不落库
        if (userMessageIdObj == null) {
            return PromptAugmenter.super.documents(prompt, context);
        }
        long userMessageId = NumberUtil.toLong(userMessageIdObj, 0L);
        if (userMessageId <= 0) {
            return PromptAugmenter.super.documents(prompt, context);
        }
        AiChatMessage message = aiChatMessageMapper.selectById(userMessageId);
        if (message == null || !MessageType.USER.getValue().equals(message.getRole())) {
            return PromptAugmenter.super.documents(prompt, context);
        }

        List<List<Document>> documents = new ArrayList<>();
        List<String> chunkIds = new ArrayList<>();
        Object raw = context.get(KNOWLEDGE_RETRIEVED_DOCUMENTS);
        if (raw instanceof List<?> documentsList && !documentsList.isEmpty()) {
            List<Document> knowledgeDocs = documentsList.stream()
                    .filter(Document.class::isInstance)
                    .map(Document.class::cast)
                    .peek(doc -> {
                        Object chunkId = doc.getMetadata().get(AiConstant.VECTOR_META_KEY_CHUNK_ID);
                        if (chunkId != null) {
                            chunkIds.add(String.valueOf(chunkId));
                        }
                    })
                    .toList();
            documents.add(knowledgeDocs);
        }

        List<UserMessageAttachment> attachments = Collections.emptyList();
        Object attachmentObj = context.get(AiConstant.CTX_USER_ATTACHMENT);
        if (attachmentObj instanceof List<?> list && !list.isEmpty()) {
            attachments = list.stream()
                    .filter(UserMessageAttachment.class::isInstance)
                    .map(UserMessageAttachment.class::cast)
                    .toList();
        }

        if (!attachments.isEmpty() || !chunkIds.isEmpty()) {
            AiChatMessage toUpdate = new AiChatMessage();
            toUpdate.setId(userMessageId);
            if (!attachments.isEmpty()) {
                toUpdate.setAttachments(attachments);
            }
            if (!chunkIds.isEmpty()) {
                toUpdate.setChunkIds(chunkIds);
            }
            aiChatMessageMapper.updateById(toUpdate);
        }
        return documents;
    }

    /**
     * 将附件写入本轮 UserMessage，再返回 RAG 上下文字符串。
     * <p>
     * 先置 {@link #PROMPT_AUGMENTED}，保证同轮 ToolCall 二次请求不再进入本增强器。
     * 附件：多模态图片进 {@link Media}，非图片抽文本进 {@code <attachment>} 标签（见 Support）。
     * 返回值交给 Advisor 模板拼进最终 prompt；附件已直接改写了 prompt 中的 UserMessage。
     *
     * @param documents 合并后的 RAG 文档（可为空）
     * @param prompt    当前 prompt（可能被就地替换最后一条 UserMessage）
     * @param context   Advisor 共享上下文
     * @return 增强结果；附件已直接改写 UserMessage，RAG 文本仍走模板注入（{@code applyUserTemplate=true}）
     */
    @Override
    public PromptAugmenter.AugmentResult augment(List<Document> documents, Prompt prompt, Map<String, Object> context) {
        context.put(PROMPT_AUGMENTED, true);

        if (context.get(AiConstant.CTX_USER_ATTACHMENT) instanceof List<?> rawAttachments) {
            boolean supportMultimodal = attachmentSupport.resolveSupportMultimodal(context);
            UserMessageAttachmentSupport.AttachmentBuildResult built =
                    attachmentSupport.buildMediaAndTextBlocks(attachmentSupport.normalizeAttachments(rawAttachments), supportMultimodal);

            List<Media> mediaList = new ArrayList<>(built.mediaList());
            List<Message> messageList = prompt.getInstructions();
            // 从后往前找本轮 user，合并原有 media 并追加附件正文块
            for (int i = messageList.size() - 1; i >= 0; i--) {
                Message message = messageList.get(i);
                if (message instanceof UserMessage userMessage) {
                    mediaList.addAll(userMessage.getMedia());
                    String text = attachmentSupport.appendAttachmentToContent(userMessage.getText(), built.attachmentXml());
                    messageList.set(i, UserMessage.builder()
                            .metadata(userMessage.getMetadata())
                            .media(mediaList)
                            .text(text)
                            .build());
                    break;
                }
            }
        }
        if (!PromptAugmenter.super.needAugmentation(prompt, context)) {
            return new AugmentResult();
        }
        return PromptAugmenter.super.augment(documents, prompt, context);
    }
}
