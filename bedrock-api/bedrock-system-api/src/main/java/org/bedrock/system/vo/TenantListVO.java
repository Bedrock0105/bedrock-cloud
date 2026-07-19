package org.bedrock.system.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.common.code.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class TenantListVO {

    @Schema(description = "ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Schema(description = "租户ID")
    private String tenantId;

    /**
     * 租户全称（如 阿里巴巴集团有限公司）
     */
    @Schema(description = "租户全称（如 阿里巴巴集团有限公司）")
    private String tenantName;

    /**
     * 租户简称（如 阿里，用于显示优化）
     */
    @Schema(description = "租户简称（如 阿里，用于显示优化）")
    private String tenantShortName;

    /**
     * 租户Logo地址（OSS/CDN链接）
     */
    @Schema(description = "租户Logo地址（OSS/CDN链接）")
    private String tenantLogo;

    /**
     * 所属行业（如 互联网、金融、制造，可关联字典表）
     */
    @Schema(description = "所属行业（如 互联网、金融、制造，可关联字典表）")
    private String industry;

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

    /**
     * 过期时间（为空表示永久有效，用于付费租户）
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @Schema(description = "过期时间（为空表示永久有效，用于付费租户）")
    private LocalDateTime expireTime;

    /**
     * 租户状态（1-正常，0-禁用）
     */
    @Schema(description = "租户状态（1-正常，0-禁用）")
    private Integer status;

}
