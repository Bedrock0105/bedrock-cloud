package org.bedrock.ai.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * Token 用量详情 VO。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Token 用量详情 VO")
public class AiTokenUsageDetailVO extends AiTokenUsageListVO {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "user 消息 id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userMessageId;

    @Schema(description = "assistant 消息 id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long assistantMessageId;

    @Schema(description = "首 token 耗时(ms)")
    private Integer firstTokenLatencyMs;

    @Schema(description = "流式生成耗时(ms)")
    private Integer streamingDurationMs;

    @Schema(description = "输出吞吐(tokens/s)")
    private Double tokensPerSecond;

    @Schema(description = "请求开始时间")
    private LocalDateTime startedAt;

    @Schema(description = "请求结束时间")
    private LocalDateTime completedAt;

}
