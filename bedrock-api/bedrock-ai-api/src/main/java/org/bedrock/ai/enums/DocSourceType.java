package org.bedrock.ai.enums;

import lombok.Getter;

@Getter
public enum DocSourceType {
    /**
     * 上传文件
     */
    UPLOAD_FILE("UPLOAD_FILE", "本地上传"),
    /**
     * 手动输入
     */
    MANUAL_TEXT("MANUAL_TEXT", "手动录入");

    /**
     * 编码
     */
    private final String code;

    /**
     * 描述
     */
    private final String description;

    DocSourceType(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
