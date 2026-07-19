package org.bedrock.system.param;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AdminOnlineSubmitParam {

    /**
     * 用户ID（关联业务系统用户表主键）
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "用户ID（关联业务系统用户表主键）")
    private Long adminId;

    /**
     * token唯一标识
     */
    @Schema(description = "token唯一标识")
    private String tokenId;

    /**
     * 用户访问Token
     */
    @Schema(description = "用户访问Token")
    private String token;

    /**
     * WebSocket连接唯一标识（服务器端连接实例ID，便于定位连接）
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "WebSocket连接唯一标识（服务器端连接实例ID，便于定位连接）")
    private Long wsOnlyId;

    /**
     * 客户端IP地址（记录连接来源）
     */
    @Schema(description = "客户端IP地址（记录连接来源）")
    private String clientIp;
}
