package org.bedrock.resource.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.common.resource.enums.EmailEnum;

import java.io.Serial;
import java.io.Serializable;

@Data
public class EmailConfigListVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键")
    private Long id;

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
     * 用户名（邮箱账号）
     */
    @Schema(description = "用户名")
    private String username;

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
