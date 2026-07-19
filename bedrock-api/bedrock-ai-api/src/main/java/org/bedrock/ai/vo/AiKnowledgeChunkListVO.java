package org.bedrock.ai.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 知识库文档分片列表 VO
 */
@Data
@Schema(description = "知识库文档分片列表 VO")
public class AiKnowledgeChunkListVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键")
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "知识库 id")
    private Long knowledgeId;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "文档 id")
    private Long docId;

    @Schema(description = "文档标题")
    private String docTitle;

    @Schema(description = "文档内分片序号")
    private Integer chunkNo;

    @Schema(description = "分片内容摘要（列表截断展示）")
    private String chunkContent;

    @Schema(description = "本段文本预估 token 数量")
    private Integer chunkTokenCount;

    @Schema(description = "向量库内对应向量唯一 ID")
    private String vectorId;

    @Schema(description = "向量化状态：0待向量化、1入库成功、2向量写入失败")
    private Integer embedStatus;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "当前分片被检索召回总次数")
    private Long recallCount;

    @Schema(description = "配置状态（1=启用，0=禁用）")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
