package org.bedrock.ai.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.common.ai.enums.AiModelTypeEnum;
import org.bedrock.common.ai.enums.AiPlatformEnum;

import java.io.Serial;
import java.io.Serializable;

/**
 * AI 模型列表查询参数
 */
@Data
public class AiModelListParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 模型名称，模糊匹配
     */
    @Schema(description = "模型名称")
    private String modelName;

    /**
     * 模型标识，模糊匹配
     */
    @Schema(description = "模型标识")
    private String model;

    /**
     * AI 厂商平台
     */
    @Schema(description = "厂商平台")
    private AiPlatformEnum platform;

    /**
     * 模型类型
     */
    @Schema(description = "模型类型")
    private AiModelTypeEnum modelType;

    /**
     * 配置状态（1=启用，0=禁用）
     */
    @Schema(description = "配置状态（1=启用，0=禁用）")
    private Integer status;

}
