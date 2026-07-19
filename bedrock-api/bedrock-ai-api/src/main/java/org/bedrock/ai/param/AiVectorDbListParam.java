package org.bedrock.ai.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.common.ai.enums.VectorStoreEnum;

import java.io.Serial;
import java.io.Serializable;

/**
 * 向量数据库配置列表查询参数
 */
@Data
public class AiVectorDbListParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "配置名称")
    private String dbName;

    @Schema(description = "向量数据库类型")
    private VectorStoreEnum vectorDbType;

    @Schema(description = "配置状态（1=启用，0=禁用）")
    private Integer status;

}
