package org.bedrock.ai.factory.transformer.support;

import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 标题层级分片器：按 Markdown 标题（# ~ ######）切分，超长章节再递归细分。
 */
public class HierarchyTextSplitter extends TextSplitter {

    private static final Pattern HEADER_PATTERN = Pattern.compile("(?m)^(#{1,6}\\s+.+)$");

    private final int chunkSize;

    private final int chunkOverlap;

    public HierarchyTextSplitter(int chunkSize, int chunkOverlap) {
        this.chunkSize = chunkSize;
        this.chunkOverlap = chunkOverlap;
    }

    @Override
    protected List<String> splitText(String text) {
        if (!StringUtils.hasText(text)) {
            return List.of();
        }
        List<String> sections = splitByHeaders(text);
        RecursiveCharacterTextSplitter fallbackSplitter = RecursiveCharacterTextSplitter.paragraph(
                chunkSize, chunkOverlap);
        List<String> chunks = new ArrayList<>();
        for (String section : sections) {
            if (!StringUtils.hasText(section)) {
                continue;
            }
            if (section.length() <= chunkSize) {
                chunks.add(section.trim());
            }
            else {
                chunks.addAll(fallbackSplitter.splitText(section));
            }
        }
        return chunks;
    }

    private List<String> splitByHeaders(String text) {
        Matcher matcher = HEADER_PATTERN.matcher(text);
        List<Integer> headerPositions = new ArrayList<>();
        while (matcher.find()) {
            headerPositions.add(matcher.start());
        }
        if (headerPositions.isEmpty()) {
            return List.of(text);
        }

        List<String> sections = new ArrayList<>();
        for (int i = 0; i < headerPositions.size(); i++) {
            int start = headerPositions.get(i);
            int end = i + 1 < headerPositions.size() ? headerPositions.get(i + 1) : text.length();
            sections.add(text.substring(start, end));
        }
        int firstHeader = headerPositions.get(0);
        if (firstHeader > 0) {
            sections.add(0, text.substring(0, firstHeader));
        }
        return sections;
    }
}
