package org.bedrock.ai.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.ai.constant.AiConstant;
import org.bedrock.ai.enums.SkillNodeType;
import org.bedrock.common.code.tree.INode;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * AI Skill 文件树节点 VO
 */
@Data
@Schema(description = "AI Skill 文件树节点 VO")
public class AiSkillFileVO implements Serializable, INode<AiSkillFileVO, Long> {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键")
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "关联 skill id")
    private Long skillId;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "父节点 id，根下为 0")
    private Long parentId;

    @Schema(description = "节点类型")
    private SkillNodeType nodeType = SkillNodeType.FILE;

    @Schema(description = "节点名")
    private String name;

    @Schema(description = "包内相对路径")
    private String relativePath;

    @Schema(description = "是否文本：1=content（AiConstant.SKILL_FILE_IS_TEXT_YES），0=file_url（AiConstant.SKILL_FILE_IS_TEXT_NO）")
    private Integer isText = AiConstant.SKILL_FILE_IS_TEXT_YES;

    @Schema(description = "文本内容")
    private String content;

    @Schema(description = "二进制全路径 URL")
    private String fileUrl;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "字节大小")
    private Long fileSize;

    @Schema(description = "文件后缀")
    private String fileSuffix;

    @Schema(description = "同级排序")
    private Integer sort;

    @Schema(description = "子节点（树形占位）")
    private List<AiSkillFileVO> children = new ArrayList<>();
}
