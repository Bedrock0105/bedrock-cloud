package org.bedrock.ai.utils;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.EncodingType;
import com.knuddels.jtokkit.api.IntArrayList;
import org.springframework.util.StringUtils;

/**
 * AI Token 计数工具类。
 * <p>
 * 基于 jtokkit 对文本进行本地 Token 估算，默认使用 {@link EncodingType#CL100K_BASE}，
 * 与 Spring AI {@link org.springframework.ai.transformer.splitter.TokenTextSplitter} 及
 * GPT-3.5 / GPT-4 系列模型编码保持一致，适用于知识库分片复盘、上下文长度校验等场景。
 * </p>
 *
 * @see org.bedrock.common.ai.advisor.TokenUsageStatisticsAdvisor
 */
public final class AiTokenUtil {

    /**
     * jtokkit 编码注册表（懒加载）。
     * <p>可在 Advisor 等场景复用，避免重复创建注册表实例。</p>
     */
    public static final EncodingRegistry ENCODING_REGISTRY = Encodings.newLazyEncodingRegistry();

    /**
     * 默认编码类型，与 {@link org.springframework.ai.transformer.splitter.TokenTextSplitter} 一致。
     */
    public static final EncodingType DEFAULT_ENCODING_TYPE = EncodingType.CL100K_BASE;

    private AiTokenUtil() {
    }

    /**
     * 获取默认编码实例（{@link #DEFAULT_ENCODING_TYPE}）。
     */
    public static Encoding getEncoding() {
        return getEncoding(DEFAULT_ENCODING_TYPE);
    }

    /**
     * 获取指定类型的编码实例。
     *
     * @param encodingType 编码类型
     * @return 编码实例
     */
    public static Encoding getEncoding(EncodingType encodingType) {
        return ENCODING_REGISTRY.getEncoding(encodingType);
    }

    /**
     * 统计文本 Token 数量（默认 {@link #DEFAULT_ENCODING_TYPE}）。
     * <p>空文本或 {@code null} 返回 {@code 0}。</p>
     *
     * @param text 待统计文本
     * @return Token 数量
     */
    public static int getTokenCount(String text) {
        return getTokenCount(text, DEFAULT_ENCODING_TYPE);
    }

    /**
     * 按指定编码统计文本 Token 数量。
     *
     * @param text         待统计文本
     * @param encodingType 编码类型
     * @return Token 数量
     */
    public static int getTokenCount(String text, EncodingType encodingType) {
        if (!StringUtils.hasText(text)) {
            return 0;
        }
        return getEncoding(encodingType).countTokens(text);
    }

    /**
     * 批量统计多段文本的 Token 总数（默认 {@link #DEFAULT_ENCODING_TYPE}）。
     *
     * @param texts 文本列表
     * @return Token 总数
     */
    public static int getTokenCount(Iterable<String> texts) {
        return getTokenCount(texts, DEFAULT_ENCODING_TYPE);
    }

    /**
     * 按指定编码批量统计多段文本的 Token 总数。
     *
     * @param texts        文本列表
     * @param encodingType 编码类型
     * @return Token 总数
     */
    public static int getTokenCount(Iterable<String> texts, EncodingType encodingType) {
        if (texts == null) {
            return 0;
        }
        int total = 0;
        for (String text : texts) {
            total += getTokenCount(text, encodingType);
        }
        return total;
    }

    /**
     * 判断文本 Token 数是否超过上限（默认 {@link #DEFAULT_ENCODING_TYPE}）。
     *
     * @param text      待检查文本
     * @param maxTokens Token 上限
     * @return {@code true} 表示超过上限
     */
    public static boolean exceedsTokenLimit(String text, int maxTokens) {
        return exceedsTokenLimit(text, maxTokens, DEFAULT_ENCODING_TYPE);
    }

    /**
     * 按指定编码判断文本 Token 数是否超过上限。
     *
     * @param text         待检查文本
     * @param maxTokens    Token 上限
     * @param encodingType 编码类型
     * @return {@code true} 表示超过上限
     */
    public static boolean exceedsTokenLimit(String text, int maxTokens, EncodingType encodingType) {
        if (maxTokens <= 0) {
            return StringUtils.hasText(text);
        }
        return getTokenCount(text, encodingType) > maxTokens;
    }

    /**
     * 按 Token 上限截断文本，保留前缀部分（默认 {@link #DEFAULT_ENCODING_TYPE}）。
     * <p>未超限时返回原文；空文本或 {@code maxTokens <= 0} 时返回空字符串。</p>
     *
     * @param text      待截断文本
     * @param maxTokens Token 上限
     * @return 截断后的文本
     */
    public static String truncateToTokenLimit(String text, int maxTokens) {
        return truncateToTokenLimit(text, maxTokens, DEFAULT_ENCODING_TYPE);
    }

    /**
     * 按指定编码和 Token 上限截断文本，保留前缀部分。
     *
     * @param text         待截断文本
     * @param maxTokens    Token 上限
     * @param encodingType 编码类型
     * @return 截断后的文本
     */
    public static String truncateToTokenLimit(String text, int maxTokens, EncodingType encodingType) {
        if (!StringUtils.hasText(text) || maxTokens <= 0) {
            return "";
        }
        Encoding encoding = getEncoding(encodingType);
        IntArrayList tokens = encoding.encode(text);
        if (tokens.size() <= maxTokens) {
            return text;
        }
        IntArrayList truncated = new IntArrayList(maxTokens);
        for (int i = 0; i < maxTokens; i++) {
            truncated.add(tokens.get(i));
        }
        return encoding.decode(truncated);
    }
}
