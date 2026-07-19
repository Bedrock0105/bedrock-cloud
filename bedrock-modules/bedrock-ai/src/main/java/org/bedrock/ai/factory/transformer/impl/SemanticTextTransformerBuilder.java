package org.bedrock.ai.factory.transformer.impl;

import org.bedrock.ai.enums.DocumentSliceModeEnum;
import org.bedrock.ai.factory.transformer.TransformerBuilder;
import org.bedrock.ai.factory.transformer.support.SemanticTextSplitter;
import org.bedrock.ai.factory.transformer.support.TextChunkParamSupport;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 语义智能分片构造器。
 * <p>
 * 对应 {@link DocumentSliceModeEnum#SEMANTIC} 模式，
 * 先按句号/换行切句，再基于本地文本 Jaccard 相似度决定是否合并，语义突变处自动断开。
 * 全程不调用大模型或 Embedding 接口。
 * </p>
 * <p>
 * 参数映射关系（通过 {@code params} 传入）：
 * <ul>
 *   <li>{@link TextChunkParamSupport#CHUNK_SIZE} → 每个分段最大字符数（默认 1000）</li>
 *   <li>{@link TextChunkParamSupport#CHUNK_OVERLAP} → 相邻分段重叠字符数（默认 200）</li>
 *   <li>{@link TextChunkParamSupport#SIMILARITY_THRESHOLD} → 文本相似度阈值（默认 0.5，越低越容易切分）</li>
 * </ul>
 * </p>
 */
@Component
public class SemanticTextTransformerBuilder implements TransformerBuilder {

    private static final int DEFAULT_CHUNK_SIZE = 1000;

    private static final int DEFAULT_CHUNK_OVERLAP = 200;

    private static final double DEFAULT_SIMILARITY_THRESHOLD = 0.5;

    @Override
    public List<Document> transformer(List<Document> documents, Map<String, Object> params) {
        SemanticTextSplitter splitter = new SemanticTextSplitter(
                TextChunkParamSupport.chunkSize(params, DEFAULT_CHUNK_SIZE),
                TextChunkParamSupport.chunkOverlap(params, DEFAULT_CHUNK_OVERLAP),
                TextChunkParamSupport.similarityThreshold(params, DEFAULT_SIMILARITY_THRESHOLD));
        return splitter.apply(documents);
    }

    @Override
    public DocumentSliceModeEnum getDocumentSliceMode() {
        return DocumentSliceModeEnum.SEMANTIC;
    }
}
