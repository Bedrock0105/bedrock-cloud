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
 * AI 客服流式发送参数。
 * <p>模型取自租户客服配置，前端无需传 modelId；{@code id} 为空则新建 CUSTOMER_SERVICE 会话。</p>
 */
@Data
@Schema(description = "AI客服发送参数")
public class AiCsSendParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "客服会话 id，为空则按客服配置新建会话")
    private Long id;

    @Schema(description = "聊天内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;

    @Schema(description = "聊天附件")
    private List<UserMessageAttachment> attachments;
}
