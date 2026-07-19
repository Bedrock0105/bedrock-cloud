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
public class SmsConfigListVO implements Serializable {

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
     * 服务地域（如：cn-hangzhou、ap-guangzhou）
     */
    @Schema(description = "服务地域")
    private String region;

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
