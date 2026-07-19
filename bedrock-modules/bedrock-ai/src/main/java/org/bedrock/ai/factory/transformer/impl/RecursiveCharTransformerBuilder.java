package org.bedrock.ai.factory.transformer.impl;

import org.bedrock.ai.enums.DocumentSliceModeEnum;
import org.bedrock.ai.factory.transformer.TransformerBuilder;
import org.bedrock.ai.factory.transformer.support.RecursiveCharacterTextSplitter;
import org.bedrock.ai.factory.transformer.support.TextChunkParamSupport;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 递归字符分片构造器。
 * <p>
 * 对应 {@link DocumentSliceModeEnum#RECURSIVE_CHAR} 模式。
 * 默认按 {@code 段落 → 换行 → 标点 → 空格} 优先级递归降级切分；
 * 传入 {@link TextChunkParamSupport#PARAGRAPH_ONLY}{@code =true} 时，仅按段落/句号边界切分后合并。
 * </p>
 * <p>
 * 参数映射关系（通过 {@code params} 传入）：
 * <ul>
 *   <li>{@link TextChunkParamSupport#CHUNK_SIZE} → 每个分段最大字符数（默认 1000）</li>
 *   <li>{@link TextChunkParamSupport#CHUNK_OVERLAP} → 相邻分段重叠字符数（默认 200）</li>
 *   <li>{@link TextChunkParamSupport#PARAGRAPH_ONLY} → 是否仅段落分片（默认 false）</li>
 * </ul>
 * </p>
 */
@Component
public class RecursiveCharTransformerBuilder implements TransformerBuilder {

    private static final int DEFAULT_CHUNK_SIZE = 1000;

    private static final int DEFAULT_CHUNK_OVERLAP = 200;

    @Override
    public List<Document> transformer(List<Document> documents, Map<String, Object> params) {
        int chunkSize = TextChunkParamSupport.chunkSize(params, DEFAULT_CHUNK_SIZE);
        int chunkOverlap = TextChunkParamSupport.chunkOverlap(params, DEFAULT_CHUNK_OVERLAP);
        RecursiveCharacterTextSplitter splitter = TextChunkParamSupport.paragraphOnly(params)
                ? RecursiveCharacterTextSplitter.paragraph(chunkSize, chunkOverlap)
                : RecursiveCharacterTextSplitter.recursive(chunkSize, chunkOverlap);
        return splitter.apply(documents);
    }

    @Override
    public DocumentSliceModeEnum getDocumentSliceMode() {
        return DocumentSliceModeEnum.RECURSIVE_CHAR;
    }
}
