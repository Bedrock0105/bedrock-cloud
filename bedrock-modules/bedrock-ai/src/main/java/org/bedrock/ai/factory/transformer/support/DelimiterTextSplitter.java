package org.bedrock.ai.factory.transformer.support;

import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 自定义分隔符分片器：仅按指定分隔符切分，再合并到目标长度。
 */
public class DelimiterTextSplitter extends TextSplitter {

    private final String delimiter;

    private final int chunkSize;

    private final int chunkOverlap;

    public DelimiterTextSplitter(String delimiter, int chunkSize, int chunkOverlap) {
        Assert.notNull(delimiter, "delimiter cannot be empty");
        this.delimiter = delimiter;
        this.chunkSize = chunkSize;
        this.chunkOverlap = Math.max(0, Math.min(chunkOverlap, chunkSize / 2));
    }

    @Override
    protected List<String> splitText(String text) {
        if (!StringUtils.hasText(text)) {
            return List.of();
        }
        List<String> segments = Arrays.stream(text.split(java.util.regex.Pattern.quote(delimiter), -1))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .toList();
        if (segments.isEmpty()) {
            return List.of(text.trim());
        }

        List<String> chunks = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        for (String segment : segments) {
            if (segment.length() > chunkSize) {
                if (!current.isEmpty()) {
                    chunks.add(current.toString().trim());
                    current.setLength(0);
                }
                chunks.addAll(RecursiveCharacterTextSplitter.paragraph(chunkSize, chunkOverlap).splitText(segment));
                continue;
            }
            String candidate = current.isEmpty() ? segment : current + delimiter + segment;
            if (candidate.length() <= chunkSize) {
                current.setLength(0);
                current.append(candidate);
            }
            else {
                chunks.add(current.toString().trim());
                current = new StringBuilder(segment);
            }
        }
        if (!current.isEmpty()) {
            chunks.add(current.toString().trim());
        }
        return applyOverlap(chunks);
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
