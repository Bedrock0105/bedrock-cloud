package org.bedrock.ai.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bedrock.common.tenant.base.TenantEntity;

import java.io.Serial;

/**
 * AI 知识库配置
 * <p>关联嵌入模型与向量数据库配置，用于 RAG 检索增强</p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bedrock_ai_knowledge")
@Schema(description = "AI 知识库配置")
public class AiKnowledge extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 知识库名称，同租户下唯一
     */
    @Schema(description = "知识库名称")
    private String knowledgeName;

    /**
     * 知识库描述
     */
    @Schema(description = "知识库描述")
    private String remark;

    /**
     * 嵌入模型 id，关联 bedrock_ai_model.id，模型类型需为 EMBEDDING
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "嵌入模型 id")
    private Long embeddingModelId;

    /**
     * 向量库 id，关联 bedrock_ai_vector_db.id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "向量库 id")
    private Long vectorDbId;

    /**
     * 配置状态：1=启用，0=禁用；新增默认禁用
     */
    @Schema(description = "配置状态（1=启用，0=禁用）")
    private Integer status;

}
