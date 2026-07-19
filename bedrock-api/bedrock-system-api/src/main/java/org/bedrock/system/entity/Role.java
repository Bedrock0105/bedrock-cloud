package org.bedrock.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bedrock.common.tenant.base.TenantEntity;

import java.io.Serial;

@Data
@Schema(description = "角色")
@TableName("bedrock_role")
@EqualsAndHashCode(callSuper = true)
public class Role extends TenantEntity {

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

}
