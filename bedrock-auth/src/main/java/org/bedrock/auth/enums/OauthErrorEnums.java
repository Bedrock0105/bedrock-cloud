package org.bedrock.auth.enums;

import lombok.Getter;
import org.bedrock.common.code.support.ResultCode;

@Getter
public enum OauthErrorEnums implements ResultCode {

    USER_NOT_DEPT(10011, "用户未分配部门"),
    USER_NOT_ROLE(10012, "用户未分配角色"),
    TENANT_INFORMATION_IS_EMPTY(10013, "租户信息为空"),
    TENANT_IS_DISABLED(10014, "租户已禁用"),
    TENANT_IS_EXPIRED(10015, "租户已过期"),
    ;

    private final int code;

    private final String message;

    OauthErrorEnums(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
