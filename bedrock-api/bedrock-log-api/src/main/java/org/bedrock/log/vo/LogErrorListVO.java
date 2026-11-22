package org.bedrock.log.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.bedrock.common.code.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 错误日志列表项
 */
@Data
public class LogErrorListVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 异常类名 */
    private String exceptionName;

    /** 异常消息 */
    private String exceptionMessage;

    /** 服务名称 */
    private String serviceName;

    /** 服务器 IP */
    private String serverIp;

    /** 环境 */
    private String env;

    /** 客户端 IP */
    private String remoteIp;

    /** 请求地址 */
    private String requestUrl;

    /** 请求方式 */
    private String requestType;

    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    private LocalDateTime createTime;
}
