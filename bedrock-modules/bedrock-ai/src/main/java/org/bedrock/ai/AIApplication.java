package org.bedrock.ai;

import org.bedrock.common.cloud.annotation.BedrockCloudApplication;
import org.bedrock.common.code.start.BedrockApplication;
import org.bedrock.common.constant.ApplicationConstant;

@BedrockCloudApplication
public class AIApplication {

    public static void main(String[] args) {
        BedrockApplication.run(ApplicationConstant.APPLICATION_AI_NAME, AIApplication.class, args);
    }
}
