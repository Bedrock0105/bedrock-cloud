package org.bedrock.ai.support;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import org.bedrock.ai.dto.AiChatOptions;
import org.bedrock.ai.param.send.AiImageParam;
import org.bedrock.ai.vo.AiChatRecordDetailVO;
import org.bedrock.ai.vo.AiModelCheckVO;
import org.bedrock.common.ai.enums.AiPlatformEnum;
import org.bedrock.common.code.util.StringUtil;
import org.springaicommunity.moonshot.MoonshotChatOptions;
import org.springframework.ai.anthropic.AnthropicChatOptions;
import org.springframework.ai.azure.openai.AzureOpenAiChatOptions;
import org.springframework.ai.azure.openai.AzureOpenAiImageOptions;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.minimax.MiniMaxChatOptions;
import org.springframework.ai.mistralai.MistralAiChatOptions;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.zhipuai.ZhiPuAiChatOptions;
import org.springframework.ai.zhipuai.ZhiPuAiImageOptions;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 各 AI 平台 ChatOptions / ImageOptions 构建器。
 * <p>
 * 平台与 {@link org.bedrock.common.ai.factory.model.AiModelBuilder} 实现一一对应；
 * 图像能力仅覆盖已实现 {@code createImageModel} 的厂商，其余平台抛出异常。
 * </p>
 */
@Component
public class ModelOptionsSupport {

    /**
     * 按平台创建对应的 ChatOptions。
     */
    public ChatOptions getChatOptions(AiPlatformEnum platform,
                                      AiChatRecordDetailVO chatRecord,
                                      AiModelCheckVO modelCheckVO,
                                      List<ToolCallback> callbacks,
                                      Map<String, Object> context) {
        String model = modelCheckVO.getModel();
        AiChatOptions chatOptions = chatRecord.getChatOptions();
        Integer maxTokens = chatOptions.getMaxTokens();
        Double temperature = chatOptions.getTemperature();
        List<ToolCallback> toolCallbacks = new ArrayList<>(callbacks);
        return switch (platform) {
            // DeepSeekAiModelBuilder
            case DEEP_SEEK -> DeepSeekChatOptions.builder()
                    .model(model)
                    .maxTokens(maxTokens)
                    .temperature(temperature)
                    .toolCallbacks(toolCallbacks)
                    .toolContext(context)
                    .build();
            // DashScopeAiModelBuilder
            case DASH_SCOPE -> DashScopeChatOptions.builder()
                    .model(model)
                    .maxToken(maxTokens)
                    .temperature(temperature)
                    .toolCallbacks(toolCallbacks)
                    .toolContext(context)
                    .build();
            // MiniMaxAiModelBuilder
            case MINIMAX -> MiniMaxChatOptions.builder()
                    .model(model)
                    .maxTokens(maxTokens)
                    .temperature(temperature)
                    .toolCallbacks(toolCallbacks)
                    .toolContext(context)
                    .build();
            // MoonshotAiModelBuilder
            case MOONSHOT -> MoonshotChatOptions.builder()
                    .model(model)
                    .maxTokens(maxTokens)
                    .temperature(temperature)
                    .toolCallbacks(toolCallbacks)
                    .toolContext(context)
                    .build();
            // ZhiPuAiModelBuilder
            case ZHI_PU -> ZhiPuAiChatOptions.builder()
                    .model(model)
                    .maxTokens(maxTokens)
                    .temperature(temperature)
                    .toolCallbacks(toolCallbacks)
                    .toolContext(context)
                    .build();
            // AnthropicAiModelBuilder
            case ANTHROPIC -> AnthropicChatOptions.builder()
                    .model(model)
                    .maxTokens(maxTokens)
                    .temperature(temperature)
                    .toolCallbacks(toolCallbacks)
                    .toolContext(context)
                    .build();
            // AzureAiModelBuilder
            case AZURE_OPENAI -> AzureOpenAiChatOptions.builder()
                    .deploymentName(model)
                    .maxTokens(maxTokens)
                    .temperature(temperature)
                    .toolCallbacks(toolCallbacks)
                    .toolContext(context)
                    .build();
            // MistralAiModelBuilder
            case MISTRAL -> MistralAiChatOptions.builder()
                    .model(model)
                    .maxTokens(maxTokens)
                    .temperature(temperature)
                    .toolCallbacks(toolCallbacks)
                    .toolContext(context)
                    .build();
            // OllamaAiModelBuilder
            case OLLAMA -> OllamaChatOptions.builder()
                    .model(model)
                    .numPredict(maxTokens)
                    .temperature(temperature)
                    .toolCallbacks(toolCallbacks)
                    .toolContext(context)
                    .build();
            // OpenAI 兼容协议
            case OPENAI, QIAN_FAN, HUNYUAN, SPARK, BAICHUAN, VOLCENGINE, YI, SILICON_FLOW, GEMINI, COHERE, XAI ->
                    OpenAiChatOptions.builder()
                            .model(model)
                            .maxTokens(maxTokens)
                            .temperature(temperature)
                            .toolCallbacks(toolCallbacks)
                            .toolContext(context)
                            .build();
        };
    }

    /**
     * 按平台创建对应的 ImageOptions。
     * <p>仅支持 {@link org.bedrock.common.ai.factory.model.AiModelBuilder#createImageModel} 已实现的厂商。</p>
     */
    public ImageOptions getImageOptions(AiPlatformEnum platform,
                                        AiModelCheckVO modelCheckVO,
                                        AiImageParam param) {
        return switch (platform) {
            case OPENAI, QIAN_FAN, VOLCENGINE, SILICON_FLOW, XAI ->
                    buildOpenAiImageOptions(modelCheckVO, param);
            case DASH_SCOPE -> buildDashScopeImageOptions(modelCheckVO, param);
            case ZHI_PU -> ZhiPuAiImageOptions.builder().model(modelCheckVO.getModel()).build();
            case AZURE_OPENAI -> buildAzureOpenAiImageOptions(modelCheckVO, param);
            default -> throw new UnsupportedOperationException(
                    "AI 平台[" + platform.getDescription() + "]暂不支持图片生成参数配置");
        };
    }

    private OpenAiImageOptions buildOpenAiImageOptions(AiModelCheckVO modelCheckVO, AiImageParam param) {
        OpenAiImageOptions.Builder builder = OpenAiImageOptions.builder()
                .model(modelCheckVO.getModel())
                .responseFormat(resolveResponseFormat(param.getExtra()));
        applyIfNotNull(param.getQuality(), builder::quality);
        applyIfNotNull(param.getWidth(), builder::width);
        applyIfNotNull(param.getHeight(), builder::height);
        applyIfNotNull(param.getNumber(), builder::N);
        applyIfNotNull(param.getStyle(), builder::style);
        return builder.build();
    }

    private DashScopeImageOptions buildDashScopeImageOptions(AiModelCheckVO modelCheckVO, AiImageParam param) {
        DashScopeImageOptions.Builder builder = DashScopeImageOptions.builder()
                .model(modelCheckVO.getModel())
                .responseFormat(resolveResponseFormat(param.getExtra()));
        applyIfNotNull(param.getWidth(), builder::width);
        applyIfNotNull(param.getHeight(), builder::height);
        applyIfNotNull(param.getNumber(), builder::n);
        applyIfNotNull(param.getStyle(), builder::style);
        Map<String, Object> extra = param.getExtra();
        applyExtra(extra, builder::seed, "seed", Integer.class);
        applyExtra(extra, builder::negativePrompt, "negativePrompt", String.class);
        applyExtra(extra, builder::refImg, "refImg", String.class);
        applyExtra(extra, builder::refMode, "refMode", String.class);
        applyExtra(extra, builder::refStrength, "refStrength", Float.class);
        applyExtra(extra, builder::watermark, "watermark", Boolean.class);
        applyExtra(extra, builder::promptExtend, "promptExtend", Boolean.class);
        applyExtra(extra, builder::baseImageUrl, "baseImageUrl", String.class);
        applyExtra(extra, builder::maskImageUrl, "maskImageUrl", String.class);
        applyExtra(extra, builder::sketchImageUrl, "sketchImageUrl", String.class);
        applyExtra(extra, builder::sketchWeight, "sketchWeight", Integer.class);
        applyExtra(extra, builder::sketchExtraction, "sketchExtraction", Boolean.class);
        applyExtra(extra, builder::maxImages, "maxImages", Integer.class);
        applyExtra(extra, builder::enableInterleave, "enableInterleave", Boolean.class);
        applyExtra(extra, builder::function, "function", String.class);
        return builder.build();
    }

    private AzureOpenAiImageOptions buildAzureOpenAiImageOptions(AiModelCheckVO modelCheckVO, AiImageParam param) {
        AzureOpenAiImageOptions.Builder builder = AzureOpenAiImageOptions.builder()
                .model(modelCheckVO.getModel())
                .deploymentName(modelCheckVO.getModel())
                .responseFormat(resolveResponseFormat(param.getExtra()));
        applyIfNotNull(param.getWidth(), builder::width);
        applyIfNotNull(param.getHeight(), builder::height);
        applyIfNotNull(param.getNumber(), builder::N);
        applyIfNotNull(param.getStyle(), builder::style);
        Map<String, Object> extra = param.getExtra();
        applyExtra(extra, builder::deploymentName, "deploymentName", String.class);
        return builder.build();
    }

    /**
     * 默认使用 b64_json，便于服务端统一下载/解码后上传 OSS。
     */
    private static String resolveResponseFormat(Map<String, Object> extra) {
        if (extra != null) {
            Object value = extra.get("responseFormat");
            if (value != null && StringUtil.isNotBlank(value.toString())) {
                return value.toString();
            }
        }
        return "b64_json";
    }


    private static <T> void applyIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

    private static <T> void applyExtra(Map<String, Object> extra, Consumer<T> setter, String key, Class<T> type) {
        if (extra == null || !extra.containsKey(key)) {
            return;
        }
        Object value = extra.get(key);
        if (value == null) {
            return;
        }
        if (type.isInstance(value)) {
            setter.accept(type.cast(value));
            return;
        }
        if (value instanceof Number number) {
            if (type == Integer.class) {
                setter.accept(type.cast(number.intValue()));
            }
            else if (type == Float.class) {
                setter.accept(type.cast(number.floatValue()));
            }
            else if (type == Long.class) {
                setter.accept(type.cast(number.longValue()));
            }
        }
    }
}
