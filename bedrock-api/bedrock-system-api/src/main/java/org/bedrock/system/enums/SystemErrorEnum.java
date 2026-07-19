package org.bedrock.system.enums;

import lombok.Getter;
import org.bedrock.common.code.support.ResultCode;

@Getter
public enum SystemErrorEnum implements ResultCode {
    NOT_AUTH_CREATE_SUPER_ADMIN(11001, "无权限创建超管角色！"),
    NOT_AUTH_CREATE_ADMIN(11002, "无权创建管理员角色!"),
    THE_NAME_ALREADY_EXISTS(11003, "当前角色名称或者别名已存在!"),
    MENU_CODE_EXISTS(11004, "当前菜单编码已存在!"),
    MENU_HAS_CHILD(11005, "当前菜单下存在子菜单，请先删除子菜单!"),
    DICT_HAS_CHILD(11006, "当前字典下存在子字典"),
    DICT_CODE_EXISTS(11007, "当前字典编码已存在!"),
    PARAM_CONFIG_KEY_EXISTS(11008, "当前参数配置键已存在!"),
    PACKAGE_HAS_TENANT(11009, "当前套餐已被使用!"),
    DEPT_CODE_EXISTS(11010, "当前部门编号已存在!"),
    ;

    private final int code;

    private final String message;

    SystemErrorEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
