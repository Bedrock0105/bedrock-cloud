package org.bedrock.ai.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.common.ai.mcp.McpManager.McpClientParameters.TransportType;

import java.io.Serial;
import java.io.Serializable;

/**
 * AI MCP 列表 VO
 */
@Data
@Schema(description = "AI MCP 列表 VO")
public class AiMcpListVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "业务名称")
    private String name;

    @Schema(description = "传输类型")
    private TransportType type;

    @Schema(description = "HTTP baseUrl")
    private String url;

    @Schema(description = "配置状态（1=启用，0=禁用）")
    private Integer status;

    @Schema(description = "描述")
    private String remark;

}
