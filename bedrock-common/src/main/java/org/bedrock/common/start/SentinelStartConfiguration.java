package org.bedrock.common.start;

import org.bedrock.common.code.start.StartConfiguration;
import org.bedrock.common.constant.SentinelConstant;
import org.bedrock.common.spi.autoservice.AutoService;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Properties;

@AutoService(StartConfiguration.class)
public class SentinelStartConfiguration implements StartConfiguration {

    @Override
    public void configure(String appName, Properties defaultProperties, SpringApplicationBuilder builder, String profile) {
        SentinelConstant.SentinelConfig sentinelConfig = SentinelConstant.getSentinelConfigByEnvironment(profile);
        /**
         * Sentinel 配置
         */
        defaultProperties.setProperty("spring.cloud.sentinel.transport.dashboard", sentinelConfig.address());
        defaultProperties.setProperty("spring.cloud.sentinel.log.dir", sentinelConfig.logDir());
    }
}
