package org.bedrock.ai.factory.documet.impl;

import org.bedrock.ai.factory.documet.DocumentReaderBuilder;
import org.bedrock.common.code.util.StringUtil;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.JsonReader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * JSON 文档读取构造器。
 * <p>
 * 匹配 {@code .json} 后缀文件，基于 Spring AI {@link JsonReader} 解析，
 * 将 JSON 数组或对象中的每个元素转换为独立的 {@link Document}。
 * </p>
 */
@Component
public class JsonReaderDocumentReaderBuilder implements DocumentReaderBuilder {

    @Override
    public boolean canRead(Resource resource, Map<String, Object> params) {
        String filename = resource.getFilename();
        return StringUtil.isNotBlank(filename) && filename.endsWith(".json");
    }

    @Override
    public List<Document> read(Resource resource, Map<String, Object> params) {
        JsonReader jsonReader = new JsonReader(resource);
        return jsonReader.get();
    }
}
