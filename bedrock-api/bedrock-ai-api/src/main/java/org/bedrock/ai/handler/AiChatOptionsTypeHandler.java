package org.bedrock.ai.handler;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import org.apache.ibatis.type.MappedTypes;
import org.bedrock.ai.dto.AiChatOptions;

/**
 * {@link AiChatOptions} JSON 字段 TypeHandler，供实体与 Mapper XML 共用。
 */
@MappedTypes(AiChatOptions.class)
public class AiChatOptionsTypeHandler extends JacksonTypeHandler {

    public AiChatOptionsTypeHandler() {
        super(AiChatOptions.class);
    }

}
