package org.bedrock.auth.config;

import org.bedrock.auth.handler.TokenParamTokenEnhancerHandler;
import org.bedrock.auth.service.UserDetailsServiceImpl;
import org.bedrock.common.authentication.handler.TokenEnhancerHandler;
import org.bedrock.common.authentication.service.UserDetailsService;
import org.bedrock.system.feign.IAdminClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class AuthConfiguration {

    /**
     * 添加参数token增强处理器
     *
     * @return
     */
    @Bean
    public TokenEnhancerHandler tokenParamTokenEnhancerHandler() {
        return new TokenParamTokenEnhancerHandler();
    }

    @Bean
    public UserDetailsService userDetailsService(IAdminClient adminClient) {
        return new UserDetailsServiceImpl(adminClient);
    }
}
