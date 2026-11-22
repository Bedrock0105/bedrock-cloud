package org.bedrock.ai.param;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.ai.constant.AiConstant;
import org.bedrock.ai.enums.SkillNodeType;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * AI Skill 提交参数
 */
@Data
public class AiSkillSubmitParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "展示名称（用户填写）")
    private String label;

    @Schema(description = "技能标识（来自 SKILL.md frontmatter name；提交/编辑时由服务端解析覆盖，编辑可变更）")
    private String name;

    @Schema(description = "技能描述")
    private String description;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "文件树节点（可选）")
    private List<AiSkillFileItem> files;

    /**
     * Skill 文件树节点项（上传解析 / 提交共用）。
     *
     * @param nodeType   节点类型
     * @param name       节点名
     * @param isText     是否文本：{@link AiConstant#SKILL_FILE_IS_TEXT_YES} / {@link AiConstant#SKILL_FILE_IS_TEXT_NO}
     * @param content    文本内容
     * @param fileUrl    二进制全路径 URL
     * @param fileSize   字节大小
     * @param fileSuffix 文件后缀
     * @param sort       同级排序
     */
    @Schema(description = "Skill 文件树节点项")
    public record AiSkillFileItem(@Schema(description = "节点类型") SkillNodeType nodeType,
                                  @Schema(description = "节点名") String name,
                                  @Schema(description = "是否文本：1=content（AiConstant.SKILL_FILE_IS_TEXT_YES），0=file_url（AiConstant.SKILL_FILE_IS_TEXT_NO）") Integer isText,
                                  @Schema(description = "文本内容") String content,
                                  @Schema(description = "二进制全路径 URL") String fileUrl,
                                  @JsonSerialize(using = ToStringSerializer.class) @Schema(description = "字节大小") Long fileSize,
                                  @Schema(description = "同级排序") Integer sort,
                                  @Schema(description = "子节点") List<AiSkillFileItem> children) {

        public AiSkillFileItem {
            if (nodeType == null) {
                nodeType = SkillNodeType.FILE;
            }
            if (isText == null) {
                isText = AiConstant.SKILL_FILE_IS_TEXT_YES;
            }
            if (sort == null) {
                sort = 0;
            }
            if (children == null) {
                children = new ArrayList<>();
            }
        }

    }

}
