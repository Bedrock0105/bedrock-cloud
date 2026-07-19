package org.bedrock.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bedrock.common.tenant.base.TenantEntity;

import java.io.Serial;

@Data
@Schema(description = "管理员")
@TableName("bedrock_admin")
@EqualsAndHashCode(callSuper = true)
public class Admin extends TenantEntity {

    @Serial
    private final static long serialVersionUID = 1L;

    /**
     * 账号
     */
    @Schema(description = "账号")
    private String username;

    /**
     * 密码
     */
    @Schema(description = "密码")
    private String password;

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
}
