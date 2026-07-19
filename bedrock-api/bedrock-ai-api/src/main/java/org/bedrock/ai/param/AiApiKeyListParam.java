package org.bedrock.ai.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.common.ai.enums.AiPlatformEnum;

import java.io.Serial;
import java.io.Serializable;

/**
 * AI API Key 列表查询参数
 */
@Data
public class AiApiKeyListParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 配置名称，模糊匹配
     */
    @Schema(description = "名称")
    private String keyName;

    /**
     * AI 厂商平台
     */
    @Schema(description = "厂商平台")
    private AiPlatformEnum platform;

    /**
     * 配置状态（1=启用，0=禁用）
     */
    @Schema(description = "配置状态（1=启用，0=禁用）")
    private Integer status;

}
