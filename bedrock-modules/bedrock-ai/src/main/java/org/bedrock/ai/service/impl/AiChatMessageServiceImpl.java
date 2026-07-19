package org.bedrock.ai.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.bedrock.ai.constant.AiConstant;
import org.bedrock.ai.entity.AiChatMessage;
import org.bedrock.ai.enums.AiChatTypeEnum;
import org.bedrock.ai.mapper.AiChatMessageMapper;
import org.bedrock.ai.service.IAiChatMessageService;
import org.bedrock.ai.support.UserMessageAttachmentSupport;
import org.bedrock.ai.vo.AiChatMessageListVO;
import org.bedrock.ai.vo.AiChatRecordDetailVO;
import org.bedrock.ai.vo.AiModelCheckVO;
import org.bedrock.common.ai.advisor.history.ChatHistoryStore;
import org.bedrock.common.auth.entity.AuthUser;
import org.bedrock.common.code.util.NumberUtil;
import org.bedrock.common.code.util.StringUtil;
import org.bedrock.common.mybatisplus.constant.BedrockDBConstant;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.content.Media;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * AI 聊天消息明细 Service
 * <p>同时实现 {@link ChatHistoryStore}，供 {@link org.bedrock.common.ai.advisor.ChatHistoryAdvisor} 持久化消息</p>
 */
@Service
@RequiredArgsConstructor
public class AiChatMessageServiceImpl extends ServiceImpl<AiChatMessageMapper, AiChatMessage>
        implements IAiChatMessageService, ChatHistoryStore {

    private final UserMessageAttachmentSupport attachmentSupport;

    /**
     * 将窗口起点对齐到完整 turn：丢弃开头连续的 assistant / tool 等，直到第一条 user。
     * <p>
     * SQL {@code LIMIT} 可能裁在 tool 链路中间，避免模型看到无 user 开头的孤儿消息。
     * </p>
     */
    @Override
    public List<ChatEntry> get(String conversationId, int maxMessages, Map<String, Object> context) {
        /**
         * 避免文章生成时，文章内容被当作对话历史
         */
        if (context.get(AiConstant.CTX_CHAT_RECORD) instanceof AiChatRecordDetailVO recordDetailVO && recordDetailVO.getChatType() == AiChatTypeEnum.ARTICLE) {
            return Collections.emptyList();
        }
        List<AiChatMessage> messages = baseMapper.selectHistoryMessages(Long.parseLong(conversationId), maxMessages * 2);
        Collections.reverse(messages);
        int start = 0;
        while (start < messages.size() && !MessageType.USER.getValue().equals(messages.get(start).getRole())) {
            start++;
        }
        if (start > 0) {
            messages.subList(0, start).clear();
        }
        return messages.stream().map(m -> toChatEntry(m, context)).toList();
    }

    @Override
    public String add(ChatEntry chatEntry) {
        AiChatMessage message = buildMessage(chatEntry);
        message.setParentId(NumberUtil.toLong(chatEntry.parentId(), 0L));
        message.setContent(chatEntry.content());
        message.setReasoning(chatEntry.reasoning());
        message.setToolCalls(chatEntry.toolCalls());
        // 不序列化 media：附件由 AiPromptAugmenter 写入 attachments 列
        this.save(message);
        return message.getId().toString();
    }

    @Override
    public void update(String messageId, ChatEntry chatEntry) {
        this.update(Wrappers.<AiChatMessage>lambdaUpdate()
                .eq(AiChatMessage::getId, Long.parseLong(messageId))
                .set(StringUtil.isNotBlank(chatEntry.content()), AiChatMessage::getContent, chatEntry.content())
                .set(StringUtil.isNotBlank(chatEntry.reasoning()), AiChatMessage::getReasoning, chatEntry.reasoning())
                .set(StringUtil.isNotBlank(chatEntry.toolCalls()), AiChatMessage::getToolCalls, chatEntry.toolCalls()));
    }

    @Override
    public void delete(String messageId) {
        this.update(Wrappers.<AiChatMessage>lambdaUpdate()
                .eq(AiChatMessage::getId, Long.parseLong(messageId))
                .set(AiChatMessage::getIsDeleted, BedrockDBConstant.DB_IS_DELETED));
    }

    @Override
    public List<AiChatMessageListVO> selectMessageListByRecordId(Long recordId) {
        return baseMapper.selectMessageListByRecordId(recordId);
    }

    private AiChatMessage buildMessage(ChatEntry chatEntry) {
        AiChatMessage message = new AiChatMessage();
        message.setRecordId(NumberUtil.toLong(chatEntry.conversationId(), 0L));
        message.setRole(chatEntry.role());
        message.setIsDeleted(BedrockDBConstant.DB_NOT_DELETED);
        Map<String, Object> context = chatEntry.context();
        if (context.get(AiConstant.CTX_USER_INFO) instanceof AuthUser authUser) {
            message.setUserId(authUser.getUserId());
        } else if (context.get(AiConstant.CTX_CHAT_RECORD) instanceof AiChatRecordDetailVO aiChatRecord) {
            message.setUserId(aiChatRecord.getUserId());
        }
        if (context.get(AiConstant.CTX_MODEL_DETAIL) instanceof AiModelCheckVO aiModelCheckVO) {
            message.setModelId(aiModelCheckVO.getId());
            message.setModel(aiModelCheckVO.getModel());
        }
        return message;
    }

    /**
     * DB 行 → ChatEntry；附件还原逻辑与 {@link org.bedrock.ai.advisor.prompt.AiPromptAugmenter} 一致
     * （按是否多模态解析 Media / 抽文本拼正文；不回灌 RAG）。
     */
    private ChatEntry toChatEntry(AiChatMessage message, Map<String, Object> context) {
        String content = message.getContent();
        List<Media> media = List.of();
        if (MessageType.USER.getValue().equals(message.getRole())
                && message.getAttachments() != null
                && !message.getAttachments().isEmpty()) {
            boolean supportMultimodal = attachmentSupport.resolveSupportMultimodal(context);
            UserMessageAttachmentSupport.AttachmentBuildResult built =
                    attachmentSupport.buildMediaAndTextBlocks(message.getAttachments(), supportMultimodal);
            media = built.mediaList();
            content = attachmentSupport.appendAttachmentToContent(content, built.attachmentXml());
        }
        return new ChatEntry(
                message.getRecordId() != null ? message.getRecordId().toString() : null,
                message.getParentId() != null ? message.getParentId().toString() : null,
                message.getRole(),
                content,
                message.getReasoning(),
                message.getToolCalls(),
                media,
                Collections.emptyMap());
    }
}
