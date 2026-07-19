package org.bedrock.ai.enums;

import lombok.Getter;

/**
 * AI 会话类型枚举
 */
@Getter
public enum AiChatTypeEnum {
    CHAT("普通对话"),
    IMAGE("图片生成"),
    MINDMAP("思维导图"),
    ARTICLE("文章写作"),
    ;

    private final String value;

    AiChatTypeEnum(String value) {
        this.value = value;
    }
}
