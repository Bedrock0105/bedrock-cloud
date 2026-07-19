package org.bedrock.gateway;

import org.bedrock.common.code.start.BedrockApplication;
import org.bedrock.common.constant.ApplicationConstant;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        BedrockApplication.run(ApplicationConstant.APPLICATION_GATEWAY_NAME, GatewayApplication.class, args);
    }
}
