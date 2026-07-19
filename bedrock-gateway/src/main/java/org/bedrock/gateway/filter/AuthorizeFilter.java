package org.bedrock.gateway.filter;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.bedrock.common.auth.constant.TokenConstant;
import org.bedrock.common.auth.util.JwtUtil;
import org.bedrock.common.code.util.AntPathUtil;
import org.bedrock.common.code.util.StringUtil;
import org.bedrock.gateway.props.SecurityProperties;
import org.bedrock.gateway.support.ResponseResultSupport;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@RequiredArgsConstructor
public class AuthorizeFilter implements GlobalFilter, Ordered {

    private final SecurityProperties securityProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        ServerHttpResponse response = exchange.getResponse();
        URI uri = request.getURI();
        /**
         * 判断当前请求路径是否忽略
         */
        if (isKipped(getPath(route, uri))) {
            return chain.filter(exchange);
        }
        /**
         * 获取Token
         */
        String token = getToken(request);
        if (token == null) {
            return ResponseResultSupport.writeError(response, 401, "令牌不能为空");
        }
        /**
         * 解析Token
         */
        Claims claims = JwtUtil.parsePayload(token);
        if (claims == null) {
            return ResponseResultSupport.writeError(response, 401, "令牌已过期或验证不正确");
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }

    /**
     * 获取Token
     *
     * @param request
     * @return
     */
    public String getToken(ServerHttpRequest request) {
        String authorization = request.getHeaders().getFirst(TokenConstant.AUTH_TOKEN);
        if (StringUtil.isBlank(authorization)) {
            authorization = request.getQueryParams().getFirst(TokenConstant.AUTH_TOKEN);
        }
        if (StringUtil.isBlank(authorization)) {
            return null;
        }
        if (authorization.startsWith(TokenConstant.AUTH_BEARER) || authorization.startsWith(TokenConstant.AUTH_BEARER.toLowerCase())) {
            authorization = authorization.substring(TokenConstant.AUTH_BEARER.length() + 1);
        }
        return authorization;
    }

    /**
     * 判断当前请求路径是否忽略
     */
    public boolean isKipped(String path) {
        return AntPathUtil.match(securityProperties.getIgnoreUrls(), path);
    }

    /**
     * 获取当前请求路径
     */
    public String getPath(Route route, URI uri) {
        if (route != null && uri.getPath().contains(route.getUri().getHost())) {
            return uri.getPath().substring(route.getUri().getHost().length() + 1);
        }
        return uri.getPath();
    }
}
