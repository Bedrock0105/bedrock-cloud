package org.bedrock.ai.factory.transformer;

import org.bedrock.ai.enums.DocumentSliceModeEnum;
import org.springframework.ai.document.Document;

import java.util.List;
import java.util.Map;

/**
 * 文档分片构造器。
 * <p>
 * 每种分片策略独立实现，负责将原始文档列表转换为分块后的 {@link Document} 列表。
 * 通过 {@link org.springframework.stereotype.Component} 注册后，
 * 由 {@link TransformerFactory} 在启动时自动注入并按 {@link DocumentSliceModeEnum} 路由。
 * </p>
 * <p>
 * 内置实现：
 * <ul>
 *   <li>{@link org.bedrock.ai.factory.transformer.impl.TokenTextTransformerBuilder} — 固定 Token 长度</li>
 *   <li>{@link org.bedrock.ai.factory.transformer.impl.SemanticTextTransformerBuilder} — 本地文本相似度分片</li>
 *   <li>{@link org.bedrock.ai.factory.transformer.impl.HierarchyTextTransformerBuilder} — Markdown 标题层级</li>
 *   <li>{@link org.bedrock.ai.factory.transformer.impl.DelimiterTransformerBuilder} — 自定义分隔符分片</li>
 *   <li>{@link org.bedrock.ai.factory.transformer.impl.RecursiveCharTransformerBuilder} — 递归字符/段落分片（paragraphOnly 参数切换）</li>
 *   <li>{@link org.bedrock.ai.factory.transformer.impl.WholeDocTransformerBuilder} — 整篇不分片</li>
 * </ul>
 * 通用参数 key 见 {@link org.bedrock.ai.factory.transformer.support.TextChunkParamSupport}。
 * </p>
 */
public interface TransformerBuilder {

    /**
     * 对文档列表执行分片转换。
     *
     * @param documents 原始文档列表
     * @param params    分片参数（块大小、最小字符数等，具体 key 由各实现定义）
     * @return 分片后的文档列表
     */
    List<Document> transformer(List<Document> documents, Map<String, Object> params);

    /**
     * 获取当前构造器对应的分片模式枚举。
     *
     * @return 分片模式标识
     */
    DocumentSliceModeEnum getDocumentSliceMode();

}
