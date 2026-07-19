package org.bedrock.ai.factory.documet;

import org.springframework.ai.document.Document;
import org.springframework.core.Ordered;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Map;

/**
 * 文档读取构造器。
 * <p>
 * 每种文件类型（Markdown、JSON、纯文本等）独立实现，负责判断资源是否可读取并解析为 {@link Document} 列表。
 * 通过 {@link org.springframework.stereotype.Component} 注册后，
 * 由 {@link DocumentReaderFactory} 在启动时自动注入并按 {@link #getOrder()} 排序匹配。
 * </p>
 */
public interface DocumentReaderBuilder extends Ordered {

    /**
     * 判断当前构造器是否支持读取指定资源。
     *
     * @param resource 待读取的资源
     * @param params   扩展参数
     * @return {@code true} 表示支持读取
     */
    boolean canRead(Resource resource, Map<String, Object> params);

    /**
     * 读取资源并解析为文档列表。
     *
     * @param resource 待读取的资源
     * @param params   扩展参数
     * @return 解析后的文档列表
     */
    List<Document> read(Resource resource, Map<String, Object> params);

    /**
     * 获取匹配优先级，数值越小越优先匹配。
     * <p>专用格式读取器应返回较小值；通用兜底读取器（如 Tika）应返回较大值。</p>
     *
     * @return 优先级序号
     */
    default int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 10000;
    }
}
