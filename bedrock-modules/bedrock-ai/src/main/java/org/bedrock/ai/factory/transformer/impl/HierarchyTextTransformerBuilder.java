package org.bedrock.ai.factory.transformer.impl;

import org.bedrock.ai.enums.DocumentSliceModeEnum;
import org.bedrock.ai.factory.transformer.TransformerBuilder;
import org.bedrock.ai.factory.transformer.support.HierarchyTextSplitter;
import org.bedrock.ai.factory.transformer.support.TextChunkParamSupport;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 标题层级分片构造器。
 * <p>
 * 对应 {@link DocumentSliceModeEnum#HIERARCHY} 模式，
 * 按 Markdown 标题（{@code #} ~ {@code ######}）切分章节，超长章节再按段落递归细分。
 * </p>
 * <p>
 * 参数映射关系（通过 {@code params} 传入）：
 * <ul>
 *   <li>{@link TextChunkParamSupport#CHUNK_SIZE} → 章节二次切分的最大字符数（默认 1200）</li>
 *   <li>{@link TextChunkParamSupport#CHUNK_OVERLAP} → 二次切分重叠字符数（默认 200）</li>
 * </ul>
 * </p>
 */
@Component
public class HierarchyTextTransformerBuilder implements TransformerBuilder {

    private static final int DEFAULT_CHUNK_SIZE = 1200;

    private static final int DEFAULT_CHUNK_OVERLAP = 200;

    @Override
    public List<Document> transformer(List<Document> documents, Map<String, Object> params) {
        HierarchyTextSplitter splitter = new HierarchyTextSplitter(
                TextChunkParamSupport.chunkSize(params, DEFAULT_CHUNK_SIZE),
                TextChunkParamSupport.chunkOverlap(params, DEFAULT_CHUNK_OVERLAP));
        return splitter.apply(documents);
    }

    @Override
    public DocumentSliceModeEnum getDocumentSliceMode() {
        return DocumentSliceModeEnum.HIERARCHY;
    }
}
