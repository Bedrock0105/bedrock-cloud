package org.bedrock.system.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TenantListParam {

    /**
     * 租户ID
     */
    @Schema(description = "租户ID")
    private String tenantId;

    /**
     * 状态
     */
    @Schema(description = "状态")
    private Integer status;

    /**
     * 租户全称（如 阿里巴巴集团有限公司）
     */
    @Schema(description = "租户全称（如 阿里巴巴集团有限公司）")
    private String tenantName;

    /**
     * 联系人姓名
     */
    @Schema(description = "联系人姓名")
    private String contactPerson;

    /**
     * 联系人手机号（用于登录验证、通知）
     */
    @Schema(description = "联系人手机号（用于登录验证、通知）")
    private String contactPhone;

    /**
     * 联系人邮箱（用于找回密码、系统通知）
     */
    @Schema(description = "联系人邮箱（用于找回密码、系统通知）")
    private String contactEmail;
}
