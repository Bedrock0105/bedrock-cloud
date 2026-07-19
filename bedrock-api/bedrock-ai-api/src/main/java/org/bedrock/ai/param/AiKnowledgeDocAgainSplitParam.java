package org.bedrock.ai.param;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 知识库文档重新拆分参数
 * <p>文档重新拆分时使用，包含文档 id 与分片拆分参数</p>
 */
@Data
public class AiKnowledgeDocAgainSplitParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "文档 id")
    private Long docId;

    @Schema(description = "文档分段参数")
    private AiKnowledgeDocSeparateParam separateParam;

}
