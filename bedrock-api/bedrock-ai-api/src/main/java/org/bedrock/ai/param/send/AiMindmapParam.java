package org.bedrock.ai.param.send;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * AI 思维导图生成参数（流式接口专用，字段形态对齐 {@link AiChatSendParam}）
 */
@Data
@Schema(description = "AI思维导图生成参数")
public class AiMindmapParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "分组/会话 id，为空则创建新分组")
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "使用的AI模型ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long modelId;

    @Schema(description = "用户提示词", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;
}
