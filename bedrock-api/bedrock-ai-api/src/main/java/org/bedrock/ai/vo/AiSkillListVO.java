package org.bedrock.ai.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * AI Skill 列表 VO
 */
@Data
@Schema(description = "AI Skill 列表 VO")
public class AiSkillListVO implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.ASSIGN_ID)
	@JsonSerialize(using = ToStringSerializer.class)
	@Schema(description = "主键")
	private Long id;

	@Schema(description = "展示名称")
	private String label;

	@Schema(description = "技能标识")
	private String name;

	@Schema(description = "技能描述")
	private String description;

	@Schema(description = "配置状态（1=启用，0=禁用）")
	private Integer status;

	@Schema(description = "备注")
	private String remark;

}
