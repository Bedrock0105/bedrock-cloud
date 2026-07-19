package org.bedrock.ai.config;

import lombok.RequiredArgsConstructor;
import org.bedrock.ai.advisor.prompt.AiPromptAugmenter;
import org.bedrock.common.ai.advisor.ChatHistoryAdvisor;
import org.bedrock.common.ai.advisor.KnowledgeRetrievalAdvisor;
import org.bedrock.common.ai.advisor.PromptAugmentationAdvisor;
import org.bedrock.common.ai.advisor.TokenUsageStatisticsAdvisor;
import org.bedrock.common.ai.advisor.history.ChatHistoryStore;
import org.bedrock.common.ai.advisor.knowledge.KnowledgeRetriever;
import org.bedrock.common.ai.advisor.usage.TokenUsageRecorder;
import org.springframework.ai.chat.client.advisor.ToolCallAdvisor;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@RequiredArgsConstructor
public class AiConfiguration {

    private final ChatHistoryStore chatHistoryStore;

    private final ToolCallingManager toolCallingManager;

    private final KnowledgeRetriever knowledgeRetriever;

    private final AiPromptAugmenter aiPromptAugmenter;

    /**
     * 聊天历史 Advisor：注入多轮上下文并持久化消息。
     */
    @Bean
    public ChatHistoryAdvisor chatHistoryAdvisor() {
        return ChatHistoryAdvisor
                .builder(chatHistoryStore)
                .build();
    }

    /**
     * 知识库检索 Advisor：仅负责向量召回，不直接修改 prompt。
     */
    @Bean
    public KnowledgeRetrievalAdvisor knowledgeRetrievalAdvisor() {
        return KnowledgeRetrievalAdvisor
                .builder(knowledgeRetriever)
                .build();
    }

    /**
     * 提示词增强 Advisor：向提示词中注入 知识库检索结果。
     */
    @Bean
    public PromptAugmentationAdvisor promptAugmentationAdvisor() {
        return PromptAugmentationAdvisor
                .builder()
                .promptAugmenter(aiPromptAugmenter)
                .build();
    }

    /**
     * 工具调用 Advisor：Agent 场景下执行 function calling。
     */
    @Bean
    public ToolCallAdvisor toolCallAdvisor() {
        return ToolCallAdvisor
                .builder()
                .disableMemory()
                .toolCallingManager(toolCallingManager)
                .build();
    }

    /**
     * Token 用量统计 Advisor：每次模型调用结束后记录 token 与耗时。
     * <p>
     * order 较大（靠近模型），在 ChatHistoryAdvisor 之后执行；
     * 实现类 {@link org.bedrock.ai.service.impl.AiTokenUsageServiceImpl} 负责持久化。
     * </p>
     */
    @Bean
    public TokenUsageStatisticsAdvisor tokenUsageStatisticsAdvisor(TokenUsageRecorder tokenUsageRecorder) {
        return TokenUsageStatisticsAdvisor.builder(tokenUsageRecorder)
                .build();
    }

    @Bean("aiExecutor")
    public Executor aiExecutor(TaskDecorator taskDecorator) {
        ThreadPoolTaskExecutor aiExecutor = new ThreadPoolTaskExecutor();
        aiExecutor.setCorePoolSize(200);
        aiExecutor.setMaxPoolSize(500);
        aiExecutor.setQueueCapacity(1500);
        aiExecutor.setKeepAliveSeconds(60);
        aiExecutor.setTaskDecorator(taskDecorator);
        aiExecutor.setThreadNamePrefix("aiExecutor--");
        aiExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        aiExecutor.setWaitForTasksToCompleteOnShutdown(true);
        aiExecutor.setAwaitTerminationSeconds(60);
        aiExecutor.initialize();
        return aiExecutor;
    }
}
