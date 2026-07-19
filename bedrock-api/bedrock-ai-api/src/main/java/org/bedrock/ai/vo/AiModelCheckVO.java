package org.bedrock.ai.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * AI 模型校验 VO，包含模型详情及关联 API Key 信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AiModelCheckVO extends AiModelDetailVO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 模型状态（1=启用，0=禁用）
     */
    @Schema(description = "模型状态（1=启用，0=禁用）")
    private Integer status;

    /**
     * 关联 API Key 密钥
     */
    @Schema(description = "API Key 密钥")
    private String apiKey;

    /**
     * 关联 API Key 名称（Token 用量统计冗余存储，联表查询自 bedrock_ai_api_key.key_name）
     */
    @Schema(description = "API Key 名称")
    private String apiKeyName;

    /**
     * 关联 API Key Base URL
     */
    @Schema(description = "Base URL")
    private String baseUrl;

    /**
     * 关联 API Key 半路径（相对 Base URL）
     */
    @Schema(description = "API 半路径")
    private String apiPath;

    /**
     * API Key 状态（1=启用，0=禁用），仅用于内部校验
     */
    @JsonIgnore
    private Integer apiKeyStatus;

}
