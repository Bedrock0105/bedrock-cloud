package org.bedrock.resource.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bedrock.common.resource.enums.EmailEnum;
import org.bedrock.common.tenant.base.TenantEntity;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bedrock_email_config")
public class EmailConfig extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 配置名称（如：生产环境-系统通知邮件）
     */
    @Schema(description = "配置名称（如：生产环境-系统通知邮件）")
    private String configName;

    /**
     * 配置编码（唯一标识，如：PROD_EMAIL_NOTIFY，用于代码中调用）
     */
    @Schema(description = "配置编码（唯一标识，如：PROD_EMAIL_NOTIFY，用于代码中调用）")
    private String configCode;

    /**
     * 邮件服务商
     */
    @Schema(description = "邮件服务商")
    private EmailEnum serviceProvider;

    /**
     * 协议（如：smtp）
     */
    @Schema(description = "协议")
    private String protocol;

    /**
     * SMTP服务器（如：smtp.qq.com）
     */
    @Schema(description = "SMTP服务器")
    private String smtpServer;

    /**
     * SMTP端口号（如：465、587）
     */
    @Schema(description = "SMTP端口号")
    private Integer smtpPort;

    /**
     * 加密类型（1=SSL，2=TLS）
     */
    @Schema(description = "加密类型(1.SSL 2.TLS)")
    private Integer encyType;

    /**
     * 账户验证（0=否，1=是）
     */
    @Schema(description = "账户验证(0.否 1.是)")
    private Integer accountAuth;

    /**
     * 用户名（邮箱账号）
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 密码/授权码（敏感信息，需加密存储）
     */
    @Schema(description = "密码/授权码")
    private String password;

    /**
     * 配置状态（1=启用，0=禁用，下线时设为0）
     */
    @Schema(description = "配置状态（1=启用，0=禁用）")
    private Integer status;

    /**
     * 描述
     */
    @Schema(description = "描述")
    private String remark;

}
