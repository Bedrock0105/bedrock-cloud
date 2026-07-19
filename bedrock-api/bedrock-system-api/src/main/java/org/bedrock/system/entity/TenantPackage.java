package org.bedrock.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bedrock.common.mybatisplus.base.BaseEntity;

@Data
@TableName("bedrock_tenant_package")
@EqualsAndHashCode(callSuper = true)
public class TenantPackage extends BaseEntity {

    /**
     * 套餐名称
     */
    @Schema(description = "套餐名称")
    private String name;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    /**
     * 菜单id
     */
    @Schema(description = "菜单id")
    private String menuIds;
}
