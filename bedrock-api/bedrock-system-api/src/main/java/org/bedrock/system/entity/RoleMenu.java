package org.bedrock.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
@TableName("bedrock_role_menu")
public class RoleMenu {

    /**
     *
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "")
    private Long id;

    /**
     * 角色id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "角色id")
    private Long roleId;

    /**
     * 菜单id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "菜单id")
    private Long menuId;
}
