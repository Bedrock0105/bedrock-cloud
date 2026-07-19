package org.bedrock.system.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.common.code.tree.INode;
import org.bedrock.common.code.tree.TreeNode;
import org.bedrock.common.code.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class DeptTreeVO implements Serializable, INode<DeptTreeVO, Long> {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键")
    private Long id;

    /**
     * 上级id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "上级id")
    private Long parentId;

    /**
     * 组织类型(保留字段)
     */
    @Schema(description = "组织类型")
    private Integer category;

    /**
     * 部门名称
     */
    @Schema(description = "部门名称")
    private String deptName;

    /**
     * 部门编号
     */
    @Schema(description = "部门编号")
    private String deptCode;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "子节点")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<DeptTreeVO> children = new ArrayList<>();

    @Schema(description = "创建时间")
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    private LocalDateTime createTime;
}
