package org.bedrock.log.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.bedrock.common.code.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class LogOperationListVO {
    /**
     * 主键id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 保存的操作日志的类型，比如：订单类型、商品类型
     */
    private String type;

    /**
     * 日志的子类型，
     */
    private String subType;

    /**
     * 执行时间(毫秒)
     */
    private Long duration;

    /**
     * 日志内容
     */
    private String action;

    /**
     * 用户姓名
     */
    private String userName;

    //---------------2. 日志基础属性（通用必录）------------------

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 服务器ip (192.168.1.5:7006)
     */
    private String serverIp;

    /**
     * 环境
     */
    private String env;

    //---------------3. 操作人/上下文信息-----------

    /**
     * 操作IP地址
     */
    private String remoteIp;

    //------------------------4.1 接口请求场景--------------------

    /**
     * 请求地址 （如/api/order/create）
     */
    private String requestUrl;

    /**
     * 请求方式 （GET/POST/PUT/DELETE）
     */
    private String requestType;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    private LocalDateTime createTime;
}
