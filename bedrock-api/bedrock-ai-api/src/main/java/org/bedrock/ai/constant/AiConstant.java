package org.bedrock.ai.constant;

/**
 * AI常量
 */
public interface AiConstant {

    /**
     * Advisor 上下文存储模型完整信息的 Key
     * 对应实体/VO：{@link org.bedrock.ai.vo.AiModelCheckVO}
     */
    String CTX_MODEL_DETAIL = "chat_ai_model_detail";

    /**
     * Advisor 上下文存储对话记录信息的 Key
     * 对应实体：{@link org.bedrock.ai.vo.AiChatRecordDetailVO}
     */
    String CTX_CHAT_RECORD = "chat_ai_record_detail";

    /**
     * Advisor 上下文存储用户信息的 Key
     * 对应实体：{@link org.bedrock.common.auth.entity.AuthUser}
     */
    String CTX_USER_INFO = "chat_ai_user_info";

    /**
     * Advisor 上下文存储用户附件信息的 Key
     * 对应实体：{@link java.util.List<String>}
     */
    String CTX_USER_ATTACHMENT = "chat_ai_user_attachment";

    /**
     * 向量库元数据Key：知识库ID，对应 bedrock_ai_knowledge.id
     */
    String VECTOR_META_KEY_KNOWLEDGE_ID = "knowledgeId";

    /**
     * 向量库元数据Key：文档ID，对应 bedrock_ai_knowledge_doc.id
     */
    String VECTOR_META_KEY_DOC_ID = "docId";

    /**
     * 向量库元数据Key：分片Chunk主键ID，对应 bedrock_ai_knowledge_chunk.id
     */
    String VECTOR_META_KEY_CHUNK_ID = "chunkId";

    /**
     * 分片向量化状态：待向量化
     */
    Integer EMBED_STATUS_PENDING = 0;

    /**
     * 分片向量化状态：入库成功
     */
    Integer EMBED_STATUS_SUCCESS = 1;

    /**
     * 分片向量化状态：向量写入失败
     */
    Integer EMBED_STATUS_FAILED = 2;
}
