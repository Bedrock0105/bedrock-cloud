package org.bedrock.ai.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.ai.dto.AiChatOptions;
import org.bedrock.ai.enums.AiChatTypeEnum;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 管理端：聊天会话详情 VO（含用户信息与 Token 汇总）
 */
@Data
@Schema(description = "管理端聊天会话详情 VO")
public class AiChatRecordAdminDetailVO implements Serializable {

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

    @Schema(description = "模型标识")
    private String model;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "用户 id")
    private Long userId;

    @Schema(description = "用户昵称")
    private String nickname;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "角色 id")
    private Long roleId;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色头像")
    private String roleAvatar;

    @Schema(description = "会话标题")
    private String title;

    @Schema(description = "会话类型")
    private AiChatTypeEnum chatType;

    @Schema(description = "系统提示词")
    private String systemPrompt;

    @Schema(description = "是否置顶：0否 1是")
    private Integer isTop;

    @Schema(description = "对话调用配置")
    private AiChatOptions chatOptions;

    @Schema(description = "累计输入 token")
    private Integer totalPromptTokens;

    @Schema(description = "累计输出 token")
    private Integer totalCompletionTokens;

    @Schema(description = "累计总 token")
    private Integer totalTokens;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}
