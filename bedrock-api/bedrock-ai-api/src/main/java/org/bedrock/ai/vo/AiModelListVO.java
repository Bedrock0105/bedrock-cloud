package org.bedrock.ai.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.common.ai.enums.AiModelTypeEnum;
import org.bedrock.common.ai.enums.AiPlatformEnum;

import java.io.Serial;
import java.io.Serializable;

/**
 * AI 模型列表 VO
 */
@Data
public class AiModelListVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键")
    private Long id;

    /**
     * 关联的 API Key 配置主键（bedrock_ai_api_key.id）
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "关联的 API Key 配置主键（bedrock_ai_api_key.id）")
    private Long apiKeyId;

    /**
     * 关联的 API Key 名称（联表查询展示用）
     */
    @Schema(description = "API Key 名称")
    private String apiKeyName;

    /**
     * 模型显示名称
     */
    @Schema(description = "模型名称")
    private String modelName;

    /**
     * 模型头像 URL
     */
    @Schema(description = "模型头像 URL")
    private String modelAvatar;

    /**
     * 模型标识
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
     * 温度，0-1 之间的小数
     */
    @Schema(description = "温度（0-1）")
    private Double temperature;

    /**
     * 单条回复最大 Token 数量
     */
    @Schema(description = "单条回复最大 Token 数量")
    private Integer maxTokens;

    /**
     * 上下文最大消息数量
     */
    @Schema(description = "最大消息数量")
    private Integer maxMessages;

    /**
     * 是否支持多模态图片识别（1=是，0=否，仅对话模型有效）
     */
    @Schema(description = "是否支持多模态图片识别（1=是，0=否）")
    private Integer supportMultimodal;

    /**
     * 排序值，越小越靠前
     */
    @Schema(description = "排序值")
    private Integer sortOrder;

    /**
     * 是否默认模型（1=是，0=否）
     */
    @Schema(description = "是否默认模型")
    private Integer isDefault;

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
