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
@EqualsAndHashCode(callSuper = true)
@TableName("bedrock_permission_datascope")
public class PermissionDatascope extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "菜单ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long menuId;

    @Schema(description = "权限名称")
    private String name;

    /**
     * Mapper方法唯一标识（格式：全限定类名.方法名，如com.bedrock.mapper.UserMapper.list）
     */
    @Schema(description = "Mapper方法唯一标识（格式：全限定类名.方法名，如com.bedrock.mapper.UserMapper.list）")
    private String mapperId;

    /**
     * 数据范围类型（枚举：如ALL-全部数据、DEPT-本部门、OWN-本人等）
     */
    @Schema(description = "数据范围类型（枚举：如ALL-全部数据、DEPT-本部门、OWN-本人等）")
    private String scopeType;

    /**
     * 数据库过滤列名（如dept_id、create_user_id）
     */
    @Schema(description = "数据库过滤列名（如dept_id、create_user_id）")
    private String scopeColumn;

    /**
     * 对应Java实体类字段名（如deptId、createUserId，用于ORM映射）
     */
    @Schema(description = "对应Java实体类字段名（如deptId、createUserId，用于ORM映射）")
    private String scopeField;

    /**
     * 数据范围值（如部门ID列表"1,2,3"、用户ID"100"，为空时表示无固定值需动态获取）
     */
    @Schema(description = "数据范围值")
    private String scopeValue;

    /**
     * 描述
     */
    @Schema(description = "描述")
    private String remark;

}
