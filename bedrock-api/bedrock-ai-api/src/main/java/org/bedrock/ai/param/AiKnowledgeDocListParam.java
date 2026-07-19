package org.bedrock.ai.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 知识库文档列表查询参数
 */
@Data
public class AiKnowledgeDocListParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "知识库 id")
    private Long knowledgeId;

    @Schema(description = "文档标题，模糊匹配")
    private String docTitle;

    @Schema(description = "来源类型：UPLOAD_FILE、MANUAL_TEXT")
    private String docSourceType;

    @Schema(description = "配置状态（1=启用，0=禁用）")
    private Integer status;

}
