package org.bedrock.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Slf4j
public class RequestLogFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String requestId = request.getId();
        // 构建格式化的日志
        String headersFormatted = request.getHeaders().entrySet().stream()
                .map(entry -> String.format("%s: %s", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("\n\t"));

        log.info("""
                        
                        ============== Gateway Request ============
                        RequestId: {}
                        Method: {}
                        Path: {}
                        Headers:
                        \t{}
                        ===========================================""",
                requestId,
                request.getMethod(),
                request.getPath(),
                headersFormatted);

        // 请求开始时间
        long startTime = System.currentTimeMillis();
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            ServerHttpResponse response = exchange.getResponse();
            long duration = System.currentTimeMillis() - startTime;
            String responseHeadersFormatted = response.getHeaders().entrySet().stream()
                    .map(entry -> String.format("%s: %s", entry.getKey(), entry.getValue()))
                    .collect(Collectors.joining("\n\t"));
            log.info("""
                            
                            ============== Gateway Response ============
                            RequestId: {}
                            Method: {}
                            Path: {}
                            Status: {} | Time: {}ms
                            Headers:
                            \t{}
                            ===========================================""",
                    requestId,
                    request.getMethod(),
                    request.getPath(),
                    response.getStatusCode(),
                    duration,
                    responseHeadersFormatted);
        }));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
