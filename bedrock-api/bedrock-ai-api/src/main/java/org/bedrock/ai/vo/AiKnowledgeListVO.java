package org.bedrock.ai.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * AI 知识库列表 VO
 */
@Data
@Schema(description = "AI 知识库列表 VO")
public class AiKnowledgeListVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "知识库名称")
    private String knowledgeName;

    @Schema(description = "知识库描述")
    private String remark;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "嵌入模型 id")
    private Long embeddingModelId;

    @Schema(description = "嵌入模型名称")
    private String embeddingModelName;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "向量库 id")
    private Long vectorDbId;

    @Schema(description = "向量库名称")
    private String vectorDbName;

    @Schema(description = "配置状态（1=启用，0=禁用）")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
