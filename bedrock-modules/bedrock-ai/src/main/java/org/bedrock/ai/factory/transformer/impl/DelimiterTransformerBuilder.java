package org.bedrock.ai.factory.transformer.impl;

import org.bedrock.ai.enums.DocumentSliceModeEnum;
import org.bedrock.ai.factory.transformer.TransformerBuilder;
import org.bedrock.ai.factory.transformer.support.DelimiterTextSplitter;
import org.bedrock.ai.factory.transformer.support.TextChunkParamSupport;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 自定义分隔符分片构造器。
 * <p>
 * 对应 {@link DocumentSliceModeEnum#DELIMITER} 模式，
 * 按业务指定的固定分隔符切分后再合并到目标长度，单段超长时降级为段落分片。
 * </p>
 * <p>
 * 参数映射关系（通过 {@code params} 传入）：
 * <ul>
 *   <li>{@link TextChunkParamSupport#DELIMITER} → 自定义分隔符（必填，如 {@code ###}、{@code |}）</li>
 *   <li>{@link TextChunkParamSupport#CHUNK_SIZE} → 每个分段最大字符数（默认 1000）</li>
 *   <li>{@link TextChunkParamSupport#CHUNK_OVERLAP} → 相邻分段重叠字符数（默认 200）</li>
 * </ul>
 * </p>
 */
@Component
public class DelimiterTransformerBuilder implements TransformerBuilder {

    private static final int DEFAULT_CHUNK_SIZE = 1000;

    private static final int DEFAULT_CHUNK_OVERLAP = 200;

    @Override
    public List<Document> transformer(List<Document> documents, Map<String, Object> params) {
        DelimiterTextSplitter splitter = new DelimiterTextSplitter(
                TextChunkParamSupport.requiredDelimiter(params),
                TextChunkParamSupport.chunkSize(params, DEFAULT_CHUNK_SIZE),
                TextChunkParamSupport.chunkOverlap(params, DEFAULT_CHUNK_OVERLAP));
        return splitter.apply(documents);
    }

    @Override
    public DocumentSliceModeEnum getDocumentSliceMode() {
        return DocumentSliceModeEnum.DELIMITER;
    }
}
