package org.bedrock.ai.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.common.ai.enums.VectorStoreEnum;

import java.io.Serial;
import java.io.Serializable;

/**
 * 向量数据库配置列表 VO
 */
@Data
@Schema(description = "向量数据库配置列表 VO")
public class AiVectorDbListVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "配置名称")
    private String dbName;

    @Schema(description = "向量数据库类型")
    private VectorStoreEnum vectorDbType;

    @Schema(description = "主机地址")
    private String host;

    @Schema(description = "端口")
    private Integer port;

    @Schema(description = "逻辑库/索引名")
    private String databaseName;

    @Schema(description = "集合名/Key 前缀")
    private String collectionName;

    @Schema(description = "向量维度")
    private Integer embeddingDimension;

    @Schema(description = "配置状态（1=启用，0=禁用）")
    private Integer status;

    @Schema(description = "描述")
    private String remark;

}
