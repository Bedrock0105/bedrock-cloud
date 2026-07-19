package org.bedrock.resource.enums;

import lombok.Getter;

@Getter
public enum ResourceErrorEnum {
    THE_CODE_ALREADY_EXISTS(12001, "编码已存在");

    private final Integer code;

    private final String message;

    ResourceErrorEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
