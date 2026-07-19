//package org.bedrock.ai.factory.documet.impl;
//
//import org.bedrock.ai.factory.documet.DocumentReaderBuilder;
//import org.bedrock.common.code.util.StringUtil;
//import org.springframework.ai.document.Document;
//import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
//import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
//import org.springframework.core.io.Resource;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * Markdown 文档读取构造器。
// * <p>
// * 匹配 {@code .md} 后缀文件，基于 Spring AI {@link MarkdownDocumentReader} 解析。
// * 默认按水平分割线（{@code ---}）切分为多个文档，不包含代码块与引用块内容。
// * </p>
// */
//@Component
//public class MarkdownDocumentReaderBuilder implements DocumentReaderBuilder {
//
//    @Override
//    public boolean canRead(Resource resource, Map<String, Object> params) {
//        String filename = resource.getFilename();
//        return StringUtil.isNotBlank(filename) && filename.endsWith(".md");
//    }
//
//    @Override
//    public List<Document> read(Resource resource, Map<String, Object> params) {
//        MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
//                .withHorizontalRuleCreateDocument(true)
//                .withIncludeCodeBlock(false)
//                .withIncludeBlockquote(false)
//                .build();
//        MarkdownDocumentReader reader = new MarkdownDocumentReader(resource, config);
//        return reader.get();
//    }
//}
