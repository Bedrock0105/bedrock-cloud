package org.bedrock.system.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class RoleDetailPermissionParam implements Serializable {

    @Serial
    private final static long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @Schema(description = "角色ID")
    private Long roleId;

    /**
     * 权限类型，1--->接口权限，2---->数据权限
     */
    @Schema(description = "权限类型，1--->接口权限，2---->数据权限")
    private Integer permType = 1;

}
