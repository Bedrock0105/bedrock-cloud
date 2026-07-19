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
import org.bedrock.ai.enums.AiChatTypeEnum;
//import org.bedrock.ai.handler.AiChatOptionsTypeHandler;
import org.bedrock.common.tenant.base.TenantEntity;

/**
 * AI聊天会话主记录表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "bedrock_ai_chat_record", autoResultMap = true)
@Schema(description = "AI聊天会话主记录表")
public class AiChatRecord extends TenantEntity {

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "操作人用户ID")
    private Long userId;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "使用的AI模型ID，关联ai_model.id")
    private Long modelId;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "AI角色ID，关联ai_role表，为空代表无自定义角色")
    private Long roleId;

    @Schema(description = "会话标题，首次提问自动截取")
    private String title;

    @Schema(description = "本次会话固定角色提示词")
    private String systemPrompt;

    @Schema(description = "是否置顶：0否 1是")
    private Integer isTop;

    @Schema(description = "对话调用配置（温度、工具、知识库检索等）")
    @TableField(value = "chat_options", typeHandler = JacksonTypeHandler.class)
    private AiChatOptions chatOptions;

    @Schema(description = "累计输入token")
    private Integer totalPromptTokens;

    @Schema(description = "累计输出token")
    private Integer totalCompletionTokens;

    @Schema(description = "总消耗token")
    private Integer totalTokens;

    @Schema(description = "对话类型枚举标识")
    private AiChatTypeEnum chatType;
}
