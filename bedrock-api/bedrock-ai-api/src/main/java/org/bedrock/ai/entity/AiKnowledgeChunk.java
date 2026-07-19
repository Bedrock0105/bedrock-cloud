package org.bedrock.ai.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.EqualsAndHashCode;
import org.bedrock.common.tenant.base.TenantEntity;

import java.io.Serial;

/**
 * 知识库-文档分片段落表（向量存储最小单元）
 *
 * @TableName bedrock_ai_knowledge_chunk
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bedrock_ai_knowledge_chunk")
@Schema(description = "知识库-文档分片段落表（向量存储最小单元）")
public class AiKnowledgeChunk extends TenantEntity {

    @Serial
    private final static long serialVersionUID = 1L;

    /**
     * 所属知识库ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "所属知识库ID")
    private Long knowledgeId;

    /**
     * 归属文档主键，关联 bedrock_ai_knowledge_doc.id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "归属文档主键，关联 bedrock_ai_knowledge_doc.id")
    private Long docId;

    /**
     * 文档内分片序号（从1自增，用于还原段落顺序）
     */
    @Schema(description = "文档内分片序号（从1自增，用于还原段落顺序）")
    private Integer chunkNo;

    /**
     * 分片纯文本内容（送入Embedding做向量化）
     */
    @Schema(description = "分片纯文本内容（送入Embedding做向量化）")
    private String chunkContent;

    /**
     * 本段文本预估token数量，用于分片策略复盘
     */
    @Schema(description = "本段文本预估token数量，用于分片策略复盘")
    private Integer chunkTokenCount;

    /**
     * 向量库内对应向量唯一ID（Milvus/Qdrant/Redis Stack主键）
     */
    @Schema(description = "向量库内对应向量唯一ID（Milvus/Qdrant/Redis Stack主键）")
    private String vectorId;

    /**
     * 向量化状态：0待向量化、1入库成功、2向量写入失败
     */
    @Schema(description = "向量化状态：0待向量化、1入库成功、2向量写入失败")
    private Integer embedStatus;

    /**
     * 当前分片被检索召回总次数
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "当前分片被检索召回总次数")
    private Long recallCount;

    /**
     * 配置状态（1=启用，0=禁用）
     */
    @Schema(description = "配置状态（1=启用，0=禁用）")
    private Integer status;

}
