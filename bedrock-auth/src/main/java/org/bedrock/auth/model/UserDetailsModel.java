package org.bedrock.auth.model;

import org.bedrock.common.authentication.support.UserDetails;

import java.util.Map;

/**
 * @param username  用户名
 * @param password  密码
 * @param enabled   账号是否启用
 * @param userId    用户id
 * @param tenantId  租户id
 * @param phone     手机号
 * @param nickname  昵称
 * @param deptIds   部门id
 * @param roleIds   角色id
 * @param roleAlias 角色别名
 * @param params    额外参数
 */
public record UserDetailsModel(String username,
                               String password,
                               boolean enabled,
                               Long userId,
                               String tenantId,
                               String phone,
                               String nickname,
                               String deptIds,
                               String roleIds,
                               String roleAlias,
                               Map<String, Object> params
) implements UserDetails {

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
