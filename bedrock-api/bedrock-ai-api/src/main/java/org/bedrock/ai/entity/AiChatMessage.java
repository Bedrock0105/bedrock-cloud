package org.bedrock.ai.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bedrock.ai.dto.UserMessageAttachment;
import org.bedrock.common.code.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * AI 聊天消息明细（数据库实体，对应 bedrock_ai_chat_message 表）
 * <p>与 {@link org.bedrock.ai.vo.AiChatSendVO}（发送响应）、
 * {@link org.bedrock.ai.vo.AiChatMessageListVO}（历史列表）职责不同，请勿混用</p>
 *
 */
@Data
@TableName(value = "bedrock_ai_chat_message", autoResultMap = true)
@Schema(description = "AI聊天消息明细表")
public class AiChatMessage implements Serializable {

    @Serial
    private final static long serialVersionUID = 1L;

    /**
     * 主键
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private Long id;

    /**
     * 关联bedrock_ai_chat_record主表主键id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "关联bedrock_ai_chat_record主表主键id")
    private Long recordId;

    /**
     * 操作人用户ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "操作人用户ID")
    private Long userId;

    /**
     * 父消息 ID；
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "父消息 ID；")
    private Long parentId;

    /**
     * 使用的AI模型ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "使用的AI模型ID")
    private Long modelId;

    /**
     * 模型标识
     */
    @Schema(description = "模型标识")
    private String model;

    /**
     * 消息角色：user用户 / assistant模型 / system系统 / tool工具
     * 枚举 {@link org.springframework.ai.chat.messages.MessageType}
     */
    @Schema(description = "消息角色：user用户 / assistant模型 / system系统 / tool工具")
    private String role;

    /**
     * 消息内容
     */
    @Schema(description = "消息内容")
    private String content;

    /**
     * 推理/思考过程内容（DeepSeek 等 reasoning 模型），可为空
     */
    @Schema(description = "推理/思考过程内容（DeepSeek 等 reasoning 模型），可为空")
    private String reasoning;

    /**
     * assistant 工具调用列表 JSON（{@link org.springframework.ai.chat.messages.AssistantMessage.ToolCall}）
     */
    @Schema(description = "assistant 工具调用列表 JSON")
    private String toolCalls;

    /**
     * 知识库分片 ID 列表（列类型 text，存 JSON 串；RAG 正文不进历史，仅备查）
     */
    @Schema(description = "知识库分片ID列表")
    @TableField(value = "chunk_ids", typeHandler = JacksonTypeHandler.class)
    private List<String> chunkIds;

    /**
     * 用户消息附件列表（列类型 text，存 JSON 串；历史 get 时再解析为 Media / 正文）
     */
    @Schema(description = "用户消息附件列表")
    @TableField(value = "attachments", typeHandler = JacksonTypeHandler.class)
    private List<UserMessageAttachment> attachments;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 是否删除
     */
    @Schema(description = "是否删除")
    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    private Integer isDeleted;

}
