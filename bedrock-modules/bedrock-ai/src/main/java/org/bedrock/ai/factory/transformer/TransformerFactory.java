package org.bedrock.ai.factory.transformer;

import lombok.extern.slf4j.Slf4j;
import org.bedrock.ai.enums.DocumentSliceModeEnum;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * 文档分片转换工厂。
 * <p>
 * 统一管理知识库文档的分片策略，内部按 {@link DocumentSliceModeEnum} 路由到对应 {@link TransformerBuilder} 实现，
 * 将原始文档列表转换为适合向量化的分块文档。
 * </p>
 *
 * @see TransformerBuilder
 * @see DocumentSliceModeEnum
 */
@Slf4j
@Component
public class TransformerFactory {

    /**
     * 分片构造器映射。
     * key: 分片模式枚举；value: 对应策略的构造器实现
     */
    private final Map<DocumentSliceModeEnum, TransformerBuilder> transformerBuilderMap = new EnumMap<>(DocumentSliceModeEnum.class);

    /**
     * 按指定分片模式转换文档（无额外参数）。
     *
     * @param documents             原始文档列表
     * @param documentSliceModeEnum 分片模式
     * @return 分片后的文档列表
     */
    public List<Document> transformer(List<Document> documents, DocumentSliceModeEnum documentSliceModeEnum) {
        return transformer(documents, documentSliceModeEnum, Map.of());
    }

    /**
     * 按指定分片模式转换文档。
     *
     * @param documents             原始文档列表
     * @param documentSliceModeEnum 分片模式
     * @param params                传递给具体分片器的扩展参数（如块大小、分隔符保留策略等）
     * @return 分片后的文档列表
     */
    public List<Document> transformer(List<Document> documents, DocumentSliceModeEnum documentSliceModeEnum, Map<String, Object> params) {
        TransformerBuilder transformerBuilder = transformerBuilderMap.get(documentSliceModeEnum);
        if (transformerBuilder == null) {
            throw new UnsupportedOperationException("分片模式[" + documentSliceModeEnum + "]暂未实现");
        }
        return transformerBuilder.transformer(documents, params);
    }

    /**
     * 注入所有 {@link TransformerBuilder} 实现，并按分片模式建立映射。
     *
     * @param list Spring 容器中所有分片构造器 Bean
     */
    public TransformerFactory(List<TransformerBuilder> list) {
        for (TransformerBuilder transformerBuilder : list) {
            transformerBuilderMap.put(transformerBuilder.getDocumentSliceMode(), transformerBuilder);
        }
    }
}
