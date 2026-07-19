package org.bedrock.system.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Schema(description = "管理员详情")
public class AdminDetailVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键")
    private Long id;

    /**
     * 账号
     */
    @Schema(description = "账号")
    private String username;

    /**
     * 租户
     */
    @Schema(description = "租户")
    private String tenantId;

    /**
     * 头像
     */
    @Schema(description = "头像")
    private String avatar;

    /**
     * 昵称
     */
    @Schema(description = "昵称")
    private String nickname;

    /**
     * 性别
     */
    @Schema(description = "性别")
    private String sex;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String phone;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;

    /**
     * 账号状态（1正常 0停用）
     */
    @Schema(description = "账号状态（1正常 0停用）")
    private Integer status;

    /**
     * 账号描述
     */
    @Schema(description = "账号描述")
    private String remark;

    /**
     * 部门
     */
    @Schema(description = "部门")
    private List<AdminDeptVO> adminDept;

    /**
     * 角色
     */
    @Schema(description = "角色")
    private List<AdminRoleVO> adminRole;
}
