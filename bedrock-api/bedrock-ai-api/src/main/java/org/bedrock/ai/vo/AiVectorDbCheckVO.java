package org.bedrock.ai.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 向量数据库校验 VO，包含连接配置及启用状态
 * <p>单表查询，不关联其他业务表，供运行时创建 VectorStore 使用</p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AiVectorDbCheckVO extends AiVectorDbDetailVO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 配置状态（1=启用，0=禁用）
     */
    @Schema(description = "配置状态（1=启用，0=禁用）")
    private Integer status;

}
