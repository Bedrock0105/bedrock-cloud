package org.bedrock.ai.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * AI 图片生成响应。
 * <p>
 * 一次请求可能生成多张图片，每张图片以 {@link AiImageItemVO} 表示独立的成功/失败状态。
 * </p>
 */
@Schema(description = "AI 图片生成响应")
public record AiImageResultVO(
        @JsonSerialize(using = ToStringSerializer.class)
        @Schema(description = "会话 id（chatType=IMAGE）")
        Long recordId,
        @Schema(description = "生成结果列表")
        List<AiImageItemVO> images
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 单张图片生成结果。
     */
    @Schema(description = "单张图片生成结果")
    public record AiImageItemVO(
            @Schema(description = "是否成功") boolean success,
            @Schema(description = "图片 URL（成功时）") String url,
            @Schema(description = "错误信息（失败时）") String message
    ) implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        public static AiImageItemVO ok(String url) {
            return new AiImageItemVO(true, url, null);
        }

        public static AiImageItemVO fail(String message) {
            return new AiImageItemVO(false, null, message);
        }
    }
}
