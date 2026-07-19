package org.bedrock.resource;

import org.bedrock.common.cloud.annotation.BedrockCloudApplication;
import org.bedrock.common.code.start.BedrockApplication;
import org.bedrock.common.constant.ApplicationConstant;

@BedrockCloudApplication
public class ResourceApplication {

    public static void main(String[] args) {
        BedrockApplication.run(ApplicationConstant.APPLICATION_RESOURCE_NAME, ResourceApplication.class, args);
    }
}
