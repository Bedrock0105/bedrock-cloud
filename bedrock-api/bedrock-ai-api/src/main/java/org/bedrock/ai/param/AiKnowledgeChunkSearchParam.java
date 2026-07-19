package org.bedrock.ai.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.ai.dto.AiChatOptions;

@Data
@Schema
public class AiKnowledgeChunkSearchParam {

    @Schema(description = "知识库检索参数")
    private AiChatOptions.KnowledgeParam knowledgeParam;

    @Schema(description = "查询内容")
    private String query;
}
