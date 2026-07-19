package org.bedrock.resource.param;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.common.resource.enums.OssEnum;

import java.io.Serial;
import java.io.Serializable;

@Data
public class OssConfigSubmitParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键")
    private Long id;

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

    /**
     * OSS访问端点（如阿里云：oss-cn-beijing.aliyuncs.com；内网端点需注明）
     */
    @Schema(description = "OSS访问端点（如阿里云：oss-cn-beijing.aliyuncs.com；内网端点需注明）")
    private String endpoint;

    /**
     * 访问密钥ID（AK，非敏感但需权限控制）
     */
    @Schema(description = "访问密钥ID（AK，非敏感但需权限控制）")
    private String accessKey;

    /**
     * 访问密钥密钥（SK，敏感信息，需加密存储）
     */
    @Schema(description = "访问密钥密钥（SK，敏感信息，需加密存储）")
    private String secretKey;

    /**
     * OSS存储桶名称（Bucket Name，全局唯一）
     */
    @Schema(description = "OSS存储桶名称（Bucket Name，全局唯一）")
    private String bucketName;

    /**
     * 存储桶地域（如：cn-beijing/cn-shanghai/ap-hongkong）
     */
    @Schema(description = "存储桶地域（如：cn-beijing/cn-shanghai/ap-hongkong）")
    private String bucketRegion;

    /**
     * 文件存储路径前缀（如：avatar/2025/，避免文件混乱）
     */
    @Schema(description = "文件存储路径前缀（如：avatar/2025/，避免文件混乱）")
    private String prefixPath;

    /**
     * 公开访问URL 默认是 endpoint
     */
    @Schema(description = "公开访问URL 默认是 endpoint")
    private String publicUrl;

    /**
     * 描述
     */
    @Schema(description = "描述")
    private String remark;
}
