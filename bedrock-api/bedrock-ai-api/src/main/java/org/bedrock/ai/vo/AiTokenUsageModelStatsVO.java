package org.bedrock.ai.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 按模型聚合的 Token 用量统计（调用次数 TOP10 柱状图）。
 */
@Data
@Schema(description = "按模型 Token 用量统计")
public class AiTokenUsageModelStatsVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "模型标识")
    private String model;

    @Schema(description = "调用次数")
    private Long callCount;

    @Schema(description = "总 token")
    private Long totalTokens;

}
