package org.bedrock.ai.factory.documet.impl;

import org.bedrock.ai.factory.documet.DocumentReaderBuilder;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.Ordered;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 基于 Apache Tika 的通用文档读取构造器。
 * <p>
 * 作为兜底读取器，{@link #canRead(Resource, Map)} 始终返回 {@code true}，
 * 可解析 PDF、Word、Excel 等 Tika 支持的多种格式。
 * 优先级最低（{@link Ordered#LOWEST_PRECEDENCE}），仅在专用读取器未命中时生效。
 * </p>
 */
@Component
public class TikaDocumentReaderBuilder implements DocumentReaderBuilder {

    @Override
    public boolean canRead(Resource resource, Map<String, Object> params) {
       return true;
    }

    @Override
    public List<Document> read(Resource resource, Map<String, Object> params) {
        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(resource);
        return tikaDocumentReader.read();
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
