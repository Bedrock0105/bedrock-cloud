package org.bedrock.system.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RoleListParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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
}
