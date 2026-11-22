package org.bedrock.log.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.common.code.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 错误日志分页查询参数
 */
@Data
public class LogErrorParam {

    @Schema(description = "异常类名")
    private String exceptionName;

    @Schema(description = "异常消息")
    private String exceptionMessage;

    @Schema(description = "服务名称")
    private String serviceName;

    @Schema(description = "服务器 IP")
    private String serverIp;

    @Schema(description = "客户端 IP")
    private String remoteIp;

    @Schema(description = "请求地址")
    private String requestUrl;

    @Schema(description = "请求方式")
    private String requestType;

    @Schema(description = "环境")
    private String env;

    @Schema(description = "开始时间")
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    private LocalDateTime endTime;
}
