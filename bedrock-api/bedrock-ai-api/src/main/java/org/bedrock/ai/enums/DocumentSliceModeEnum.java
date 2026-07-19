package org.bedrock.ai.enums;

import lombok.Getter;

/**
 * 文档分片模式枚举
 */
@Getter
public enum DocumentSliceModeEnum {

    /**
     * 固定长度分片：按字符/Token固定长度切割
     */
    FIXED_LENGTH("fixed_length", "固定长度分片"),
    /**
     * 语义分片：基于本地文本相似度分割段落（不调用大模型）
     */
    SEMANTIC("semantic", "语义智能分片"),
    /**
     * 层级标题分片：按Markdown/Word标题层级切割
     */
    HIERARCHY("hierarchy", "标题层级分片"),
    /**
     * 自定义分隔符分片：按指定分隔符切分后合并
     */
    DELIMITER("delimiter", "自定义分隔符分片"),
    /**
     * 递归字符分片：递归分割标点/换行/空格；{@code paragraphOnly=true} 时仅按段落/句号边界切分
     */
    RECURSIVE_CHAR("recursive_char", "递归字符分片"),
    /**
     * 完整文档不分片：整篇文档作为单个chunk
     */
    WHOLE_DOC("whole_doc", "完整文档不分片");

    /**
     * 存储入库编码（存入slice_mode字段）
     */
    private final String code;

    /**
     * 前端展示中文名称
     */
    private final String desc;

    DocumentSliceModeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}