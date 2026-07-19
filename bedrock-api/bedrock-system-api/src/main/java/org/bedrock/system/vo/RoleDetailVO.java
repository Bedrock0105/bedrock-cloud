package org.bedrock.system.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class RoleDetailVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键")
    private Long id;

    /**
     * 角色名称
     */
    @Schema(description = "角色名称")
    private String roleName;

    /**
     * 角色别名
     */
    @Schema(description = "角色别名")
    private String roleAlias;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sort;

    /**
     * 组织描述
     */
    @Schema(description = "角色描述")
    private String remark;

    /**
     * 菜单集合
     */
    @Schema(description = "菜单集合")
    private List<Long> menuIds;
}
