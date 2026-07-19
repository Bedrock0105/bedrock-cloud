package org.bedrock.ai.factory.transformer.support;

import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 递归字符分片器（LangChain RecursiveCharacterTextSplitter 思路）。
 * <p>
 * 按分隔符优先级递归切分，超长片段再降级到更小分隔符。
 * </p>
 */
public class RecursiveCharacterTextSplitter extends TextSplitter {

    private static final List<String> DEFAULT_SEPARATORS = List.of(
            "\n\n", "\n", "。", "！", "？", ".", "!", "?", "；", ";", " ", ""
    );

    private static final List<String> PARAGRAPH_SEPARATORS = List.of(
            "\n\n", "\n", "。", "！", "？", ".", "!", "?"
    );

    private final List<String> separators;

    private final int chunkSize;

    private final int chunkOverlap;

    public RecursiveCharacterTextSplitter(List<String> separators, int chunkSize, int chunkOverlap) {
        this.separators = separators;
        this.chunkSize = chunkSize;
        this.chunkOverlap = Math.max(0, Math.min(chunkOverlap, chunkSize / 2));
    }

    public static RecursiveCharacterTextSplitter paragraph(int chunkSize, int chunkOverlap) {
        return new RecursiveCharacterTextSplitter(PARAGRAPH_SEPARATORS, chunkSize, chunkOverlap);
    }

    public static RecursiveCharacterTextSplitter recursive(int chunkSize, int chunkOverlap) {
        return new RecursiveCharacterTextSplitter(DEFAULT_SEPARATORS, chunkSize, chunkOverlap);
    }

    @Override
    protected List<String> splitText(String text) {
        if (!StringUtils.hasText(text)) {
            return List.of();
        }
        List<String> chunks = splitTextRecursive(text, separators);
        return mergeChunks(chunks);
    }

    private List<String> splitTextRecursive(String text, List<String> currentSeparators) {
        if (text.length() <= chunkSize) {
            return List.of(text);
        }
        if (currentSeparators.isEmpty()) {
            return splitByLength(text);
        }

        String separator = currentSeparators.get(0);
        List<String> nextSeparators = currentSeparators.subList(1, currentSeparators.size());
        List<String> splits = splitBySeparator(text, separator);
        if (splits.size() == 1 && !separator.isEmpty()) {
            return splitTextRecursive(text, nextSeparators);
        }

        List<String> chunks = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        for (String split : splits) {
            if (!StringUtils.hasText(split)) {
                continue;
            }
            String candidate = current.isEmpty() ? split : current + separator + split;
            if (candidate.length() <= chunkSize) {
                current.setLength(0);
                current.append(candidate);
                continue;
            }
            if (!current.isEmpty()) {
                chunks.addAll(splitOversizedChunk(current.toString(), nextSeparators));
                current.setLength(0);
            }
            if (split.length() > chunkSize) {
                chunks.addAll(splitTextRecursive(split, nextSeparators));
            }
            else {
                current.append(split);
            }
        }
        if (!current.isEmpty()) {
            chunks.addAll(splitOversizedChunk(current.toString(), nextSeparators));
        }
        return chunks;
    }

    private List<String> splitOversizedChunk(String chunk, List<String> nextSeparators) {
        if (chunk.length() <= chunkSize) {
            return List.of(chunk);
        }
        return splitTextRecursive(chunk, nextSeparators);
    }

    private List<String> splitBySeparator(String text, String separator) {
        if (separator.isEmpty()) {
            return splitByLength(text);
        }
        return new ArrayList<>(Arrays.asList(text.split(java.util.regex.Pattern.quote(separator), -1)));
    }

    private List<String> splitByLength(String text) {
        List<String> chunks = new ArrayList<>();
        for (int start = 0; start < text.length(); start += chunkSize) {
            int end = Math.min(start + chunkSize, text.length());
            chunks.add(text.substring(start, end));
        }
        return chunks;
    }

    private List<String> mergeChunks(List<String> chunks) {
        if (chunks.isEmpty()) {
            return List.of();
        }
        List<String> merged = new ArrayList<>();
        StringBuilder current = new StringBuilder(chunks.get(0));
        for (int i = 1; i < chunks.size(); i++) {
            String chunk = chunks.get(i);
            if (current.length() + chunk.length() <= chunkSize) {
                current.append(chunk);
            }
            else {
                merged.add(current.toString().trim());
                current = new StringBuilder(chunk);
            }
        }
        merged.add(current.toString().trim());
        return applyOverlap(merged);
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
            String prefix = previous.substring(overlapStart);
            overlapped.add((prefix + current).trim());
        }
        return overlapped;
    }
}
