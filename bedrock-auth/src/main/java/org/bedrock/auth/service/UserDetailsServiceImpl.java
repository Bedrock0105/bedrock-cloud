package org.bedrock.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bedrock.auth.enums.OauthErrorEnums;
import org.bedrock.auth.model.UserDetailsModel;
import org.bedrock.auth.util.TokenUtil;
import org.bedrock.common.auth.util.AuthUtil;
import org.bedrock.common.authentication.exception.OAuth2Exception;
import org.bedrock.common.authentication.exception.UsernameNotFoundException;
import org.bedrock.common.authentication.model.OauthTokenParams;
import org.bedrock.common.authentication.service.UserDetailsService;
import org.bedrock.common.authentication.support.UserDetails;
import org.bedrock.common.code.api.R;
import org.bedrock.common.code.constant.StringPool;
import org.bedrock.common.code.util.CollectionUtil;
import org.bedrock.common.mybatisplus.constant.BedrockDBConstant;
import org.bedrock.system.cache.SystemCache;
import org.bedrock.system.dto.LoginInfo;
import org.bedrock.system.entity.Admin;
import org.bedrock.system.entity.Dept;
import org.bedrock.system.entity.Role;
import org.bedrock.system.feign.IAdminClient;
import org.bedrock.system.vo.TenantDetailVO;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IAdminClient adminClient;

    @NonNull
    @Override
    public UserDetails loadUserByUsername(String username, OauthTokenParams params) {
        /**
         * 校验租户信息
         */
        String tenantId = AuthUtil.getTenantId();
        TenantDetailVO tenantDetail = SystemCache.getTenantDetail(tenantId);
        TokenUtil.checkTenant(tenantDetail);
        /**
         * 获取用户信息
         */
        R<LoginInfo> loginInfoR = adminClient.selectAdminLoginInfoByUsername(username, tenantId);
        if (!loginInfoR.isSuccess()) {
            log.error("用户不存在 feign 调用异常 code:{},msg{}", loginInfoR.getCode(), loginInfoR.getMsg());
            throw new UsernameNotFoundException();
        }
        LoginInfo data = loginInfoR.getData();
        if (data == null) {
            log.error("用户不存在 LoginInfo 是空的,{}", username);
            throw new UsernameNotFoundException();
        }
        Admin admin = data.getAdmin();
        List<Dept> depts = data.getAdminDept();
        List<Role> roles = data.getAdminRole();
        if (CollectionUtil.isEmpty(depts)) {
            log.error("用户不存在 depts 是空的,{}:{}", admin.getId(), username);
            throw new OAuth2Exception(OauthErrorEnums.USER_NOT_DEPT.getCode(), OauthErrorEnums.USER_NOT_DEPT.getMessage());
        }
        if (CollectionUtil.isEmpty(roles)) {
            log.error("用户不存在 roles 是空的,{}:{}", admin.getId(), username);
            throw new OAuth2Exception(OauthErrorEnums.USER_NOT_ROLE.getCode(), OauthErrorEnums.USER_NOT_ROLE.getMessage());
        }
        StringJoiner roleIds = new StringJoiner(StringPool.COMMA);
        StringJoiner roleAlias = new StringJoiner(StringPool.COMMA);
        for (Role role : roles) {
            roleIds.add(role.getId().toString());
            roleAlias.add(role.getRoleAlias());
        }
        return new UserDetailsModel(admin.getUsername(),
                admin.getPassword(),
                BedrockDBConstant.DB_STATUS_NORMAL.equals(admin.getStatus()),
                admin.getId(),
                admin.getTenantId(),
                admin.getPhone(),
                admin.getNickname(),
                depts.stream()
                        .map(dept -> dept.getId().toString())
                        .collect(Collectors.joining(StringPool.COMMA)),
                roleIds.toString(),
                roleAlias.toString(),
                data.getParams()
        );
    }
}
