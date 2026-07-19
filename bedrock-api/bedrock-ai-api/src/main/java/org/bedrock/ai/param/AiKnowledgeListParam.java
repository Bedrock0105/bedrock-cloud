package org.bedrock.ai.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * AI 知识库列表查询参数
 * <p>支持按名称、状态筛选</p>
 */
@Data
public class AiKnowledgeListParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 知识库名称，模糊匹配
     */
    @Schema(description = "知识库名称")
    private String knowledgeName;

    /**
     * 配置状态：1=启用，0=禁用
     */
    @Schema(description = "配置状态（1=启用，0=禁用）")
    private Integer status;

}
