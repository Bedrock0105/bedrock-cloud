package org.bedrock.ai.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bedrock.common.ai.mcp.McpManager.McpClientParameters.TransportType;
import org.bedrock.common.tenant.base.TenantEntity;

import java.io.Serial;
import java.util.Map;

/**
 * AI MCP（Model Context Protocol）客户端配置
 * <p>
 * 业务 {@code name} 创建后不可变；运行时以主键 id 字符串作为 {@link org.bedrock.common.ai.mcp.McpManager} 注册 ID。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "bedrock_ai_mcp", autoResultMap = true)
@Schema(description = "AI MCP 配置")
public class AiMcp extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "业务名称（不可变，用于 NamedTransport / Implementation）")
    private String name;

    @Schema(description = "传输类型：SSE / STREAMABLE_HTTP / STDIO")
    private TransportType type;

    @Schema(description = "HTTP baseUrl（SSE / StreamableHTTP）")
    private String url;

    @Schema(description = "HTTP endpoint；SSE 默认 /sse，StreamableHTTP 默认 /mcp")
    private String endpoint;

    @Schema(description = "可选 HTTP 请求头")
    @TableField(value = "headers", typeHandler = JacksonTypeHandler.class)
    private Map<String, String> headers;

    @Schema(description = "STDIO：Claude Desktop 格式 JSON")
    private String stdioServersJson;

    @Schema(description = "MCP client 展示名，空则用默认")
    private String clientName;

    @Schema(description = "客户端版本")
    private String version;

    @Schema(description = "请求超时秒数；<=0 时用默认 30")
    private Integer requestTimeoutSeconds;

    @Schema(description = "配置状态（1=启用，0=禁用）")
    private Integer status;

    @Schema(description = "描述")
    private String remark;

}
