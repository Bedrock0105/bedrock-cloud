package org.bedrock.system.enums;

import lombok.Getter;

/**
 * 常用字典枚举
 */
@Getter
public enum DictEnum {
    SEX("sex", "性别"),
    STATUS("status", "状态"),
    ;

    /**
     * 字典编码
     */
    private final String dictCode;

    /**
     * 字典名称
     */
    private final String dictName;

    DictEnum(String dictCode, String dictName) {
        this.dictCode = dictCode;
        this.dictName = dictName;
    }
}
