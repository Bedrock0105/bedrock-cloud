package org.bedrock.ai.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * AI 聊天发送响应（普通/流式发送接口专用，非 bedrock_ai_chat_message 表记录）
 * <p>
 * 该 VO 类用于封装 AI 聊天接口的响应数据。
 * 在流式（Streaming）场景下，{@link #content} 字段返回的是增量片段（即每次返回部分内容），
 * 而 {@link #id} 字段则代表整个聊天会话的唯一标识，用于前端拼接流式消息。
 * </p>
 *
 * @author （你的团队或作者名）
 * @since 1.0.0
 */
@Data
@Schema(description = "AI聊天发送响应")
public class AiChatSendVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 聊天会话 ID
     * <p>
     * 用于标识一次完整的聊天会话。
     * 在流式响应中，该 ID 保持不变，以便前端将多次增量返回的内容合并到同一条消息中。
     * </p>
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "聊天会话 id")
    private Long id;

    /**
     * 使用的 AI 模型 ID
     * <p>
     * 该 ID 对应后端系统中配置的模型主键，用于唯一标识一个模型版本。
     * </p>
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "使用的AI模型ID")
    private Long modelId;

    /**
     * 模型标识
     * <p>
     * 模型在系统中的唯一字符串标识，例如 "gpt-4"、"deepseek-chat" 等。
     * 通常用于前端展示或日志记录。
     * </p>
     */
    @Schema(description = "模型标识")
    private String model;

    /**
     * 模型名称
     * <p>
     * 模型的用户友好名称，例如 "GPT-4"、"DeepSeek Chat"。
     * 用于在前端界面中展示给用户。
     * </p>
     */
    @Schema(description = "模型名称")
    private String modelName;

    /**
     * 返回的聊天内容
     * <p>
     * 在普通（非流式）响应中，该字段包含完整的回复内容。
     * 在流式响应中，该字段包含的是当前返回的增量片段（即部分内容），
     * 前端需要将所有增量片段按顺序拼接，才能得到完整的回复。
     * </p>
     */
    @Schema(description = "返回的聊天内容（流式为增量片段）")
    private String content;

    /**
     * 创建一个新的 Builder 实例
     *
     * @return Builder 对象，用于链式构建 AiChatSendVO
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * AiChatSendVO 的构建器类
     * <p>
     * 使用 Builder 模式可以更清晰、灵活地构建 AiChatSendVO 对象，
     * 避免使用多个重载构造函数或大量 setter 调用。
     * </p>
     */
    public static class Builder {

        /**
         * 聊天会话 ID
         */
        private Long id;

        /**
         * 使用的 AI 模型 ID
         */
        private Long modelId;

        /**
         * 模型标识
         */
        private String model;

        /**
         * 模型名称
         */
        private String modelName;

        /**
         * 返回的聊天内容
         */
        private String content;

        /**
         * 设置聊天会话 ID
         *
         * @param id 聊天会话 ID
         * @return 当前 Builder 实例（用于链式调用）
         */
        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        /**
         * 设置 AI 模型 ID
         *
         * @param modelId AI 模型 ID
         * @return 当前 Builder 实例（用于链式调用）
         */
        public Builder modelId(Long modelId) {
            this.modelId = modelId;
            return this;
        }

        /**
         * 设置模型标识
         *
         * @param model 模型标识字符串
         * @return 当前 Builder 实例（用于链式调用）
         */
        public Builder model(String model) {
            this.model = model;
            return this;
        }

        /**
         * 设置模型名称
         *
         * @param modelName 模型名称
         * @return 当前 Builder 实例（用于链式调用）
         */
        public Builder modelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        /**
         * 设置聊天内容
         *
         * @param content 聊天内容（流式场景下为增量片段）
         * @return 当前 Builder 实例（用于链式调用）
         */
        public Builder content(String content) {
            this.content = content;
            return this;
        }

        /**
         * 构建 AiChatSendVO 对象
         * <p>
         * 将 Builder 中设置的所有属性填充到新的 AiChatSendVO 实例中。
         * </p>
         *
         * @return 构建完成的 AiChatSendVO 对象
         */
        public AiChatSendVO build() {
            AiChatSendVO vo = new AiChatSendVO();
            vo.setId(id);
            vo.setModelId(modelId);
            vo.setModel(model);
            vo.setModelName(modelName);
            vo.setContent(content);
            return vo;
        }
    }
}
