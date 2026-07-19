package org.bedrock.ai.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Token 用量日趋势（近 7 日折线图）。
 */
@Data
@Schema(description = "Token 用量日趋势")
public class AiTokenUsageTrendVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "统计日期 yyyy-MM-dd")
    private String statDate;

    private Long callCount;

    private Long totalTokens;

}
