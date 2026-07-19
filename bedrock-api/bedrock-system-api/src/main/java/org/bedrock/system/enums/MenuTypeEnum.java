package org.bedrock.system.enums;

import lombok.Getter;

@Getter
public enum MenuTypeEnum {
    /**
     * 目录（1）
     */
    DIRECTORY(1, "目录"),

    /**
     * 菜单（2）
     */
    MENU(2, "菜单"),

    /**
     * 按钮（3）
     */
    BUTTON(3, "按钮"),

    /**
     * 外部链接（4）
     */
    EXTERNAL_LINK(4, "外部链接");

    /**
     * 类型编码
     */
    private final Integer code;

    /**
     * 类型描述
     */
    private final String description;

    // 构造方法
    MenuTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
    /**
     * 根据编码获取对应的枚举
     *
     * @param code 类型编码
     * @return 对应的枚举，若未匹配则返回 null
     */
    public static MenuTypeEnum getByCode(Integer code) {
        for (MenuTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
