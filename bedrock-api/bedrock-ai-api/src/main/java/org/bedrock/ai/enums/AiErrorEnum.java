package org.bedrock.ai.enums;

import lombok.Getter;

/**
 * AI 模块业务错误码
 */
@Getter
public enum AiErrorEnum {

    /** API Key 名称已存在 */
    KEY_NAME_ALREADY_EXISTS(13001, "API Key 名称已存在"),
    /** 同平台下模型标识已存在 */
    MODEL_ALREADY_EXISTS(13002, "模型标识已存在"),
    /** 角色名称已存在 */
    ROLE_NAME_ALREADY_EXISTS(13003, "角色名称已存在"),
    /** API Key 被模型引用，无法删除 */
    API_KEY_IN_USE(13004, "API Key 已被模型引用，无法删除"),
    /** 关联的 API Key 配置不存在 */
    API_KEY_NOT_FOUND(13005, "API Key 不存在"),
    /** AI 模型不存在 */
    MODEL_NOT_FOUND(13006, "AI 模型不存在"),
    /** AI 模型已禁用 */
    MODEL_DISABLED(13007, "AI 模型已禁用"),
    /** API Key 已禁用 */
    API_KEY_DISABLED(13008, "API Key 已禁用"),
    /** 聊天会话不存在 */
    CHAT_RECORD_NOT_FOUND(13009, "聊天会话不存在"),
    /** AI 角色不存在 */
    ROLE_NOT_FOUND(13010, "AI 角色不存在"),
    /** AI 角色已禁用 */
    ROLE_DISABLED(13011, "AI 角色已禁用"),
    /** 暂无可用模型 */
    NO_AVAILABLE_MODEL(13012, "暂无可用模型"),
    /** 聊天内容不能为空 */
    CHAT_CONTENT_EMPTY(13013, "聊天内容不能为空"),
    /** 向量库名称已存在 */
    VECTOR_DB_NAME_ALREADY_EXISTS(13014, "向量库名称已存在"),
    /** 向量模型不存在 */
    EMBEDDING_MODEL_NOT_FOUND(13015, "向量模型不存在"),
    /** 关联模型不是向量类型 */
    EMBEDDING_MODEL_TYPE_INVALID(13016, "关联模型不是向量类型"),
    /** 知识库名称已存在 */
    KNOWLEDGE_NAME_ALREADY_EXISTS(13017, "知识库名称已存在"),
    /** 知识库不存在 */
    KNOWLEDGE_NOT_FOUND(13018, "知识库不存在"),
    /** 向量库不存在 */
    VECTOR_DB_NOT_FOUND(13019, "向量库不存在"),
    /** 向量库已禁用 */
    VECTOR_DB_DISABLED(13020, "向量库已禁用"),
    /** 知识库文档不存在 */
    KNOWLEDGE_DOC_NOT_FOUND(13021, "知识库文档不存在"),
    /** 同知识库下文档标题已存在 */
    KNOWLEDGE_DOC_TITLE_ALREADY_EXISTS(13022, "同知识库下文档标题已存在"),
    /** 知识库分片不存在 */
    KNOWLEDGE_CHUNK_NOT_FOUND(13023, "知识库分片不存在"),
    /** 同文档下分片序号已存在 */
    KNOWLEDGE_CHUNK_NO_ALREADY_EXISTS(13024, "同文档下分片序号已存在"),
    /** 文档与知识库不匹配 */
    KNOWLEDGE_DOC_NOT_MATCH(13025, "文档与知识库不匹配"),
    /** MCP 名称已存在 */
    MCP_NAME_ALREADY_EXISTS(13026, "MCP 名称已存在"),
    /** MCP 不存在 */
    MCP_NOT_FOUND(13027, "MCP 配置不存在"),
    /** MCP 名称创建后不可修改 */
    MCP_NAME_IMMUTABLE(13028, "MCP 名称创建后不可修改"),
    /** 关联模型不是图片类型 */
    IMAGE_MODEL_TYPE_INVALID(13029, "关联模型不是图片类型"),
    /** 图片提示词不能为空 */
    IMAGE_CONTENT_EMPTY(13030, "图片提示词不能为空"),
    ;

    private final Integer code;

    private final String message;

    AiErrorEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
