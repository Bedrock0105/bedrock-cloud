package org.bedrock.ai.factory.transformer.support;

import org.bedrock.common.code.util.NumberUtil;
import org.bedrock.common.code.util.StringUtil;
import org.bedrock.common.log.exception.ServiceException;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 文档分片通用参数读取工具。
 */
public final class TextChunkParamSupport {

    /**
     * 每个分段的最大长度（Token 或字符，由具体分片器解释）
     */
    public static final String CHUNK_SIZE = "chunkSize";

    /**
     * 相邻分段重叠长度（字符）
     */
    public static final String CHUNK_OVERLAP = "chunkOverlap";

    /**
     * 自定义固定分隔符
     */
    public static final String DELIMITER = "delimiter";

    /**
     * 是否仅按段落/句号边界切分（用于 RECURSIVE_CHAR 模式）
     */
    public static final String PARAGRAPH_ONLY = "paragraphOnly";

    /**
     * 语义分片相似度阈值（0~1，越低越容易切分）
     */
    public static final String SIMILARITY_THRESHOLD = "similarityThreshold";

    private TextChunkParamSupport() {
    }

    public static int chunkSize(Map<String, Object> params, int defaultValue) {
        return NumberUtil.toInt(params.get(CHUNK_SIZE), defaultValue);
    }

    public static int chunkOverlap(Map<String, Object> params, int defaultValue) {
        return NumberUtil.toInt(params.get(CHUNK_OVERLAP), defaultValue);
    }

    public static String delimiter(Map<String, Object> params) {
        return StringUtil.toStr(params.get(DELIMITER), null);
    }

    public static boolean paragraphOnly(Map<String, Object> params) {
        return Boolean.parseBoolean(StringUtil.toStr(params.get(PARAGRAPH_ONLY), "false"));
    }

    public static String requiredDelimiter(Map<String, Object> params) {
        String delimiter = delimiter(params);
        if (delimiter == null) {
            throw new ServiceException("自定义分隔符分片需要传入 delimiter 参数");
        }
        return delimiter;
    }

    public static double similarityThreshold(Map<String, Object> params, double defaultValue) {
        Object value = params.get(SIMILARITY_THRESHOLD);
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        try {
            return Double.parseDouble(value.toString());
        } catch (Exception ex) {
            return defaultValue;
        }
    }
}
