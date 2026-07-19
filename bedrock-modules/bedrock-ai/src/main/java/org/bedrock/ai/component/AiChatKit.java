package org.bedrock.ai.component;

import org.bedrock.ai.factory.documet.DocumentReaderFactory;
import org.bedrock.ai.factory.transformer.TransformerFactory;
import org.bedrock.ai.param.send.AiImageParam;
import org.bedrock.ai.support.ModelOptionsSupport;
import org.bedrock.ai.vo.AiChatRecordDetailVO;
import org.bedrock.ai.vo.AiModelCheckVO;
import org.bedrock.common.ai.advisor.ChatHistoryAdvisor;
import org.bedrock.common.ai.advisor.KnowledgeRetrievalAdvisor;
import org.bedrock.common.ai.advisor.PromptAugmentationAdvisor;
import org.bedrock.common.ai.advisor.TokenUsageStatisticsAdvisor;
import org.bedrock.common.ai.enums.AiPlatformEnum;
import org.bedrock.common.ai.mcp.McpManager;
import org.bedrock.common.ai.tool.ToolManager;
import org.springframework.ai.chat.client.advisor.ToolCallAdvisor;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * AI 聊天基础设施聚合器。
 * <p>
 * 将聊天链路中常用的 Advisor、Options 等 Bean 集中注入，
 * 避免 Service 层构造函数参数过多；同时提供简短的方法名便于调用。
 * </p>
 *
 * @param historyAdvisor    多轮上下文 Advisor
 * @param toolCallAdvisor   工具调用 Advisor
 * @param knowledgeAdvisor  知识检索 Advisor
 * @param promptAdvisor     提示词增强 Advisor
 * @param tokenUsageAdvisor Token 用量统计 Advisor
 * @param modelOptions      各平台 ChatOptions 构建器
 * @param toolManager       各平台工具管理器
 * @param mcpManager        MCP 客户端管理器
 * @param transformer       文档拆分工厂
 * @param documentReader    文档读取器
 */
@Component
public record AiChatKit(ChatHistoryAdvisor historyAdvisor,
                        KnowledgeRetrievalAdvisor knowledgeAdvisor,
                        PromptAugmentationAdvisor promptAdvisor,
                        ToolCallAdvisor toolCallAdvisor,
                        TokenUsageStatisticsAdvisor tokenUsageAdvisor,
                        ModelOptionsSupport modelOptions,
                        ToolManager toolManager,
                        McpManager mcpManager,
                        TransformerFactory transformer,
                        DocumentReaderFactory documentReader
) {

    /**
     * 按平台与会话配置构建 ChatOptions。
     */
    public ChatOptions getChatOptions(AiPlatformEnum platform,
                                      AiChatRecordDetailVO chatRecord,
                                      AiModelCheckVO modelCheckVO,
                                      List<ToolCallback> callbacks,
                                      Map<String, Object> context) {
        return modelOptions.getChatOptions(platform, chatRecord, modelCheckVO, callbacks, context);
    }

    /**
     * 按平台与会话配置构建 ChatOptions。
     */
    public ImageOptions getImageOptions(AiPlatformEnum platform,
                                        AiModelCheckVO modelCheckVO,
                                        AiImageParam param) {
        return modelOptions.getImageOptions(platform, modelCheckVO, param);
    }

}
