package org.bedrock.ai.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * AI Skill 列表查询参数
 */
@Data
public class AiSkillListParam implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Schema(description = "展示名称")
	private String label;

	@Schema(description = "技能标识")
	private String name;

	@Schema(description = "配置状态（1=启用，0=禁用）")
	private Integer status;

}
