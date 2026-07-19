package org.bedrock.ai.param.send;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * AI 文章写作参数（流式接口专用）。
 * <p>字典字段传 value：字数区间 / 文体 / 语气 / 语言 / 格式。</p>
 */
@Data
@Schema(description = "AI文章写作参数")
public class AiArticleParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "分组/会话 id，为空则创建新分组")
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "使用的AI模型ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long modelId;

    @Schema(description = "文章标题", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "字数区间字典值（aiArticleWordRange）", requiredMode = Schema.RequiredMode.REQUIRED)
    private String wordRange;

    @Schema(description = "文体字典值（aiArticleGenre）", requiredMode = Schema.RequiredMode.REQUIRED)
    private String genre;

    @Schema(description = "语气字典值（aiArticleTone）", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tone;

    @Schema(description = "语言字典值（aiArticleLang）", requiredMode = Schema.RequiredMode.REQUIRED)
    private String language;

    @Schema(description = "格式字典值（aiArticleFormat）", requiredMode = Schema.RequiredMode.REQUIRED)
    private String format;

    @Schema(description = "关键词，逗号分隔")
    private String keywords;

    @Schema(description = "用户提示词", requiredMode = Schema.RequiredMode.REQUIRED)
    private String extraRequirements;
}
