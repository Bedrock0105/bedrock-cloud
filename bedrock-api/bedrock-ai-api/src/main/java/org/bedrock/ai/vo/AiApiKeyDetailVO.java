package org.bedrock.ai.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.common.ai.enums.AiPlatformEnum;

import java.io.Serial;
import java.io.Serializable;

/**
 * AI API Key 详情 VO
 */
@Data
@Schema(description = "AI API Key 详情 VO")
public class AiApiKeyDetailVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键")
    private Long id;

    /**
     * 配置名称
     */
    @Schema(description = "名称")
    private String keyName;

    /**
     * 厂商 API Key 密钥
     */
    @Schema(description = "API Key")
    private String apiKey;

    /**
     * AI 厂商平台
     */
    @Schema(description = "厂商平台")
    private AiPlatformEnum platform;

    /**
     * 自定义 API 地址
     */
    @Schema(description = "Base URL")
    private String baseUrl;

    /**
     * API 半路径（相对 Base URL）
     */
    @Schema(description = "API 半路径")
    private String apiPath;

    /**
     * 描述
     */
    @Schema(description = "描述")
    private String remark;

}
