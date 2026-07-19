package org.bedrock.common.start;

import org.bedrock.common.code.start.StartConfiguration;
import org.bedrock.common.constant.NacosConstant;
import org.bedrock.common.spi.autoservice.AutoService;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Properties;

@AutoService(StartConfiguration.class)
public class NacosStartConfiguration implements StartConfiguration {

    @Override
    public void configure(String appName, Properties defaultProperties, SpringApplicationBuilder builder, String profile) {
        NacosConstant.NacosConfig nacosConfig = NacosConstant.getNacosConfigByEnvironment(profile);

        /**
         * nacos 配置
         */
        defaultProperties.setProperty("spring.cloud.nacos.config.server-addr", nacosConfig.address());
        defaultProperties.setProperty("spring.cloud.nacos.discovery.server-addr", nacosConfig.address());
        defaultProperties.setProperty("spring.cloud.nacos.username", nacosConfig.username());
        defaultProperties.setProperty("spring.cloud.nacos.password", nacosConfig.password());
        defaultProperties.setProperty("spring.cloud.nacos.discovery.namespace", nacosConfig.namespace());
        defaultProperties.setProperty("spring.cloud.nacos.config.namespace", nacosConfig.namespace());

    }
}
