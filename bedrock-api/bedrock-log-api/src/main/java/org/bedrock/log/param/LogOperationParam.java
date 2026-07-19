package org.bedrock.log.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.common.code.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class LogOperationParam {

    /**
     * 保存的操作日志的类型，比如：订单类型、商品类型
     */
    @Schema(description = "保存的操作日志的类型，比如：订单类型、商品类型")
    private String type;

    /**
     * 日志的子类型，
     */
    @Schema(description = "日志的子类型，")
    private String subType;

    /**
     * 用户姓名
     */
    @Schema(description = "用户姓名")
    private String userName;

    /**
     * 服务名称
     */
    @Schema(description = "服务名称")
    private String serviceName;

    /**
     * 服务器ip (192.168.1.5:7006)
     */
    @Schema(description = "服务器ip (192.168.1.5:7006)")
    private String serverIp;

    /**
     * 操作IP地址
     */
    @Schema(description = "操作IP地址")
    private String remoteIp;

    //------------------------4.1 接口请求场景--------------------

    /**
     * 请求地址 （如/api/order/create）
     */
    @Schema(description = "请求地址 （如/api/order/create）")
    private String requestUrl;

    /**
     * 请求方式 （GET/POST/PUT/DELETE）
     */
    @Schema(description = "请求方式 （GET/POST/PUT/DELETE）")
    private String requestType;

    @Schema(description = "开始时间")
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    private LocalDateTime endTime;
}
