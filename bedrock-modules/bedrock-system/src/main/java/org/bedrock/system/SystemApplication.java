package org.bedrock.system;

import org.bedrock.common.cloud.annotation.BedrockCloudApplication;
import org.bedrock.common.code.start.BedrockApplication;
import org.bedrock.common.constant.ApplicationConstant;

@BedrockCloudApplication
public class SystemApplication {

    public static void main(String[] args) {
        BedrockApplication.run(ApplicationConstant.APPLICATION_SYSTEM_NAME, SystemApplication.class, args);
    }
}
