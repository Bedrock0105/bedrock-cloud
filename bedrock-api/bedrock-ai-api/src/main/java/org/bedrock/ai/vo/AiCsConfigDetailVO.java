package org.bedrock.ai.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.ai.dto.AiChatOptions;

import java.io.Serial;
import java.io.Serializable;

/**
 * AI 客服配置详情（管理端表单回填）。
 */
@Data
@Schema(description = "AI 客服配置详情")
public class AiCsConfigDetailVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "客服名称")
    private String name;

    @Schema(description = "客服头像")
    private String avatar;

    @Schema(description = "描述")
    private String remark;

    @Schema(description = "系统提示词")
    private String systemPrompt;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "默认模型 id")
    private Long modelId;

    @Schema(description = "默认模型名称")
    private String modelName;

    @Schema(description = "对话调用配置")
    private AiChatOptions chatOptions;

    @Schema(description = "配置状态（1=启用，0=禁用）")
    private Integer status;

    @Schema(description = "是否启用")
    private boolean enabled = true;

    /**
     * 构建未启用结果，便于统一缓存结构。
     */
    public static AiCsConfigDetailVO disabled() {
        AiCsConfigDetailVO vo = new AiCsConfigDetailVO();
        vo.setEnabled(false);
        return vo;
    }

}
