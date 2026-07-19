package org.bedrock.ai.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.bedrock.ai.constant.AiConstant;
import org.bedrock.ai.entity.AiTokenUsage;
import org.bedrock.ai.mapper.AiTokenUsageMapper;
import org.bedrock.ai.param.AiTokenUsageListParam;
import org.bedrock.ai.service.IAiChatRecordService;
import org.bedrock.ai.service.IAiTokenUsageService;
import org.bedrock.ai.vo.*;
import org.bedrock.common.ai.advisor.history.ChatHistoryStore;
import org.bedrock.common.ai.advisor.usage.TokenUsageRecorder;
import org.bedrock.common.auth.entity.AuthUser;
import org.bedrock.common.code.util.NumberUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Token 用量服务实现。
 */
@Service
@RequiredArgsConstructor
public class AiTokenUsageServiceImpl extends ServiceImpl<AiTokenUsageMapper, AiTokenUsage>
        implements IAiTokenUsageService, TokenUsageRecorder {

    private final IAiChatRecordService aiChatRecordService;

    @Override
    public AiTokenUsageDetailVO detail(Long id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public IPage<AiTokenUsageListVO> selectTokenUsageListPage(IPage<AiTokenUsageListVO> page,
                                                              AiTokenUsageListParam param) {
        return page.setRecords(baseMapper.selectTokenUsageList(page, param));
    }

    @Override
    public AiTokenUsageStatsVO statsOverview() {
        AiTokenUsageStatsVO stats = new AiTokenUsageStatsVO();
        stats.setTotalCalls(defaultLong(baseMapper.selectTotalCalls()));
        stats.setTotalTokens(defaultLong(baseMapper.selectTotalTokens()));
        stats.setTotalPromptTokens(defaultLong(baseMapper.selectTotalPromptTokens()));
        stats.setTotalCompletionTokens(defaultLong(baseMapper.selectTotalCompletionTokens()));
        stats.setTodayCalls(defaultLong(baseMapper.selectTodayCalls()));
        stats.setTodayTokens(defaultLong(baseMapper.selectTodayTokens()));
        stats.setModelStats(baseMapper.selectModelStats());
        stats.setTrends(baseMapper.selectRecentTrends(7));
        return stats;
    }

    /**
     * 记录一次模型调用的 Token 用量。
     * <p>
     * 消息 id 说明：
     * <ul>
     *   <li>{@code userMessageId} — 本轮输入（user/tool）消息，关联 prompt token</li>
     *   <li>{@code assistantMessageId} — 本轮 assistant 输出消息，关联 completion token</li>
     * </ul>
     * 两者均由 {@link org.bedrock.common.ai.advisor.ChatHistoryAdvisor} 在 before 阶段预插入并写入 context，应成对记录。
     * </p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recordTokenUsage(TokenUsageRecord record) {
        Map<String, Object> context = record.advisorContext();
        AiTokenUsage tokenUsage = new AiTokenUsage();
        /**
         * 获取会话记录 id
         */
        if (context.get(AiConstant.CTX_CHAT_RECORD) instanceof AiChatRecordDetailVO detailVO) {
            tokenUsage.setRecordId(detailVO.getId());
        }
        /**
         * 设置当前操作人
         */
        if (context.get(AiConstant.CTX_USER_INFO) instanceof AuthUser authUser) {
            tokenUsage.setUserId(authUser.getUserId());
            tokenUsage.setTenantId(authUser.getTenantId());
        }
        /**
         * 设置模型信息
         */
        if (context.get(AiConstant.CTX_MODEL_DETAIL) instanceof AiModelCheckVO modelDetail) {
            tokenUsage.setModelId(modelDetail.getId());
            tokenUsage.setModel(modelDetail.getModel());
            tokenUsage.setApiKeyId(modelDetail.getApiKeyId());
            tokenUsage.setApiKeyName(modelDetail.getApiKeyName());
            tokenUsage.setPlatform(modelDetail.getPlatform());
        }
        long userMessageId = NumberUtil.toLong(context.get(ChatHistoryStore.USER_MESSAGE_ID));
        tokenUsage.setUserMessageId(userMessageId == 0L ? null : userMessageId);
        long assistantMessageId = NumberUtil.toLong(context.get(ChatHistoryStore.ASSISTANT_MESSAGE_ID));
        tokenUsage.setAssistantMessageId(assistantMessageId == 0L ? null : assistantMessageId);
        tokenUsage.setStream(record.stream());
        tokenUsage.setPromptTokens(record.usage().getPromptTokens());
        tokenUsage.setCompletionTokens(record.usage().getCompletionTokens());
        tokenUsage.setTotalTokens(record.usage().getTotalTokens());
        tokenUsage.setUsageSource(record.usageSource());
        tokenUsage.setTotalLatencyMs(record.totalLatencyMs());
        tokenUsage.setFirstTokenLatencyMs(record.firstTokenLatencyMs());
        tokenUsage.setStreamingDurationMs(record.streamingDurationMs());
        tokenUsage.setTokensPerSecond(record.tokensPerSecond());
        tokenUsage.setStartedAt(record.startedAt());
        tokenUsage.setCompletedAt(record.completedAt());
        this.save(tokenUsage);
        /**
         * 更新会话记录的 Token 用量
         */
        if (tokenUsage.getRecordId() != null) {
            aiChatRecordService.updateTokenUsage(tokenUsage.getRecordId(), record.usage());
        }
    }

    private long defaultLong(Long value) {
        return value == null ? 0L : value;
    }

}
