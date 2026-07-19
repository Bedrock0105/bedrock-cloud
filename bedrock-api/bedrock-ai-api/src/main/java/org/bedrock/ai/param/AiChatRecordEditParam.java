package org.bedrock.ai.param;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.ai.dto.AiChatOptions;

import java.io.Serial;
import java.io.Serializable;

/**
 * AI 聊天会话编辑参数
 */
@Data
@Schema(description = "AI 聊天会话编辑参数")
public class AiChatRecordEditParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "会话 id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "会话标题")
    private String title;

    @Schema(description = "系统提示词")
    private String systemPrompt;

    @Schema(description = "对话调用配置")
    private AiChatOptions chatOptions;

}
