package org.bedrock.ai.vo;

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
 * Token 用量列表 VO。
 */
@Data
@Schema(description = "Token 用量列表 VO")
public class AiTokenUsageListVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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

    @Schema(description = "创建时间")
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    private LocalDateTime createTime;

}
