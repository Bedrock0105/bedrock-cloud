package org.bedrock.ai.param;

import io.swagger.v3.oas.annotations.media.Schema;
import org.bedrock.ai.enums.DocumentSliceModeEnum;

import java.util.List;
import java.util.Map;

/**
 * 知识库文档拆分参数
 * <p>用于上传向导分片预览及文件入库前的文档拆分</p>
 */
public record AiKnowledgeDocSeparateParam(@Schema(description = "文档") List<DocFileItem> fileItems,
                                          @Schema(description = "文档拆分模式") DocumentSliceModeEnum mode,
                                          @Schema(description = "拆分参数") Map<String, Object> params) {

    public AiKnowledgeDocSeparateParam(List<DocFileItem> fileItems, DocumentSliceModeEnum mode) {
        this(fileItems, mode, Map.of());
    }

    /**
     * 待拆分文件项
     *
     * @param fileName 文件名
     * @param fileUrl  OSS 上传返回的文件访问地址
     * @param size     文件大小（字节）
     */
    public record DocFileItem(@Schema(description = "文件名") String fileName,
                              @Schema(description = "文件访问地址（OSS 上传返回的 url）") String fileUrl,
                              @Schema(description = "文件大小") Long size) {

    }
}
