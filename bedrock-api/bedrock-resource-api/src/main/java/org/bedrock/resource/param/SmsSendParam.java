package org.bedrock.resource.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * 短信发送参数
 */
@Data
public class SmsSendParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 接收手机号
     */
    @Schema(description = "手机号")
    private String phone;

    /**
     * 短信模板变量参数（如：{"code":"123456"}）
     */
    @Schema(description = "模板参数")
    private Map<String, Object> params;

}
