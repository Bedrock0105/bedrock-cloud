package org.bedrock.ai.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bedrock.common.tenant.base.TenantEntity;

import java.io.Serial;

/**
 * AI Skill 主表
 * <p>
 * 业务 {@code name} 由 SKILL.md frontmatter 解析；编辑时可随 SKILL.md 变更，
 * 变更时需同步本地目录与 SkillManager 注册。
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bedrock_ai_skill")
@Schema(description = "AI Skill")
public class AiSkill extends TenantEntity {

	@Serial
	private static final long serialVersionUID = 1L;

	@Schema(description = "展示名称（用户填写）")
	private String label;

	@Schema(description = "技能标识（SKILL.md frontmatter name；编辑可随 SKILL.md 变更）")
	private String name;

	@Schema(description = "技能描述（供模型语义匹配）")
	private String description;

	@Schema(description = "配置状态（1=启用，0=禁用）")
	private Integer status;

	@Schema(description = "备注")
	private String remark;

}
