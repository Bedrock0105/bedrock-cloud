package org.bedrock.ai.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.EqualsAndHashCode;
import org.bedrock.ai.enums.DocSourceType;
import org.bedrock.ai.enums.DocumentSliceModeEnum;
import org.bedrock.common.tenant.base.TenantEntity;

import java.io.Serial;

/**
 * 知识库-文档元数据表
 *
 * @TableName bedrock_ai_knowledge_doc
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bedrock_ai_knowledge_doc")
@Schema(description = "知识库-文档元数据表")
public class AiKnowledgeDoc extends TenantEntity {

    @Serial
    private final static long serialVersionUID = 1L;

    /**
     * 关联知识库主键，关联 bedrock_ai_knowledge.id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "关联知识库主键，关联 bedrock_ai_knowledge.id")
    private Long knowledgeId;

    /**
     * 文档标题/文件名
     */
    @Schema(description = "文档标题/文件名")
    private String docTitle;

    /**
     * 来源类型：UPLOAD_FILE本地上传、MANUAL_TEXT手动录入
     */
    @Schema(description = "来源类型：UPLOAD_FILE本地上传、MANUAL_TEXT手动录入")
    private DocSourceType docSourceType;

    /**
     * OSS文件存储路径
     */
    @Schema(description = "OSS文件存储路径")
    private String fileUrl;

    /**
     * 源文件字节大小
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "源文件字节大小")
    private Long fileSize;

    /**
     * 文件后缀：pdf/docx/markdown/txt
     */
    @Schema(description = "文件后缀：pdf/docx/markdown/txt")
    private String fileSuffix;

    /**
     * 分段分片模式（分片算法策略）
     */
    @Schema(description = "分段分片模式（分片算法策略）")
    private DocumentSliceModeEnum sliceMode;

    /**
     * 文档被检索召回总次数
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "文档被检索召回总次数")
    private Long recallCount;

    /**
     * 配置状态（1=启用，0=禁用）
     */
    @Schema(description = "配置状态（1=启用，0=禁用）")
    private Integer status;

}
