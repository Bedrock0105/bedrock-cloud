package org.bedrock.ai.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.ai.dto.UserMessageAttachment;
import org.bedrock.common.code.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * AI 聊天消息明细列表 VO（对应 bedrock_ai_chat_message 表，用于历史消息回显）
 */
@Data
@Schema(description = "AI聊天消息明细列表")
public class AiChatMessageListVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "消息 id")
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "会话 id")
    private Long recordId;

    @Schema(description = "消息角色：user / assistant")
    private String role;

    @Schema(description = "消息内容")
    private String content;

    @Schema(description = "推理/思考过程内容")
    private String reasoning;

    /** 用户消息附件列表（text 列 JSON） */
    @Schema(description = "用户消息附件列表")
    private List<UserMessageAttachment> attachments;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    private LocalDateTime createTime;
}
