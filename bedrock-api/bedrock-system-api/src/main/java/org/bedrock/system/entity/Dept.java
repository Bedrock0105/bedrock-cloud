package org.bedrock.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bedrock.common.tenant.base.TenantEntity;

import java.io.Serial;

@Data
@Schema(description = "组织机构")
@TableName("bedrock_dept")
@EqualsAndHashCode(callSuper = true)
public class Dept extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 上级id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "上级id")
    private Long parentId;

    /**
     * 祖籍列表
     */
    @Schema(description = "祖籍列表")
    private String ancestors;

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
     * 层级
     */
    @Schema(description = "层级")
    private Integer level;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sort;

    /**
     * 组织描述
     */
    @Schema(description = "组织描述")
    private String remark;
}
