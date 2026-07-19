package org.bedrock.ai.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.ai.dto.AiChatOptions;

import java.io.Serial;
import java.io.Serializable;

/**
 * AI 角色详情 VO
 */
@Data
public class AiRoleDetailVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色头像")
    private String roleAvatar;

    @Schema(description = "角色描述")
    private String remark;

    @Schema(description = "角色设定（系统提示词）")
    private String systemPrompt;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "默认模型 id")
    private Long modelId;

    @Schema(description = "默认模型名称")
    private String modelName;

    @Schema(description = "对话调用配置")
    private AiChatOptions chatOptions;

}
