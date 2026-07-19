package org.bedrock.ai.param;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * AI 知识库提交参数
 * <p>用于新增与编辑接口，编辑时需携带主键 id</p>
 */
@Data
public class AiKnowledgeSubmitParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键，编辑时必填
     */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键")
    private Long id;

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
     * 嵌入模型 id，模型类型需为 EMBEDDING
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "嵌入模型 id")
    private Long embeddingModelId;

    /**
     * 向量库 id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "向量库 id")
    private Long vectorDbId;

}
