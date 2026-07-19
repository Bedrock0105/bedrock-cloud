package org.bedrock.ai.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Token 用量统计概览 VO（Dashboard 卡片 + 图表数据源）。
 */
@Data
@Schema(description = "Token 用量统计概览 VO")
public class AiTokenUsageStatsVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "累计调用次数")
    private Long totalCalls = 0L;

    @Schema(description = "累计总 token")
    private Long totalTokens = 0L;

    @Schema(description = "累计输入 token")
    private Long totalPromptTokens = 0L;

    @Schema(description = "累计输出 token")
    private Long totalCompletionTokens = 0L;

    @Schema(description = "今日调用次数")
    private Long todayCalls = 0L;

    @Schema(description = "今日总 token")
    private Long todayTokens = 0L;

    @Schema(description = "按模型统计")
    private List<AiTokenUsageModelStatsVO> modelStats = new ArrayList<>();

    @Schema(description = "近 7 日趋势")
    private List<AiTokenUsageTrendVO> trends = new ArrayList<>();

}
