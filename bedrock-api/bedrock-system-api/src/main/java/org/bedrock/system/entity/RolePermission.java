package org.bedrock.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
@TableName("bedrock_role_permission")
public class RolePermission {

    /**
     *
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "")
    private Long id;

    /**
     * 权限类型，1--->接口权限，2---->数据权限
     */
    @Schema(description = "权限类型，1--->接口权限，2---->数据权限")
    private Integer permType;

    /**
     * 角色id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "角色id")
    private Long roleId;

    /**
     * 权限id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "权限id")
    private Long permissionId;
}
