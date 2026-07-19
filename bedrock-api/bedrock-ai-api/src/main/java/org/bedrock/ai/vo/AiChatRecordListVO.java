package org.bedrock.ai.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.ai.enums.AiChatTypeEnum;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * AI 聊天会话列表 VO（用户端）
 */
@Data
@Schema(description = "AI 聊天会话列表 VO")
public class AiChatRecordListVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键")
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "模型 id")
    private Long modelId;

    @Schema(description = "模型名称")
    private String modelName;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "角色 id")
    private Long roleId;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色头像")
    private String roleAvatar;

    @Schema(description = "会话标题")
    private String title;

    @Schema(description = "对话类型")
    private AiChatTypeEnum chatType;

    @Schema(description = "是否置顶：0否 1是")
    private Integer isTop;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}
