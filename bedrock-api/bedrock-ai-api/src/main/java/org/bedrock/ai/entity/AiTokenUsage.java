package org.bedrock.ai.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.common.ai.enums.AiPlatformEnum;
import org.bedrock.common.ai.enums.TokenUsageSource;
import org.bedrock.common.code.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * AI Token 用量明细实体，对应 {@code bedrock_ai_token_usage} 表。
 * <p>
 * 每次模型调用结束后由 {@link org.bedrock.ai.service.impl.AiTokenUsageServiceImpl} 写入一条记录。
 * 通过 {@code userMessageId} / {@code assistantMessageId} 关联本轮输入与输出消息，便于审计与追溯。
 * </p>
 */
@Data
@TableName("bedrock_ai_token_usage")
@Schema(description = "AI Token 用量明细")
public class AiTokenUsage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键")
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "会话 id")
    private Long recordId;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "用户 id")
    private Long userId;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "模型 id")
    private Long modelId;

    @Schema(description = "模型标识")
    private String model;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "API Key id")
    private Long apiKeyId;

    @Schema(description = "API Key 名称")
    private String apiKeyName;

    @Schema(description = "厂商平台")
    private AiPlatformEnum platform;

    /**
     * 本轮 user/tool 输入消息 id，对应 {@link org.bedrock.common.ai.advisor.history.ChatHistoryStore#USER_MESSAGE_ID}。
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "user 消息 id")
    private Long userMessageId;

    /**
     * 本轮 assistant 输出消息 id，对应 {@link org.bedrock.common.ai.advisor.history.ChatHistoryStore#ASSISTANT_MESSAGE_ID}。
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "assistant 消息 id")
    private Long assistantMessageId;

    @Schema(description = "是否流式")
    private Boolean stream;

    @Schema(description = "输入 token")
    private Integer promptTokens;

    @Schema(description = "输出 token")
    private Integer completionTokens;

    @Schema(description = "总 token")
    private Integer totalTokens;

    @Schema(description = "用量来源")
    private TokenUsageSource usageSource;

    @Schema(description = "端到端总耗时(ms)")
    private Integer totalLatencyMs;

    @Schema(description = "首 token 耗时(ms)")
    private Integer firstTokenLatencyMs;

    @Schema(description = "流式生成耗时(ms)")
    private Integer streamingDurationMs;

    @Schema(description = "输出吞吐(tokens/s)")
    private Double tokensPerSecond;

    @Schema(description = "请求开始时间")
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    private LocalDateTime startedAt;

    @Schema(description = "请求结束时间")
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    private LocalDateTime completedAt;

    @Schema(description = "租户ID")
    @TableField(value = "tenant_id", fill = FieldFill.INSERT)
    private String tenantId;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "删除状态 0 是未删除，1 是已经删除")
    @TableLogic
    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    private Integer isDeleted;
}
