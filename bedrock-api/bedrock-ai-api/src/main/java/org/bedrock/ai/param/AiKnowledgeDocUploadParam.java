package org.bedrock.ai.param;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 知识库文档提交参数
 * <p>文件数据集上传入库时使用，包含知识库 id 与分片拆分参数</p>
 */
@Data
public class AiKnowledgeDocUploadParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "知识库 id")
    private Long knowledgeId;

    @Schema(description = "文档分段参数")
    private AiKnowledgeDocSeparateParam separateParam;

}
