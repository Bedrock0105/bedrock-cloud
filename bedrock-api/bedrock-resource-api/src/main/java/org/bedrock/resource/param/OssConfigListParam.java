package org.bedrock.resource.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.common.resource.enums.OssEnum;

import java.io.Serial;
import java.io.Serializable;

@Data
public class OssConfigListParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 配置名称（如：生产环境-用户头像OSS）
     */
    @Schema(description = "配置名称（如：生产环境-用户头像OSS）")
    private String configName;

    /**
     * 配置编码（唯一标识，如：PROD_AVATAR_OSS，用于代码中调用）
     */
    @Schema(description = "配置编码（唯一标识，如：PROD_AVATAR_OSS，用于代码中调用）")
    private String configCode;

    /**
     * OSS服务商
     */
    @Schema(description = "OSS服务商")
    private OssEnum serviceProvider;
}
