package org.bedrock.system.entity;

import java.io.Serial;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bedrock.common.mybatisplus.base.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bedrock_client")
public class Client extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 客户端唯一标识（主键）
     */
    @Schema(description = "客户端唯一标识（主键）")
    private String clientId;

    /**
     * 客户端可访问的资源ID列表（多个用逗号分隔）
     */
    @Schema(description = "客户端可访问的资源ID列表（多个用逗号分隔）")
    private String resourceIds;

    /**
     * 客户端密钥（加密存储）
     */
    @Schema(description = "客户端密钥")
    private String clientSecret;

    /**
     * 客户端权限范围（多个用空格分隔，如read write）
     */
    @Schema(description = "客户端权限范围（多个用空格分隔，如read write）")
    private String scope;

    /**
     * 支持的授权类型（多个用逗号分隔，如password,refresh_token）
     */
    @Schema(description = "支持的授权类型（多个用逗号分隔，如password,refresh_token）")
    private String authorizedGrantTypes;

    /**
     * 注册的重定向URI（多个用逗号分隔）
     */
    @Schema(description = "注册的重定向URI（多个用逗号分隔）")
    private String registeredRedirectUri;

    /**
     * access_token有效期（秒，NULL表示使用默认值）
     */
    @Schema(description = "access_token有效期（秒，NULL表示使用默认值）")
    private Integer accessTokenValidity;

    /**
     * refresh_token有效期（秒，NULL表示使用默认值）
     */
    @Schema(description = "refresh_token有效期（秒，NULL表示使用默认值）")
    private Integer refreshTokenValidity;

    /**
     * 额外信息（通常存储JSON格式的扩展配置）
     */
    @Schema(description = "额外信息（通常存储JSON格式的扩展配置）")
    private String additionalInformation;

    /**
     * 不需要确定的鉴权
     */
    @Schema(description = "不需要确定的鉴权")
    private String autoApprove;

    @Schema(description = "描述")
    private String remark;

}
