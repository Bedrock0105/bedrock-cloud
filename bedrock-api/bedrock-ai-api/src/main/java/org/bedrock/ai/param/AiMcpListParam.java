package org.bedrock.ai.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.common.ai.mcp.McpManager.McpClientParameters.TransportType;

import java.io.Serial;
import java.io.Serializable;

/**
 * AI MCP 列表查询参数
 */
@Data
public class AiMcpListParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "业务名称")
    private String name;

    @Schema(description = "传输类型")
    private TransportType type;

    @Schema(description = "配置状态（1=启用，0=禁用）")
    private Integer status;

}
