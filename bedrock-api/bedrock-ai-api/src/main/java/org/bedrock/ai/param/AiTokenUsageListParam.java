package org.bedrock.ai.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.common.ai.enums.AiPlatformEnum;
import org.bedrock.common.ai.enums.TokenUsageSource;

import java.io.Serial;
import java.io.Serializable;

/**
 * Token 用量列表查询参数。
 */
@Data
public class AiTokenUsageListParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "模型标识")
    private String model;

    @Schema(description = "API Key 名称")
    private String apiKeyName;

    @Schema(description = "厂商平台")
    private AiPlatformEnum platform;

    @Schema(description = "用量来源")
    private TokenUsageSource usageSource;

    @Schema(description = "开始时间")
    private String startTime;

    @Schema(description = "结束时间")
    private String endTime;

}
