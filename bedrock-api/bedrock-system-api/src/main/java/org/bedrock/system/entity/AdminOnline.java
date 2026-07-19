package org.bedrock.system.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;
import org.bedrock.common.code.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@TableName("bedrock_admin_online")
public class AdminOnline {

    /**
     * 主键
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private Long id;

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
     * token过期时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @Schema(description = "token过期时间")
    private LocalDateTime tokenExpired;

    /**
     * 上次心跳时间（WebSocket心跳包触发更新，用于判断是否离线）
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "上次心跳时间（WebSocket心跳包触发更新，用于判断是否离线）")
    private Long lastHeartbeatTime;

    /**
     * 在线状态：0-离线，1-在线（快速查询在线用户）
     */
    @Schema(description = "在线状态：0-离线，1-在线（快速查询在线用户）")
    private Integer onlineStatus;

    /**
     * WebSocket连接唯一标识（服务器端连接实例ID，便于定位连接）
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "WebSocket连接唯一标识（服务器端连接实例ID，便于定位连接）")
    private Long wsOnlyId;

    /**
     * 登录/连接建立时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @Schema(description = "登录/连接建立时间")
    private LocalDateTime loginTime;

    /**
     * 客户端IP地址（记录连接来源）
     */
    @Schema(description = "客户端IP地址（记录连接来源）")
    private String clientIp;
}
