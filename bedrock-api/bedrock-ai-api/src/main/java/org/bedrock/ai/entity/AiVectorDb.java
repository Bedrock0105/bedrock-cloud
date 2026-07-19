package org.bedrock.ai.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bedrock.common.ai.enums.VectorStoreEnum;
import org.bedrock.common.tenant.base.TenantEntity;

import java.io.Serial;

/**
 * 向量数据库连接配置
 * <p>管理 Redis / Milvus / Elasticsearch / 本地 Simple 等向量库的连接信息，非运行时检索实例</p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bedrock_ai_vector_db")
@Schema(description = "向量数据库连接配置")
public class AiVectorDb extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 配置名称，同租户下唯一
     */
    @Schema(description = "配置名称")
    private String dbName;

    /**
     * 向量数据库类型：REDIS / MILVUS / ELASTICSEARCH / SIMPLE
     */
    @Schema(description = "向量数据库类型")
    private VectorStoreEnum vectorDbType;

    /**
     * 主机地址，支持单地址或逗号分隔多节点（Elasticsearch）
     */
    @Schema(description = "主机地址")
    private String host;

    /**
     * 服务端口
     */
    @Schema(description = "端口")
    private Integer port;

    /**
     * 认证用户名，无认证时可空
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 认证密码，无认证时可空
     */
    @Schema(description = "密码")
    private String password;

    /**
     * 逻辑库/索引名
     */
    @Schema(description = "逻辑库/索引名")
    private String databaseName;

    /**
     * 集合名/Key 前缀：Milvus → collectionName，Redis → prefix，Elasticsearch 不使用
     */
    @Schema(description = "集合名/Key 前缀")
    private String collectionName;

    /**
     * 向量维度，建索引时使用
     */
    @Schema(description = "向量维度")
    private Integer embeddingDimension;

    /**
     * 配置状态：1=启用，0=禁用；新增默认禁用
     */
    @Schema(description = "配置状态（1=启用，0=禁用）")
    private Integer status;

    /**
     * 描述信息
     */
    @Schema(description = "描述")
    private String remark;

}
