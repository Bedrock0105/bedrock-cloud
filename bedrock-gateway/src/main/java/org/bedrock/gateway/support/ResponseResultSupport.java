package org.bedrock.gateway.support;

import org.bedrock.common.code.util.JsonUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

/**
 * 编写响应对象
 */
public class ResponseResultSupport {

    public static Mono<Void> writeError(ServerHttpResponse response, GatewayErrorResponse<?> errorResponse) {
        HttpStatusCode statusCode = response.getStatusCode();
        if (statusCode == null || statusCode.is2xxSuccessful()) {
            HttpStatus resolve = HttpStatus.resolve(errorResponse.code);
            if (resolve == null) {
                resolve = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            response.setStatusCode(resolve);
        }

        // 4. 设置响应信息
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(JsonUtil.toJsonBytes(errorResponse))));
    }

    /**
     * 错误响应
     *
     * @param code    错误码
     * @param message 错误信息
     */
    public static Mono<Void> writeError(ServerHttpResponse response, int code, String message) {
        return writeError(response, new GatewayErrorResponse<>(code, message, null, System.currentTimeMillis(), false));
    }

    /**
     * 错误响应
     *
     * @param code    错误码
     * @param message 错误信息
     */
    public static <T> Mono<Void> writeError(ServerHttpResponse response, int code, String message, T data) {
        return writeError(response, new GatewayErrorResponse<>(code, message, data, System.currentTimeMillis(), false));
    }

    public record GatewayErrorResponse<T>(int code, String msg, T data, long timestamp, boolean success) {

    }
}
