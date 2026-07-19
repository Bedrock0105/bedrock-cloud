package org.bedrock.system.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TenantEnableParam {

    /**
     * 租户ID
     */
    @Schema(description = "租户ID")
    private Long id;

    /**
     * 禁用/注销原因（如 欠费、违规操作）
     */
    @Schema(description = "禁用/注销原因（如 欠费、违规操作）")
    private String disableReason;

    /**
     * 租户状态（1-正常，0-禁用）
     */
    @Schema(description = "租户状态（1-正常，0-禁用）")
    private Integer status;

}
