package org.bedrock.ai.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.bedrock.ai.vo.AiModelDetailVO;
import org.bedrock.ai.vo.AiRoleDetailVO;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 对话调用配置（温度、工具、知识库检索等），以 JSON 存储于 {@code chat_options} 列。
 */
@Getter
@Setter
@Schema(description = "对话调用配置")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AiChatOptions implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "温度参数，控制回复随机性，0~1 之间")
    private Double temperature;

    @Schema(description = "单条回复最大 Token 数量")
    private Integer maxTokens;

    @Schema(description = "上下文最大消息数量（来回算一条）")
    private Integer maxMessages;

    @Schema(description = "关联工具组名称")
    private List<String> tools;

    @Schema(description = "关联 MCP 配置 id（字符串，对应 bedrock_ai_mcp.id）")
    private List<String> mcpIds;

    @Schema(description = "关联知识库检索参数")
    private List<KnowledgeParam> knowledgeParams;

    /**
     * 从模型默认配置初始化会话级选项。
     */
    public static AiChatOptions fromModel(AiModelDetailVO model) {
        AiChatOptions options = new AiChatOptions();
        if (model == null) {
            return options;
        }
        options.setTemperature(model.getTemperature());
        options.setMaxTokens(model.getMaxTokens());
        options.setMaxMessages(model.getMaxMessages());
        return options;
    }

    /**
     * 从角色配置复制工具与知识库检索参数（不含温度、最大消息数）。
     */
    public static AiChatOptions fromRole(AiRoleDetailVO role) {
        if (role == null || role.getChatOptions() == null) {
            return new AiChatOptions();
        }
        return role.getChatOptions();
    }

    /**
     * 合并模型默认值：仅当会话选项未设置时回填温度、Token、消息数上限。
     */
    public AiChatOptions mergeModelDefaults(AiModelDetailVO model) {
        if (model == null) {
            return this;
        }
        if (temperature == null) {
            temperature = model.getTemperature();
        }
        if (maxTokens == null) {
            maxTokens = model.getMaxTokens();
        }
        if (maxMessages == null) {
            maxMessages = model.getMaxMessages();
        }
        return this;
    }

    /**
     * 知识库检索参数
     * 本来是 记录类但是Protostuff不支持
     */
    @Getter
    @Setter
    public static class KnowledgeParam {

        public KnowledgeParam(String knowledgeId, Double similarity, Integer topK) {
            this.knowledgeId = knowledgeId;
            this.similarity = similarity;
            this.topK = topK;
        }

        @Schema(description = "知识库 id")
        private String knowledgeId;

        @Schema(description = "相似度阈值")
        private Double similarity;

        @Schema(description = "TopK")
        private Integer topK;

        public String knowledgeId() {
            return this.knowledgeId;
        }

        public Double similarity() {
            return this.similarity;
        }

        public Integer topK() {
            return this.topK;
        }
    }

}
