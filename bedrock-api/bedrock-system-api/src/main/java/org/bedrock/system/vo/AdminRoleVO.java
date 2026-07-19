package org.bedrock.system.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class AdminRoleVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键")
    private Long roleId;

    /**
     * 部门名称
     */
    @Schema(description = "角色名称")
    private String roleName;

    /**
     * 角色别名
     */
    @Schema(description = "角色别名")
    private String roleAlias;
}
