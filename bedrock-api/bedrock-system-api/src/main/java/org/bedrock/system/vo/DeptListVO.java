package org.bedrock.system.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class DeptListVO implements Serializable {

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

    @Schema(description = "是否有子部门")
    private boolean hasChildren;
}
