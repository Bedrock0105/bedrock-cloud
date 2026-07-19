package org.bedrock.gateway.handler;

import lombok.extern.slf4j.Slf4j;
import org.bedrock.gateway.support.ResponseResultSupport;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Slf4j
public class CustomGlobalExceptionHandler implements WebExceptionHandler, Ordered {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        int code = 500;
        String message = "请求异常";
        // 2. 根据异常类型设置错误码和信息
        if (ex instanceof ResponseStatusException) {
            // 处理响应状态异常（如404路由不存在）
            ResponseStatusException statusEx = (ResponseStatusException) ex;
            code = statusEx.getStatusCode().value();
            message = (statusEx.getReason() != null ? statusEx.getReason() : "请求异常");
        } else if (ex instanceof org.springframework.cloud.gateway.support.NotFoundException) {
            // 处理路由不存在异常
            code = (HttpStatus.NOT_FOUND.value());
            message = "请求的服务路由不存在";
        }
        // 3. 记录异常日志（便于排查）
//        log.error("网关异常：{}", ex.getMessage(), ex);
        log.error("网关异常：{}", ex.getMessage());
        return ResponseResultSupport.writeError(exchange.getResponse(), code, message, exchange.getRequest().getPath().value());
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
