package org.bedrock.ai.param;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 知识库文档提交参数
 */
@Data
public class AiKnowledgeDocCreateParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "知识库 id")
    private Long knowledgeId;
    /**
     * 文档标题/文件名
     */
    @Schema(description = "文档标题/文件名")
    private String docTitle;

}
