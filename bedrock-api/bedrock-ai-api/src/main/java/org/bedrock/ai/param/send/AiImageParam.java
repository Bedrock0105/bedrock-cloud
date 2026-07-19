package org.bedrock.ai.param.send;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * AI 图片生成参数。
 * <p>
 * 通用字段适用于多数厂商；各平台特有参数通过 {@link #extra} 传入，
 * 由 {@link org.bedrock.ai.support.ModelOptionsSupport} 按厂商映射到对应 ImageOptions。
 * </p>
 */
@Data
@Schema(description = "AI 图片生成参数")
public class AiImageParam implements Serializable {

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "会话 id（可选；不传则新建 IMAGE 会话）")
    private Long recordId;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "使用的 AI 模型 ID（模型类型须为 IMAGE）", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long modelId;

    @Schema(description = "提示词", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;

    @Schema(description = "图片宽度（像素，可选，未传时使用模型默认值）")
    private Integer width;

    @Schema(description = "图片高度（像素，可选，未传时使用模型默认值）")
    private Integer height;

    @Schema(description = "生成数量（可选，未传时使用模型默认值）")
    private Integer number;

    @Schema(description = "图片风格（可选，如 OpenAI 的 vivid / natural，DashScope 的 anime 等）")
    private String style;

    @Schema(description = "图片质量（可选，如 OpenAI 的 standard / hd）")
    private String quality;

    /**
     * 厂商扩展参数，key 与 Spring AI 各平台 ImageOptions 字段对齐（camelCase）。
     * <p>未列出的 key 会被忽略；类型须与目标字段一致（String / Integer / Long / Boolean / Float）。</p>
     *
     * <h4>OpenAI / 千帆 / 火山 / 硅基流动 / xAI（OpenAI 兼容协议）</h4>
     * <ul>
     *   <li>{@code user}：终端用户标识，便于平台审计</li>
     * </ul>
     *
     * <h4>DashScope（通义万相）</h4>
     * <ul>
     *   <li>{@code seed}：随机种子（Integer）</li>
     *   <li>{@code negativePrompt}：反向提示词</li>
     *   <li>{@code refImg}：参考图 URL</li>
     *   <li>{@code refMode}：参考模式，如 {@code style}、{@code content}、{@code style-content}</li>
     *   <li>{@code refStrength}：参考图相似度（Float，0~1）</li>
     *   <li>{@code watermark}：是否添加 AI 水印（Boolean）</li>
     *   <li>{@code promptExtend}：是否扩展提示词（Boolean）</li>
     *   <li>{@code responseFormat}：{@code url} 或 {@code b64_json}</li>
     *   <li>{@code baseImageUrl} / {@code maskImageUrl}：局部重绘</li>
     *   <li>{@code sketchImageUrl} / {@code sketchWeight} / {@code sketchExtraction}：线稿生图</li>
     *   <li>{@code maxImages} / {@code enableInterleave}：多图交错生成</li>
     * </ul>
     *
     * <h4>Azure OpenAI</h4>
     * <ul>
     *   <li>{@code deploymentName}：部署名称（未传时默认使用模型标识）</li>
     *   <li>{@code user}：终端用户标识</li>
     * </ul>
     *
     * <h4>智谱（ZhiPu）</h4>
     * <ul>
     *   <li>width / height / number / style 请使用上方通用字段</li>
     * </ul>
     */
    @Schema(description = "厂商扩展参数，详见类注释")
    private Map<String, Object> extra;
}
