package org.bedrock.system.entity;

import java.io.Serial;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bedrock.common.tenant.base.TenantEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bedrock_permission_api")
public class PermissionApi extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "菜单ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long menuId;

    @Schema(description = "权限名称")
    private String name;

    /**
     * 权限标识
     */
    @Schema(description = "权限标识")
    private String permission;

    /**
     * 接口路径（例：/api/user/list，与bedrock_api表path字段保持一致）
     */
    @Schema(description = "接口路径（例：/api/user/list，与bedrock_api表path字段保持一致）")
    private String path;

    /**
     * HTTP请求方法（枚举：GET、POST、PUT、DELETE、PATCH等）
     */
    @Schema(description = "HTTP请求方法（枚举：GET、POST、PUT、DELETE、PATCH等）")
    private String method;

    /**
     * 描述
     */
    @Schema(description = "描述")
    private String remark;

}
