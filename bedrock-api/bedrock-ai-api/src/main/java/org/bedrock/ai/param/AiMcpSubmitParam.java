package org.bedrock.ai.param;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.common.ai.mcp.McpManager.McpClientParameters.TransportType;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * AI MCP 提交参数
 */
@Data
public class AiMcpSubmitParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "业务名称（创建后不可修改）")
    private String name;

    @Schema(description = "传输类型：SSE / STREAMABLE_HTTP / STDIO")
    private TransportType type;

    @Schema(description = "HTTP baseUrl（SSE / StreamableHTTP）")
    private String url;

    @Schema(description = "HTTP endpoint")
    private String endpoint;

    @Schema(description = "可选 HTTP 请求头")
    private Map<String, String> headers;

    @Schema(description = "STDIO：Claude Desktop 格式 JSON")
    private String stdioServersJson;

    @Schema(description = "MCP client 展示名")
    private String clientName;

    @Schema(description = "客户端版本")
    private String version;

    @Schema(description = "请求超时秒数")
    private Integer requestTimeoutSeconds;

    @Schema(description = "描述")
    private String remark;

}
