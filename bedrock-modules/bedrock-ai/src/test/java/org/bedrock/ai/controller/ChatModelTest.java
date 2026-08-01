package org.bedrock.ai.controller;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.api.DashScopeImageApi;
import com.alibaba.cloud.ai.dashscope.embedding.text.DashScopeEmbeddingModel;
import com.alibaba.cloud.ai.dashscope.embedding.text.DashScopeEmbeddingOptions;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageModel;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import com.alibaba.cloud.ai.dashscope.spec.DashScopeApiSpec;
import com.alibaba.cloud.ai.dashscope.spec.DashScopeModel;
import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.EncodingType;
import com.knuddels.jtokkit.api.IntArrayList;
import org.bedrock.ai.AIApplication;
import org.bedrock.ai.dto.UserMessageAttachment;
import org.bedrock.ai.entity.AiChatMessage;
import org.bedrock.ai.mapper.AiChatMessageMapper;
import org.bedrock.common.code.util.JsonUtil;
import org.bedrock.common.code.util.StringUtil;
import org.bedrock.common.test.BedrockTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.ai.deepseek.api.DeepSeekApi;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@BedrockTest(enableStartConfiguration = true, classes = AIApplication.class)
public class ChatModelTest {

    private ChatClient chatClient;
    @Autowired
    private AiChatMessageMapper aiChatMessage;

    @BeforeEach
    public void setChatClient() {
//        this.chatClient = ChatClient.builder(
//                DeepSeekChatModel.builder()
//                        .deepSeekApi(DeepSeekApi.builder().apiKey("").build())
//                        .build()
//        ).build();
    }

    @Test
    void test1() {
        this.chatClient
                .prompt("写一个自我介绍")
                .system("ds")
                .system("s")
                .system("dss")
                .advisors()
                .options(DeepSeekChatOptions.builder().maxTokens(1000).build())
                .stream()
                .chatClientResponse()
                .toIterable().forEach(k -> {
                    System.out.println(k);
                    System.out.println(k.chatResponse().getMetadata().getUsage());
                });

        final EncodingRegistry registry = Encodings.newLazyEncodingRegistry();
//
        final Encoding encoding = registry.getEncoding(EncodingType.CL100K_BASE);
        int countTokens = encoding.countTokens("写一个自我介绍aa");
        final Encoding encoding1 = registry.getEncoding(EncodingType.O200K_BASE);
        System.out.println(countTokens);
        countTokens = encoding.countTokens("写一个自我介绍aa");
        System.out.println(countTokens);
        List<Integer> boxed = encoding.encode("写一个自我介绍").boxed();
        System.out.println(boxed);
        ChatClientResponse chatClientResponse = this.chatClient.prompt("写一个自我介绍")
                .call()
                .chatClientResponse();
        System.out.println(chatClientResponse.chatResponse().getMetadata().getUsage());
        int i = encoding.countTokens(chatClientResponse.chatResponse().getResult().getOutput().getText());
//        System.out.println(i);
//        System.out.println(encoding.countTokens("写一个自我介绍"));
    }

    @Test
    void test2() {
        AssistantMessage.ToolCall toolCall = new AssistantMessage.ToolCall("tool_call_id", "function_name", "ss","s");
        List<AssistantMessage.ToolCall> toolCall1 = List.of(toolCall);
        System.out.println(JsonUtil.toJson(toolCall1));
        List<AssistantMessage.ToolCall> calls = JsonUtil.parseList(JsonUtil.toJson(toolCall1), AssistantMessage.ToolCall.class);
        System.out.println(calls);
    }
    @Test
    void test3() {
        DashScopeApi.Builder builder = DashScopeApi
                .builder()
                .apiKey("");
        DashScopeEmbeddingModel build = DashScopeEmbeddingModel.builder()
                .dashScopeApi(builder.build())
                .defaultOptions(DashScopeEmbeddingOptions
                        .builder()
                        .textType(DashScopeApi.DEFAULT_EMBEDDING_TEXT_TYPE)
                        .model("text-embedding-v3")
                        .build())
                .build();
        List<float[]> embed = build.embed(List.of("写一个自我介绍"));
        System.out.println(embed);
    }
    @Test
    void test4() {
        var dashScopeImageApi = DashScopeImageApi.builder()
                .apiKey("")
                .build();

        var dashScopeImageModel = new DashScopeImageModel(dashScopeImageApi);
        DashScopeImageOptions imageOptions = DashScopeImageOptions.builder()
                .model(DashScopeModel.ImageModel.QWEN_IMAGE.getValue())
                .n(2)
                .width(1024)
                .height(1024)
                .style("photography")
                .seed(12345)
                .build();

        ImagePrompt imagePrompt = new ImagePrompt("A beautiful sunset over the ocean", imageOptions);
        ImageResponse response = dashScopeImageModel.call(imagePrompt);
        System.out.println(response);
    }

    @Test
    void test5() {
        List<AiChatMessage> aiChatMessages = aiChatMessage.selectHistoryMessages(2078008222004531201L, 10);
        System.out.println(aiChatMessages);
//        List<UserMessageAttachment> userMessageAttachments = new ArrayList<>(){{
//            add(new UserMessageAttachment("1", "https://example.com"));
//        }};
//        System.out.println(JsonUtil.toJson(userMessageAttachments));
    }
}
