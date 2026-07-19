package org.bedrock.ai.factory.documet;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bedrock.common.code.util.ResourceUtil;
import org.springframework.ai.document.Document;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 文档读取工厂。
 * <p>
 * 统一管理各类文档的读取入口，内部按 {@link DocumentReaderBuilder#getOrder()} 排序后，
 * 依次匹配 {@link DocumentReaderBuilder#canRead(Resource, Map)}，由首个命中的构造器完成解析。
 * 所有 {@link DocumentReaderBuilder} 实现通过 Spring 自动注入装配。
 * </p>
 *
 * @see DocumentReaderBuilder
 */
@Slf4j
@Component
public class DocumentReaderFactory {

    /**
     * 已注册的文档读取构造器列表，按优先级升序排列
     */
    private final List<DocumentReaderBuilder> documentReaderBuilders;

    /**
     * 根据文件路径读取文档（无额外参数）。
     *
     * @param filePath 文件路径，支持 classpath、file 等 Spring Resource 协议
     * @return 解析后的文档列表
     */
    public List<Document> readDocuments(String filePath) {
        return readDocuments(filePath, Map.of());
    }

    /**
     * 根据文件路径读取文档。
     *
     * @param filePath 文件路径
     * @param params   传递给具体读取器的扩展参数
     * @return 解析后的文档列表
     */
    public List<Document> readDocuments(String filePath, Map<String, Object> params) {
        return readDocuments(getResource(filePath), params);
    }

    /**
     * 根据 Spring Resource 读取文档（无额外参数）。
     *
     * @param resource 待读取的资源
     * @return 解析后的文档列表
     */
    public List<Document> readDocuments(Resource resource) {
        return readDocuments(resource, Map.of());
    }

    /**
     * 根据 Spring Resource 读取文档。
     * <p>遍历已注册的构造器，返回首个 {@code canRead} 为 {@code true} 的读取结果。</p>
     *
     * @param resource 待读取的资源
     * @param params   传递给具体读取器的扩展参数
     * @return 解析后的文档列表
     * @throws IllegalArgumentException 无匹配的读取器时抛出
     */
    public List<Document> readDocuments(Resource resource, Map<String, Object> params) {
        return documentReaderBuilders.stream()
                .filter(builder -> builder.canRead(resource, params))
                .findFirst()
                .map(builder -> builder.read(resource, params))
                .orElseGet(List::of);
    }

    /**
     * 将文件路径解析为 Spring {@link Resource}。
     *
     * @param filePath 文件路径
     * @return 对应的 Resource 实例
     */
    @SneakyThrows
    public Resource getResource(String filePath) {
        return ResourceUtil.getResource(filePath);
    }

    /**
     * 注入所有 {@link DocumentReaderBuilder} 实现，并按优先级排序。
     *
     * @param list Spring 容器中所有文档读取构造器 Bean
     */
    public DocumentReaderFactory(List<DocumentReaderBuilder> list) {
        this.documentReaderBuilders = list;
        this.documentReaderBuilders.sort(Comparator.comparingInt(DocumentReaderBuilder::getOrder));
    }
}
