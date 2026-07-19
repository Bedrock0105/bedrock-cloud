package org.bedrock.system.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.common.code.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class AdminOnlineListVO {

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
     * 账号
     */
    @Schema(description = "账号")
    private String username;

    /**
     * token过期时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @Schema(description = "token过期时间")
    private LocalDateTime tokenExpired;

    /**
     * 头像
     */
    @Schema(description = "头像")
    private String avatar;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;

    /**
     * 昵称
     */
    @Schema(description = "昵称")
    private String nickname;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String phone;

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
