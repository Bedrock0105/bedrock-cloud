package org.bedrock.ai.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 知识库文档分片详情 VO
 */
@Data
@Schema(description = "知识库文档分片详情 VO")
public class AiKnowledgeChunkDetailVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键")
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "知识库 id")
    private Long knowledgeId;

    @Schema(description = "知识库名称")
    private String knowledgeName;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "文档 id")
    private Long docId;

    @Schema(description = "文档标题")
    private String docTitle;


    @Schema(description = "分片纯文本内容")
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
