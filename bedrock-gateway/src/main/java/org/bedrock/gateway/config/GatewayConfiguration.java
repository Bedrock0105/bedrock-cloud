package org.bedrock.gateway.config;

import org.bedrock.gateway.filter.AuthorizeFilter;
import org.bedrock.gateway.filter.RequestLogFilter;
import org.bedrock.gateway.handler.CustomGlobalExceptionHandler;
import org.bedrock.gateway.props.SecurityProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebExceptionHandler;

@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class GatewayConfiguration {

    /**
     * 自定义全局异常处理
     */
    @Bean
    public WebExceptionHandler customGlobalExceptionHandler() {
        return new CustomGlobalExceptionHandler();
    }

    /**
     * 鉴权过滤器
     */
    @Bean
    public AuthorizeFilter authorizeFilter(SecurityProperties securityProperties) {
        return new AuthorizeFilter(securityProperties);
    }

    /**
     * 请求日志过滤器
     */
    @Bean
    @ConditionalOnProperty(prefix = "bedrock.gateway.log", name = "enabled", havingValue = "true", matchIfMissing = true)
    public RequestLogFilter requestLogFilter() {
        return new RequestLogFilter();
    }
}
