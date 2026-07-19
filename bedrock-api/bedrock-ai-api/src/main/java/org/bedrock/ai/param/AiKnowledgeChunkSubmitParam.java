package org.bedrock.ai.param;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 知识库文档分片提交参数
 */
@Data
public class AiKnowledgeChunkSubmitParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键,编辑的时候有值")
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键")
    private Long docId;

    @Schema(description = "分片纯文本内容")
    private String chunkContent;

}
