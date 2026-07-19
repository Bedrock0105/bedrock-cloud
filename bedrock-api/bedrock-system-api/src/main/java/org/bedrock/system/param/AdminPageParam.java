package org.bedrock.system.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class AdminPageParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 账号
     */
    @Schema(description = "账号")
    private String username;

    /**
     * 昵称
     */
    @Schema(description = "昵称")
    private String nickname;

    /**
     * 账号状态（1正常 0停用）
     */
    @Schema(description = "账号状态（1正常 0停用）")
    private Integer status;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String phone;

    /**
     * 性别
     */
    @Schema(description = "性别")
    private String sex;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "部门")
    private Long deptId;

    @Schema(description = "角色")
    private Long roleId;
}
