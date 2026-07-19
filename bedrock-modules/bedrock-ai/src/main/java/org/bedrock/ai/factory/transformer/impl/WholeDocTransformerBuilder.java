package org.bedrock.ai.factory.transformer.impl;

import org.bedrock.ai.enums.DocumentSliceModeEnum;
import org.bedrock.ai.factory.transformer.TransformerBuilder;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 完整文档分片构造器。
 * <p>
 * 对应 {@link DocumentSliceModeEnum#WHOLE_DOC} 模式，不做任何切分，整篇文档作为单个 chunk。
 * 适合短文档、FAQ、制度条文等无需拆分的场景。
 * </p>
 */
@Component
public class WholeDocTransformerBuilder implements TransformerBuilder {

    @Override
    public List<Document> transformer(List<Document> documents, Map<String, Object> params) {
        return documents;
    }

    @Override
    public DocumentSliceModeEnum getDocumentSliceMode() {
        return DocumentSliceModeEnum.WHOLE_DOC;
    }
}
