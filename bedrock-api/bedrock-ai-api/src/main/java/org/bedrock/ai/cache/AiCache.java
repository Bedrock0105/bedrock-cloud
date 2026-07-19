package org.bedrock.ai.cache;

/**
 * AI 模块缓存 Key 常量
 */
public final class AiCache {

    private AiCache() {
    }

    /**
     * 模型校验详情 key
     */
    public static final String MODEL_CHECK_ID = "model:check:id:";

    /**
     * 向量数据库校验详情 key
     */
    public static final String VECTOR_DB_CHECK_ID = "vector:db:check:id:";

    /**
     * 知识库详情 key
     */
    public static final String KNOWLEDGE_DETAIL_ID = "knowledge:detail:id:";

    /**
     * 会话详情 key
     */
    public static final String CHAT_RECORD_DETAIL_ID = "chat:record:detail:id:";

}
