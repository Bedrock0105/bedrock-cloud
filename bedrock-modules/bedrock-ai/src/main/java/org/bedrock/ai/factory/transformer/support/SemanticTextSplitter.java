package org.bedrock.ai.factory.transformer.support;

import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 语义分片器：先按句子切分，再基于本地文本 Jaccard 相似度合并/切分（不调用大模型）。
 */
public class SemanticTextSplitter extends TextSplitter {

    private static final Pattern SENTENCE_PATTERN = Pattern.compile("(?<=[。！？.!?\\n])\\s*");

    private static final Pattern WORD_PATTERN = Pattern.compile("[\\p{L}\\p{N}]+");

    private final int chunkSize;

    private final int chunkOverlap;

    private final double similarityThreshold;

    public SemanticTextSplitter(int chunkSize, int chunkOverlap, double similarityThreshold) {
        this.chunkSize = chunkSize;
        this.chunkOverlap = Math.max(0, Math.min(chunkOverlap, chunkSize / 2));
        this.similarityThreshold = similarityThreshold;
    }

    @Override
    protected List<String> splitText(String text) {
        if (!StringUtils.hasText(text)) {
            return List.of();
        }
        List<String> sentences = splitSentences(text);
        if (sentences.size() <= 1) {
            return List.of(text.trim());
        }

        List<String> chunks = new ArrayList<>();
        StringBuilder current = new StringBuilder(sentences.get(0));
        Set<String> currentTokens = tokenize(sentences.get(0));

        for (int i = 1; i < sentences.size(); i++) {
            String sentence = sentences.get(i);
            Set<String> sentenceTokens = tokenize(sentence);
            double similarity = jaccardSimilarity(currentTokens, sentenceTokens);
            String candidate = current + sentence;
            boolean shouldSplit = similarity < similarityThreshold || candidate.length() > chunkSize;
            if (shouldSplit && !current.isEmpty()) {
                chunks.add(current.toString().trim());
                current = new StringBuilder(sentence);
                currentTokens = sentenceTokens;
            }
            else {
                current.append(sentence);
                currentTokens.addAll(sentenceTokens);
            }
        }
        if (!current.isEmpty()) {
            chunks.add(current.toString().trim());
        }
        return applyOverlap(chunks);
    }

    private List<String> splitSentences(String text) {
        String[] parts = SENTENCE_PATTERN.split(text);
        List<String> sentences = new ArrayList<>();
        for (String part : parts) {
            if (StringUtils.hasText(part)) {
                sentences.add(part.trim());
            }
        }
        if (sentences.isEmpty()) {
            sentences.add(text.trim());
        }
        return sentences;
    }

    private Set<String> tokenize(String text) {
        Set<String> tokens = new HashSet<>();
        var matcher = WORD_PATTERN.matcher(text.toLowerCase());
        while (matcher.find()) {
            String token = matcher.group();
            tokens.add(token);
            if (containsCjk(token)) {
                for (int i = 0; i < token.length() - 1; i++) {
                    tokens.add(token.substring(i, i + 2));
                }
            }
        }
        return tokens;
    }

    private boolean containsCjk(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (Character.UnicodeScript.of(text.charAt(i)) == Character.UnicodeScript.HAN) {
                return true;
            }
        }
        return false;
    }

    private double jaccardSimilarity(Set<String> left, Set<String> right) {
        if (left.isEmpty() && right.isEmpty()) {
            return 1;
        }
        Set<String> intersection = new HashSet<>(left);
        intersection.retainAll(right);
        Set<String> union = new HashSet<>(left);
        union.addAll(right);
        return union.isEmpty() ? 0 : (double) intersection.size() / union.size();
    }

    private List<String> applyOverlap(List<String> chunks) {
        if (chunkOverlap <= 0 || chunks.size() <= 1) {
            return chunks;
        }
        List<String> overlapped = new ArrayList<>();
        overlapped.add(chunks.get(0));
        for (int i = 1; i < chunks.size(); i++) {
            String previous = chunks.get(i - 1);
            String current = chunks.get(i);
            int overlapStart = Math.max(0, previous.length() - chunkOverlap);
            overlapped.add((previous.substring(overlapStart) + current).trim());
        }
        return overlapped;
    }
}
