package org.bedrock.ai.factory.documet.impl;

import org.bedrock.ai.factory.documet.DocumentReaderBuilder;
import org.bedrock.common.code.util.StringUtil;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 纯文本文档读取构造器。
 * <p>
 * 匹配 {@code .txt} 后缀文件，基于 Spring AI {@link TextReader} 解析，
 * 将整个文本文件内容作为单个 {@link Document} 返回。
 * </p>
 */
@Component
public class TextReaderDocumentReaderBuilder implements DocumentReaderBuilder {

    @Override
    public boolean canRead(Resource resource, Map<String, Object> params) {
        String filename = resource.getFilename();
        return StringUtil.isNotBlank(filename) && filename.endsWith(".txt");
    }

    @Override
    public List<Document> read(Resource resource, Map<String, Object> params) {
        TextReader textReader = new TextReader(resource);
        return textReader.read();
    }
}
