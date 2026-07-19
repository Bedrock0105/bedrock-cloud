package org.bedrock.ai.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bedrock.common.ai.enums.AiPlatformEnum;
import org.bedrock.common.tenant.base.TenantEntity;

import java.io.Serial;

/**
 * AI API Key 配置
 * <p>存储各厂商 API Key 及 Base URL，供模型配置关联使用</p>
 */

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bedrock_ai_api_key")
@Schema(description = "AI API Key 配置")
public class AiApiKey extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 配置名称，便于识别
     */
    @Schema(description = "名称")
    private String keyName;

    /**
     * 厂商 API Key 密钥（敏感信息）
     */
    @Schema(description = "API Key")
    private String apiKey;

    /**
     * AI 厂商平台
     */
    @Schema(description = "厂商平台")
    private AiPlatformEnum platform;

    /**
     * 自定义 API 地址，为空时使用厂商默认地址
     */
    @Schema(description = "Base URL（非必填）")
    private String baseUrl;

    /**
     * API 半路径（相对 Base URL），为空时使用厂商/SDK 默认 path
     */
    @Schema(description = "API 半路径（非必填）")
    private String apiPath;

    /**
     * 配置状态（1=启用，0=禁用）
     */
    @Schema(description = "配置状态（1=启用，0=禁用）")
    private Integer status;

    /**
     * 描述
     */
    @Schema(description = "描述")
    private String remark;

}
