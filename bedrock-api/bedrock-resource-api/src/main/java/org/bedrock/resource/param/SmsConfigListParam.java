package org.bedrock.resource.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.common.resource.enums.SmsEnum;

import java.io.Serial;
import java.io.Serializable;

@Data
public class SmsConfigListParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 配置名称（如：生产环境-验证码短信）
     */
    @Schema(description = "配置名称（如：生产环境-验证码短信）")
    private String configName;

    /**
     * 配置编码（唯一标识，如：PROD_SMS_CODE，用于代码中调用）
     */
    @Schema(description = "配置编码（唯一标识，如：PROD_SMS_CODE，用于代码中调用）")
    private String configCode;

    /**
     * 短信服务商
     */
    @Schema(description = "短信服务商")
    private SmsEnum serviceProvider;

}
