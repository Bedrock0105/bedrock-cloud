package org.bedrock.auth;

import org.bedrock.common.cloud.annotation.BedrockCloudApplication;
import org.bedrock.common.code.start.BedrockApplication;
import org.bedrock.common.constant.ApplicationConstant;

@BedrockCloudApplication
public class AuthApplication {

    public static void main(String[] args) {
        BedrockApplication.run(ApplicationConstant.APPLICATION_AUTH_NAME, AuthApplication.class, args);
    }
}
