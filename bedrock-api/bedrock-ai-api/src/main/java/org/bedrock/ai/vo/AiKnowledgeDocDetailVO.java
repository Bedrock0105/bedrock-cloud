package org.bedrock.ai.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bedrock.ai.enums.DocSourceType;
import org.bedrock.ai.enums.DocumentSliceModeEnum;
import org.bedrock.common.code.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 知识库文档详情 VO
 */
@Data
@Schema(description = "知识库文档详情 VO")
public class AiKnowledgeDocDetailVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键")
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "知识库 id")
    private Long knowledgeId;

    @Schema(description = "文档标题/文件名")
    private String docTitle;

    @Schema(description = "来源类型：UPLOAD_FILE、MANUAL_TEXT")
    private DocSourceType docSourceType;

    @Schema(description = "OSS 文件存储路径")
    private String fileUrl;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "源文件字节大小")
    private Long fileSize;

    @Schema(description = "文件后缀")
    private String fileSuffix;

    @Schema(description = "分段分片模式")
    private DocumentSliceModeEnum sliceMode;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "文档被检索召回总次数")
    private Long recallCount;

    @Schema(description = "配置状态（1=启用，0=禁用）")
    private Integer status;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    private LocalDateTime createTime;

}
