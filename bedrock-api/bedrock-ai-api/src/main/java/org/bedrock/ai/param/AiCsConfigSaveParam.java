package org.bedrock.ai.param;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.ai.dto.AiChatOptions;

import java.io.Serial;
import java.io.Serializable;

/**
 * AI 客服配置保存参数（租户 upsert）。
 */
@Data
public class AiCsConfigSaveParam implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@JsonSerialize(using = ToStringSerializer.class)
	@Schema(description = "主键，有则更新无则新增")
	private Long id;

	@Schema(description = "客服名称", requiredMode = Schema.RequiredMode.REQUIRED)
	private String name;

	@Schema(description = "客服头像")
	private String avatar;

	@Schema(description = "描述")
	private String remark;

	@Schema(description = "系统提示词", requiredMode = Schema.RequiredMode.REQUIRED)
	private String systemPrompt;

	@JsonSerialize(using = ToStringSerializer.class)
	@Schema(description = "默认模型 id", requiredMode = Schema.RequiredMode.REQUIRED)
	private Long modelId;

	@Schema(description = "对话调用配置（知识库必填）")
	private AiChatOptions chatOptions;

	@Schema(description = "配置状态（1=启用，0=禁用）")
	private Integer status;

}
