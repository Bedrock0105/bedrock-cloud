package org.bedrock.ai.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.common.ai.enums.VectorStoreEnum;
import org.bedrock.common.ai.model.factory.vectorstore.VectorStoreCreateParam;

import java.io.Serial;
import java.io.Serializable;

/**
 * 向量数据库配置详情 VO
 */
@Data
@Schema(description = "向量数据库配置详情 VO")
public class AiVectorDbDetailVO implements Serializable {

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

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "逻辑库/索引名")
    private String databaseName;

    @Schema(description = "集合名/Key 前缀")
    private String collectionName;

    @Schema(description = "向量维度")
    private Integer embeddingDimension;

    @Schema(description = "描述")
    private String remark;

    /**
     * 构建向量库连接参数，供运行时创建 VectorStore 使用
     */
    @JsonIgnore
    public VectorStoreCreateParam.VectorStoreConnectionParam getVectorStoreConnectionParam() {
        return new VectorStoreCreateParam.VectorStoreConnectionParam(
                host,
                port != null ? port : 0,
                username,
                password
        );
    }

}
