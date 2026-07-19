package org.bedrock.system.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.bedrock.common.code.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class TenantSubmitParam {


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
     * 账号
     */
    @Schema(description = "账号")
    private String username;

    /**
     * 密码
     */
    @Schema(description = "密码")
    private String password;

    /**
     * 所属行业（如 互联网、金融、制造，可关联字典表）
     */
    @Schema(description = "所属行业（如 互联网、金融、制造，可关联字典表）")
    private String industry;

    /**
     * 业务描述（租户核心业务简介）
     */
    @Schema(description = "业务描述（租户核心业务简介）")
    private String businessDesc;

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
     * 联系地址
     */
    @Schema(description = "联系地址")
    private String contactAddress;

    /**
     * 套餐ID（关联套餐表）
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "套餐ID（关联套餐表）")
    private Long packageId;

    /**
     * 独立数据库实例ID（独立数据库时使用，关联数据库配置表）
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "独立数据库实例ID（独立数据库时使用，关联数据库配置表）")
    private Long dbInstanceId;

    /**
     * 租户专属域名（如 alibaba.xxx.com，支持多域名绑定）
     */
    @Schema(description = "租户专属域名（如 alibaba.xxx.com，支持多域名绑定）")
    private String tenantDomain;

    /**
     * 最大用户数（-1表示无限制）
     */
    @Schema(description = "最大用户数（-1表示无限制）")
    private Integer maxUserNum;

    /**
     * 过期时间（为空表示永久有效，用于付费租户）
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @Schema(description = "过期时间（为空表示永久有效，用于付费租户）")
    private LocalDateTime expireTime;

    /**
     * 禁用/注销原因（如 欠费、违规操作）
     */
    @Schema(description = "禁用/注销原因（如 欠费、违规操作）")
    private String disableReason;

    /**
     * 备注（如 2025年付费旗舰版租户）
     */
    @Schema(description = "备注（如 2025年付费旗舰版租户）")
    private String remark;

    /**
     * 扩展配置（JSON格式，如 {"sms_enabled":true, "wechat_login":false}）
     */
    @Schema(description = "扩展配置（JSON格式，如 {\"sms_enabled\":true, \"wechat_login\":false}）")
    private String extConfig;
}
