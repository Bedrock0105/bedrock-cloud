package org.bedrock.ai.factory.transformer.impl;

import org.bedrock.ai.enums.DocumentSliceModeEnum;
import org.bedrock.ai.factory.transformer.TransformerBuilder;
import org.bedrock.ai.factory.transformer.support.TextChunkParamSupport;
import org.bedrock.common.code.util.NumberUtil;
import org.bedrock.common.code.util.StringUtil;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 基于 Token 的固定长度分片构造器。
 * <p>
 * 对应 {@link DocumentSliceModeEnum#FIXED_LENGTH} 模式，
 * 使用 Spring AI {@link TokenTextSplitter} 按 Token 数量将文档切分为多个块，
 * 超长时优先在句号等标点处断开。
 * </p>
 * <p>
 * 参数映射关系（通过 {@code params} 传入）：
 * <ul>
 *   <li>{@link TextChunkParamSupport#CHUNK_SIZE} → 每个分段最大 Token 数（默认 800）</li>
 *   <li>{@code minChunkSizeChars} → 每个分段最小字符数（默认 350）</li>
 *   <li>{@code minChunkLengthToEmbed} → 可嵌入分块的最小长度（默认 5）</li>
 *   <li>{@code maxNumChunks} → 单文档最大分块数（默认 10000）</li>
 *   <li>{@code keepSeparator} → 是否保留分隔符（默认 true）</li>
 * </ul>
 * </p>
 */
@Component
public class TokenTextTransformerBuilder implements TransformerBuilder {

    private static final int DEFAULT_CHUNK_SIZE = 800;

    private static final String MIN_CHUNK_SIZE_CHARS = "minChunkSizeChars";

    private static final String MIN_CHUNK_LENGTH_TO_EMBED = "minChunkLengthToEmbed";

    private static final String MAX_NUM_CHUNKS = "maxNumChunks";

    private static final String KEEP_SEPARATOR = "keepSeparator";

    @Override
    public List<Document> transformer(List<Document> documents, Map<String, Object> params) {
        int chunkSize = TextChunkParamSupport.chunkSize(params, DEFAULT_CHUNK_SIZE);
        if (params.containsKey("defaultChunkSizeKey")) {
            chunkSize = NumberUtil.toInt(params.get("defaultChunkSizeKey"), chunkSize);
        }
        TokenTextSplitter splitter = TokenTextSplitter.builder()
                .withChunkSize(chunkSize)
                .withMinChunkSizeChars(NumberUtil.toInt(params.get(MIN_CHUNK_SIZE_CHARS), 350))
                .withMinChunkLengthToEmbed(NumberUtil.toInt(params.get(MIN_CHUNK_LENGTH_TO_EMBED), 5))
                .withMaxNumChunks(NumberUtil.toInt(params.get(MAX_NUM_CHUNKS), 10000))
                .withKeepSeparator(Boolean.parseBoolean(StringUtil.toStr(params.get(KEEP_SEPARATOR), "true")))
                .build();
        return splitter.apply(documents);
    }

    @Override
    public DocumentSliceModeEnum getDocumentSliceMode() {
        return DocumentSliceModeEnum.FIXED_LENGTH;
    }
}
