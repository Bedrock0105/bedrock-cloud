package org.bedrock.ai.param.send;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.ai.dto.UserMessageAttachment;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * AI 聊天发送参数（普通/流式发送接口专用，非消息明细表实体）
 */
@Data
@Schema(description = "AI聊天发送参数")
public class AiChatSendParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "聊天会话 id，为空则创建新会话")
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "使用的AI模型ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long modelId;

    @Schema(description = "聊天内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;

    @Schema(description = "聊天附件")
    private List<UserMessageAttachment> attachments;
}
