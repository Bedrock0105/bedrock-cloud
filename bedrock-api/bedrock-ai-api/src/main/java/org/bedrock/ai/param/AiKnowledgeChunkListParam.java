package org.bedrock.ai.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 知识库文档分片列表查询参数
 */
@Data
public class AiKnowledgeChunkListParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "知识库 id")
    private Long knowledgeId;

    @Schema(description = "文档 id")
    private Long docId;

    @Schema(description = "向量化状态：0待向量化、1入库成功、2向量写入失败")
    private Integer embedStatus;

    @Schema(description = "配置状态（1=启用，0=禁用）")
    private Integer status;

}
