package org.bedrock.resource.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.common.resource.enums.EmailEnum;

import java.io.Serial;
import java.io.Serializable;

@Data
public class EmailConfigListParam implements Serializable {

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

}
