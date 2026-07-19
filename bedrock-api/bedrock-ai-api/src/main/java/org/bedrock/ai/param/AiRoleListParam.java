package org.bedrock.ai.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * AI 角色列表查询参数
 */
@Data
public class AiRoleListParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 角色名称，模糊匹配
     */
    @Schema(description = "角色名称")
    private String roleName;

    /**
     * 配置状态（1=启用，0=禁用）
     */
    @Schema(description = "配置状态（1=启用，0=禁用）")
    private Integer status;

}
