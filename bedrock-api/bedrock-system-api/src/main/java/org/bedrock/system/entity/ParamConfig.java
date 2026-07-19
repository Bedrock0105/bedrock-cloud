package org.bedrock.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bedrock.common.mybatisplus.base.BaseEntity;

@Data
@Schema(description = "参数配置")
@EqualsAndHashCode(callSuper = true)
@TableName("bedrock_param_config")
public class ParamConfig extends BaseEntity {

    /**
     * 配置键（唯一标识，如"sys_api_timeout"=接口超时时间、"sys_file_max_size"=文件最大上传大小）
     */
    @Schema(description = "配置键（唯一标识，")
    private String configKey;

    /**
     * 配置值（具体配置内容，如"3000"（毫秒）、"10"（MB））
     */
    @Schema(description = "配置值")
    private String configValue;

    /**
     * 配置名称（显示用描述，如"接口超时时间（毫秒）"、"文件最大上传大小（MB）"）
     */
    @Schema(description = "配置名称")
    private String configName;

    /**
     * 备注（如"该配置超过10MB可能导致服务器存储压力增大"）
     */
    @Schema(description = "备注")
    private String remark;

}
