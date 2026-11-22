package org.bedrock.ai.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bedrock.ai.enums.SkillNodeType;
import org.bedrock.common.tenant.base.TenantEntity;

import java.io.Serial;

/**
 * AI Skill 文件树节点
 *
 * @TableName bedrock_ai_skill_file
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bedrock_ai_skill_file")
@Schema(description = "AI Skill 文件树节点")
public class AiSkillFile extends TenantEntity {

	@Serial
	private static final long serialVersionUID = 1L;

	@JsonSerialize(using = ToStringSerializer.class)
	@Schema(description = "关联 bedrock_ai_skill.id")
	private Long skillId;

	@JsonSerialize(using = ToStringSerializer.class)
	@Schema(description = "父节点 id，根下为 0")
	private Long parentId;

	@Schema(description = "节点类型：FILE / DIRECTORY")
	private SkillNodeType nodeType;

	@Schema(description = "节点名（文件名或文件夹名）")
	private String name;

	@Schema(description = "包内相对路径冗余")
	private String relativePath;

	@Schema(description = "是否文本：1=存 content（AiConstant.SKILL_FILE_IS_TEXT_YES），0=存 file_url（AiConstant.SKILL_FILE_IS_TEXT_NO）")
	private Integer isText;

	@Schema(description = "文本内容（is_text=1 时使用）")
	private String content;

	@Schema(description = "二进制文件访问全路径 URL（is_text=0 时使用）")
	private String fileUrl;

	@JsonSerialize(using = ToStringSerializer.class)
	@Schema(description = "字节大小")
	private Long fileSize;

	@Schema(description = "文件后缀；文件夹为空")
	private String fileSuffix;

	@Schema(description = "同级排序")
	private Integer sort;

}
