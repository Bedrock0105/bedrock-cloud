package org.bedrock.ai.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bedrock.ai.dto.AiChatOptions;
//import org.bedrock.ai.handler.AiChatOptionsTypeHandler;
import org.bedrock.common.tenant.base.TenantEntity;

import java.io.Serial;

/**
 * AI 对话角色配置
 * <p>定义 AI 对话中使用的角色人设与系统提示词</p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "bedrock_ai_role", autoResultMap = true)
@Schema(description = "AI 对话角色配置")
public class AiRole extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

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

    @Schema(description = "对话调用配置（工具、知识库检索等）")
    @TableField(value = "chat_options", typeHandler = JacksonTypeHandler.class)
    private AiChatOptions chatOptions;

    @Schema(description = "配置状态（1=启用，0=禁用）")
    private Integer status;

}
