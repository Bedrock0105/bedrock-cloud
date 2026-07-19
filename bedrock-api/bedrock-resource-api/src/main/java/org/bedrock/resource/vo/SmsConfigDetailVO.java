package org.bedrock.resource.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.common.resource.enums.SmsEnum;

import java.io.Serial;
import java.io.Serializable;

@Data
public class SmsConfigDetailVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键")
    private Long id;

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

    /**
     * 短信 API 账号
     */
    @Schema(description = "短信 API 账号")
    private String apiKey;

    /**
     * 短信 API 密钥（敏感信息，需加密存储）
     */
    @Schema(description = "短信 API 密钥")
    private String apiSecret;

    /**
     * 短信签名
     */
    @Schema(description = "短信签名")
    private String signature;

    /**
     * 短信模板 ID
     */
    @Schema(description = "短信模板 ID")
    private String templateId;

    /**
     * 服务端点（可选，未配置时使用各厂商默认端点）
     */
    @Schema(description = "服务端点")
    private String endpoint;

    /**
     * 服务地域（如：cn-hangzhou、ap-guangzhou）
     */
    @Schema(description = "服务地域")
    private String region;

    /**
     * 短信应用 AppId（仅腾讯云 TENCENT 使用）
     */
    @Schema(description = "短信应用 AppId（腾讯云）")
    private String appId;

    /**
     * 短信通道号（仅华为云 HUAWEI 使用）
     */
    @Schema(description = "短信通道号（华为云）")
    private String sender;

    /**
     * 描述
     */
    @Schema(description = "描述")
    private String remark;

}
