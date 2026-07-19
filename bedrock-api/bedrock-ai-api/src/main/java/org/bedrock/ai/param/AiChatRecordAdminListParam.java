package org.bedrock.ai.param;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.ai.enums.AiChatTypeEnum;

import java.io.Serial;
import java.io.Serializable;

/**
 * 管理端：租户内全员聊天会话列表查询参数
 */
@Data
public class AiChatRecordAdminListParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "会话标题，模糊匹配")
    private String title;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "用户 id")
    private Long userId;

    @Schema(description = "用户昵称/用户名，模糊匹配")
    private String nickname;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "模型 id")
    private Long modelId;

    @Schema(description = "开始时间")
    private String startTime;

    @Schema(description = "结束时间")
    private String endTime;

    @Schema(description = "会话类型：CHAT / IMAGE / MINDMAP / ARTICLE")
    private AiChatTypeEnum chatType;

}
