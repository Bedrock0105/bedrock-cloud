package org.bedrock.log;

import org.bedrock.common.cloud.annotation.BedrockCloudApplication;
import org.bedrock.common.code.start.BedrockApplication;
import org.bedrock.common.constant.ApplicationConstant;

@BedrockCloudApplication
public class LogApplication {

    public static void main(String[] args) {
        BedrockApplication.run(ApplicationConstant.APPLICATION_LOG_NAME, LogApplication.class, args);
    }
}
