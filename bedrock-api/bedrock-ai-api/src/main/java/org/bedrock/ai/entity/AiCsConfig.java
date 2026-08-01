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
import org.bedrock.common.tenant.base.TenantEntity;

import java.io.Serial;

/**
 * AI 客服配置（租户级单条）。
 * <p>每个租户最多一条有效配置，用于右下角客服浮窗的人设、模型与知识库绑定。</p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "bedrock_ai_cs_config", autoResultMap = true)
@Schema(description = "AI 客服配置")
public class AiCsConfig extends TenantEntity {

	@Serial
	private static final long serialVersionUID = 1L;

	@Schema(description = "客服名称")
	private String name;

	@Schema(description = "客服头像")
	private String avatar;

	@Schema(description = "描述")
	private String remark;

	@Schema(description = "系统提示词")
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
