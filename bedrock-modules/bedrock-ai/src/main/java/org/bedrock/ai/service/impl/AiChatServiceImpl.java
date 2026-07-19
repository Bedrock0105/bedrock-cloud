package org.bedrock.ai.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bedrock.ai.component.AiChatKit;
import org.bedrock.ai.constant.AiConstant;
import org.bedrock.ai.constant.PromptConstant;
import org.bedrock.ai.dto.AiChatOptions;
import org.bedrock.ai.dto.UserMessageAttachment;
import org.bedrock.ai.entity.AiChatMessage;
import org.bedrock.ai.enums.AiChatTypeEnum;
import org.bedrock.ai.enums.AiErrorEnum;
import org.bedrock.ai.param.send.AiArticleParam;
import org.bedrock.ai.param.send.AiChatSendParam;
import org.bedrock.ai.param.send.AiImageParam;
import org.bedrock.ai.param.send.AiMindmapParam;
import org.bedrock.ai.service.IAiChatMessageService;
import org.bedrock.ai.service.IAiChatRecordService;
import org.bedrock.ai.service.IAiChatService;
import org.bedrock.ai.service.IAiModelService;
import org.bedrock.ai.vo.AiChatRecordDetailVO;
import org.bedrock.ai.vo.AiChatSendVO;
import org.bedrock.ai.vo.AiImageResultVO;
import org.bedrock.ai.vo.AiImageResultVO.AiImageItemVO;
import org.bedrock.ai.vo.AiModelCheckVO;
import org.bedrock.common.mybatisplus.constant.BedrockDBConstant;
import org.springframework.ai.chat.messages.MessageType;
import org.bedrock.common.ai.advisor.ChatHistoryAdvisor;
import org.bedrock.common.ai.advisor.KnowledgeRetrievalAdvisor;
import org.bedrock.common.ai.advisor.PromptAugmentationAdvisor;
import org.bedrock.common.ai.advisor.TokenUsageStatisticsAdvisor;
import org.bedrock.common.ai.advisor.history.ChatHistoryStore;
import org.bedrock.common.auth.entity.AuthUser;
import org.bedrock.common.auth.util.AuthUtil;
import org.bedrock.common.code.api.R;
import org.bedrock.common.code.util.*;
import org.bedrock.common.log.exception.ServiceException;
import org.bedrock.common.resource.model.oss.OssResultFile;
import org.bedrock.resource.dto.OssMultipartFile;
import org.bedrock.resource.feign.IOssClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.ToolCallAdvisor;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImageGeneration;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.*;

/**
 * AI 对话执行服务实现。
 * <p>
 * 封装「校验 → 解析会话 → 构建 Prompt → 调用模型 → 组装响应」的通用对话链路，
 * 文本、图片、音频等不同模态可在此模块按类型扩展独立入口方法。
 * </p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiChatServiceImpl implements IAiChatService {

    private static final int TITLE_MAX_LENGTH = 50;

    private static final String DEFAULT_IMAGE_FILENAME = "ai-generated.png";

    private final IAiModelService aiModelService;

    private final IAiChatRecordService aiChatRecordService;

    private final IAiChatMessageService aiChatMessageService;

    private final AiChatKit chatKit;

    private final IOssClient ossClient;

    /**
     * 文本对话（同步）。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AiChatSendVO sendChar(AiChatSendParam param) {
        validateTextParam(param);
        AiModelCheckVO modelCheckVO = aiModelService.checkAiModel(param.getModelId());
        AiChatRecordDetailVO chatRecord = resolveChatRecord(param, modelCheckVO);
        ChatClient chatClient = aiModelService.getChatClient(modelCheckVO);
        String content = buildTextPrompt(chatClient, param.getContent(), chatRecord, modelCheckVO, param.getAttachments())
                .call()
                .content();
        return buildSendVO(chatRecord, modelCheckVO, content);
    }

    /**
     * 文本对话（流式）。
     */
    @Override
    public Flux<R<AiChatSendVO>> sendCharStream(AiChatSendParam param) {
        validateTextParam(param);
        AiModelCheckVO modelCheckVO = aiModelService.checkAiModel(param.getModelId());
        AiChatRecordDetailVO chatRecord = resolveChatRecord(param, modelCheckVO);
        ChatClient chatClient = aiModelService.getChatClient(modelCheckVO);
        ChatClient.ChatClientRequestSpec requestSpec = buildTextPrompt(chatClient, param.getContent(), chatRecord, modelCheckVO, param.getAttachments());
        return requestSpec.stream()
                .content()
                .map(chunk -> R.success(buildSendVO(chatRecord, modelCheckVO, chunk)));
    }

    /**
     * 思维导图生成（流式）。同分组挂 HistoryAdvisor，与普通聊天会话隔离。
     */
    @Override
    public Flux<R<AiChatSendVO>> generateMindmapStream(AiMindmapParam param) {
        validateMindmapParam(param);
        AiModelCheckVO modelCheckVO = aiModelService.checkAiModel(param.getModelId());
        AiChatRecordDetailVO chatRecord = resolveMindmapRecord(param, modelCheckVO);
        ChatClient chatClient = aiModelService.getChatClient(modelCheckVO);
        ChatClient.ChatClientRequestSpec requestSpec = buildTextPrompt(chatClient, param.getContent(), chatRecord, modelCheckVO);
        return requestSpec.stream()
                .content()
                .map(chunk -> R.success(buildSendVO(chatRecord, modelCheckVO, chunk)));
    }

    /**
     * 文章写作（流式）。同分组挂 HistoryAdvisor，与普通聊天会话隔离。
     */
    @Override
    public Flux<R<AiChatSendVO>> generateArticleStream(AiArticleParam param) {
        validateArticleParam(param);
        AiModelCheckVO modelCheckVO = aiModelService.checkAiModel(param.getModelId());
        AiChatRecordDetailVO chatRecord = resolveArticleRecord(param, modelCheckVO);
        ChatClient chatClient = aiModelService.getChatClient(modelCheckVO);
        String userContent = buildArticleUserContent(param);
        ChatClient.ChatClientRequestSpec requestSpec = buildTextPrompt(chatClient, userContent, chatRecord, modelCheckVO);
        return requestSpec.stream()
                .content()
                .map(chunk -> R.success(buildSendVO(chatRecord, modelCheckVO, chunk)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AiImageResultVO generateImage(AiImageParam param) {
        validateImageParam(param);
        AiModelCheckVO modelCheckVO = aiModelService.checkAiModel(param.getModelId());
        AiChatRecordDetailVO chatRecord = resolveImageRecord(param, modelCheckVO);

        ImageModel imageModel = aiModelService.getImageModel(modelCheckVO);
        ImageOptions imageOptions = chatKit.getImageOptions(modelCheckVO.getPlatform(), modelCheckVO, param);
        ImageResponse imageResponse = imageModel.call(new ImagePrompt(param.getContent(), imageOptions));

        List<AiImageItemVO> images = new ArrayList<>();
        if (CollectionUtil.isEmpty(imageResponse.getResults())) {
            images.add(AiImageItemVO.fail("模型未返回图片"));
        } else {
            for (ImageGeneration generation : imageResponse.getResults()) {
                images.add(uploadGeneratedImage(generation));
            }
        }
        saveImageMessages(chatRecord, modelCheckVO, param, images);
        return new AiImageResultVO(chatRecord.getId(), images);
    }

    /**
     * 解析或创建图片会话。
     */
    private AiChatRecordDetailVO resolveImageRecord(AiImageParam param, AiModelCheckVO modelCheckVO) {
        if (param.getRecordId() == null) {
            AiChatRecordDetailVO chatRecord = aiChatRecordService.createAndSaveChatRecord(
                    modelCheckVO, buildTitle(param.getContent()), null, null, null, AiChatTypeEnum.IMAGE);
            param.setRecordId(chatRecord.getId());
            return chatRecord;
        }
        AiChatRecordDetailVO chatRecord = aiChatRecordService.detail(param.getRecordId());
        if (chatRecord == null) {
            throw new ServiceException(AiErrorEnum.CHAT_RECORD_NOT_FOUND.getCode(),
                    AiErrorEnum.CHAT_RECORD_NOT_FOUND.getMessage());
        }
        if (!param.getModelId().equals(chatRecord.getModelId())) {
            aiChatRecordService.changeChatRecordModel(param.getRecordId(), param.getModelId());
            chatRecord.setModelId(param.getModelId());
        }
        return chatRecord;
    }

    /**
     * 持久化图片生成的 user / assistant 消息。
     */
    private void saveImageMessages(AiChatRecordDetailVO chatRecord,
                                   AiModelCheckVO modelCheckVO,
                                   AiImageParam param,
                                   List<AiImageItemVO> images) {
        Long userId = AuthUtil.getUserId();
        AiChatMessage userMessage = new AiChatMessage();
        userMessage.setRecordId(chatRecord.getId());
        userMessage.setUserId(userId);
        userMessage.setModelId(modelCheckVO.getId());
        userMessage.setModel(modelCheckVO.getModel());
        userMessage.setRole(MessageType.USER.getValue());
        userMessage.setContent(param.getContent());
        userMessage.setIsDeleted(BedrockDBConstant.DB_NOT_DELETED);
        aiChatMessageService.save(userMessage);

        Map<String, Object> assistantPayload = new LinkedHashMap<>();
        assistantPayload.put("images", images);
        assistantPayload.put("params", buildImageParamsSnapshot(param));

        AiChatMessage assistantMessage = new AiChatMessage();
        assistantMessage.setRecordId(chatRecord.getId());
        assistantMessage.setUserId(userId);
        assistantMessage.setParentId(userMessage.getId());
        assistantMessage.setModelId(modelCheckVO.getId());
        assistantMessage.setModel(modelCheckVO.getModel());
        assistantMessage.setRole(MessageType.ASSISTANT.getValue());
        assistantMessage.setContent(JsonUtil.toJson(assistantPayload));
        assistantMessage.setIsDeleted(BedrockDBConstant.DB_NOT_DELETED);
        aiChatMessageService.save(assistantMessage);
    }

    private Map<String, Object> buildImageParamsSnapshot(AiImageParam param) {
        Map<String, Object> snapshot = new LinkedHashMap<>();
        snapshot.put("modelId", param.getModelId() == null ? null : param.getModelId().toString());
        snapshot.put("content", param.getContent());
        snapshot.put("width", param.getWidth());
        snapshot.put("height", param.getHeight());
        snapshot.put("number", param.getNumber());
        snapshot.put("style", param.getStyle());
        snapshot.put("quality", param.getQuality());
        snapshot.put("extra", param.getExtra());
        return snapshot;
    }

    /**
     * 将模型输出上传到 OSS，返回单张图片结果。
     */
    private AiImageItemVO uploadGeneratedImage(ImageGeneration generation) {
        Image output = generation.getOutput();
        try {
            byte[] bytes = resolveImageBytes(output);
            if (bytes == null || bytes.length == 0) {
                return AiImageItemVO.fail("无法获取图片数据");
            }
            R<OssResultFile> uploadResult = ossClient.uploadFile(
                    OssMultipartFile.of(bytes, resolveImageFilename(output)));
            if (uploadResult.isSuccess()) {
                return AiImageItemVO.ok(uploadResult.getData().url());
            }
            return AiImageItemVO.fail(uploadResult.getMsg());
        } catch (IOException e) {
            log.error("读取图片数据失败", e);
            return AiImageItemVO.fail("读取图片数据失败: " + e.getMessage());
        } catch (Exception e) {
            log.error("图片上传失败", e);
            return AiImageItemVO.fail("图片上传失败: " + e.getMessage());
        }
    }

    /**
     * 优先使用 Base64，否则从 URL 下载。
     */
    private byte[] resolveImageBytes(Image output) throws IOException {
        String b64Json = output.getB64Json();
        if (StringUtil.isNotBlank(b64Json)) {
            return Base64Util.decodeToBytes(b64Json);
        }
        String url = output.getUrl();
        if (StringUtil.isNotBlank(url)) {
            return ResourceUtil.getResource(url).getContentAsByteArray();
        }
        return null;
    }

    private String resolveImageFilename(Image output) {
        String url = output.getUrl();
        if (StringUtil.isNotBlank(url)) {
            int queryIndex = url.indexOf('?');
            String path = queryIndex > 0 ? url.substring(0, queryIndex) : url;
            int slashIndex = path.lastIndexOf('/');
            if (slashIndex >= 0 && slashIndex < path.length() - 1) {
                String name = path.substring(slashIndex + 1);
                if (name.contains(".")) {
                    return name;
                }
            }
        }
        return DEFAULT_IMAGE_FILENAME;
    }

    /**
     * 校验图片生成入参。
     */
    private void validateImageParam(AiImageParam param) {
        if (param.getModelId() == null) {
            throw new ServiceException(AiErrorEnum.MODEL_NOT_FOUND.getCode(), AiErrorEnum.MODEL_NOT_FOUND.getMessage());
        }
        if (StringUtil.isBlank(param.getContent())) {
            throw new ServiceException(AiErrorEnum.IMAGE_CONTENT_EMPTY.getCode(), AiErrorEnum.IMAGE_CONTENT_EMPTY.getMessage());
        }
    }

    /**
     * 校验思维导图入参。
     */
    private void validateMindmapParam(AiMindmapParam param) {
        if (param.getModelId() == null) {
            throw new ServiceException(AiErrorEnum.MODEL_NOT_FOUND.getCode(), AiErrorEnum.MODEL_NOT_FOUND.getMessage());
        }
        if (StringUtil.isBlank(param.getContent())) {
            throw new ServiceException(AiErrorEnum.CHAT_CONTENT_EMPTY.getCode(), AiErrorEnum.CHAT_CONTENT_EMPTY.getMessage());
        }
    }

    /**
     * 校验文章写作入参。
     */
    private void validateArticleParam(AiArticleParam param) {
        if (param.getModelId() == null) {
            throw new ServiceException(AiErrorEnum.MODEL_NOT_FOUND.getCode(), AiErrorEnum.MODEL_NOT_FOUND.getMessage());
        }
        if (StringUtil.isBlank(param.getTitle())) {
            throw new ServiceException(AiErrorEnum.CHAT_CONTENT_EMPTY.getCode(), "文章标题不能为空");
        }
        if (StringUtil.isBlank(param.getWordRange())
                || StringUtil.isBlank(param.getGenre())
                || StringUtil.isBlank(param.getTone())
                || StringUtil.isBlank(param.getLanguage())
                || StringUtil.isBlank(param.getFormat())) {
            throw new ServiceException(AiErrorEnum.CHAT_CONTENT_EMPTY.getCode(), "请完善字数区间、文体、语气、语言与格式");
        }
        if (StringUtil.isBlank(param.getExtraRequirements())) {
            throw new ServiceException(AiErrorEnum.CHAT_CONTENT_EMPTY.getCode(), AiErrorEnum.CHAT_CONTENT_EMPTY.getMessage());
        }
    }

    /**
     * 将文章参数组装为落库可读的用户消息（续写时模型可见）。
     */
    private String buildArticleUserContent(AiArticleParam param) {
        String keywords = StringUtil.isBlank(param.getKeywords()) ? "无" : param.getKeywords().trim();
        return """
                请撰写文章。
                标题：%s
                字数区间：%s
                文体：%s
                语气：%s
                语言：%s
                格式：%s
                关键词：%s
                用户提示词：%s
                """.formatted(
                param.getTitle().trim(),
                param.getWordRange().trim(),
                param.getGenre().trim(),
                param.getTone().trim(),
                param.getLanguage().trim(),
                param.getFormat().trim(),
                keywords,
                param.getExtraRequirements().trim());
    }

    /**
     * 校验文本对话入参。
     */
    private void validateTextParam(AiChatSendParam param) {
        if (param.getModelId() == null) {
            throw new ServiceException(AiErrorEnum.MODEL_NOT_FOUND.getCode(), AiErrorEnum.MODEL_NOT_FOUND.getMessage());
        }
        if (StringUtil.isBlank(param.getContent())) {
            throw new ServiceException(AiErrorEnum.CHAT_CONTENT_EMPTY.getCode(), AiErrorEnum.CHAT_CONTENT_EMPTY.getMessage());
        }
    }

    /**
     * 解析或创建思维导图分组：无 id 时写入固定 system 提示词与 MINDMAP 类型。
     */
    private AiChatRecordDetailVO resolveMindmapRecord(AiMindmapParam param, AiModelCheckVO modelCheckVO) {
        if (param.getId() == null) {
            AiChatRecordDetailVO chatRecord = aiChatRecordService.createAndSaveChatRecord(
                    modelCheckVO,
                    buildTitle(param.getContent()),
                    PromptConstant.MINDMAP_SYSTEM,
                    null,
                    null,
                    AiChatTypeEnum.MINDMAP);
            param.setId(chatRecord.getId());
            return chatRecord;
        }
        AiChatRecordDetailVO chatRecord = aiChatRecordService.detail(param.getId());
        if (chatRecord == null) {
            throw new ServiceException(AiErrorEnum.CHAT_RECORD_NOT_FOUND.getCode(), AiErrorEnum.CHAT_RECORD_NOT_FOUND.getMessage());
        }
        if (!param.getModelId().equals(chatRecord.getModelId())) {
            aiChatRecordService.changeChatRecordModel(param.getId(), param.getModelId());
            chatRecord.setModelId(param.getModelId());
        }
        return chatRecord;
    }

    /**
     * 解析或创建文章写作分组：无 id 时写入固定 system 提示词与 ARTICLE 类型。
     */
    private AiChatRecordDetailVO resolveArticleRecord(AiArticleParam param, AiModelCheckVO modelCheckVO) {
        if (param.getId() == null) {
            AiChatRecordDetailVO chatRecord = aiChatRecordService.createAndSaveChatRecord(
                    modelCheckVO,
                    buildTitle(param.getTitle()),
                    PromptConstant.ARTICLE_SYSTEM,
                    null,
                    null,
                    AiChatTypeEnum.ARTICLE);
            param.setId(chatRecord.getId());
            return chatRecord;
        }
        AiChatRecordDetailVO chatRecord = aiChatRecordService.detail(param.getId());
        if (chatRecord == null) {
            throw new ServiceException(AiErrorEnum.CHAT_RECORD_NOT_FOUND.getCode(), AiErrorEnum.CHAT_RECORD_NOT_FOUND.getMessage());
        }
        if (!param.getModelId().equals(chatRecord.getModelId())) {
            aiChatRecordService.changeChatRecordModel(param.getId(), param.getModelId());
            chatRecord.setModelId(param.getModelId());
        }
        return chatRecord;
    }

    /**
     * 解析或创建会话：无 id 时按首条消息创建，有 id 时走会话详情缓存。
     */
    private AiChatRecordDetailVO resolveChatRecord(AiChatSendParam param, AiModelCheckVO modelCheckVO) {
        if (param.getId() == null) {
            AiChatRecordDetailVO chatRecord = aiChatRecordService.createAndSaveChatRecord(
                    modelCheckVO, buildTitle(param.getContent()), null, null, null, AiChatTypeEnum.CHAT);
            param.setId(chatRecord.getId());
            return chatRecord;
        }
        AiChatRecordDetailVO chatRecord = aiChatRecordService.detail(param.getId());
        if (chatRecord == null) {
            throw new ServiceException(AiErrorEnum.CHAT_RECORD_NOT_FOUND.getCode(), AiErrorEnum.CHAT_RECORD_NOT_FOUND.getMessage());
        }
        if (!param.getModelId().equals(chatRecord.getModelId())) {
            aiChatRecordService.changeChatRecordModel(param.getId(), param.getModelId());
            chatRecord.setModelId(param.getModelId());
        }
        return chatRecord;
    }

    /**
     * 构建文本对话 Prompt，挂载历史上下文、Token 统计与工具调用能力。
     */
    private ChatClient.ChatClientRequestSpec buildTextPrompt(ChatClient chatClient,
                                                             String content,
                                                             AiChatRecordDetailVO chatRecord,
                                                             AiModelCheckVO modelCheckVO) {
        return buildTextPrompt(chatClient, content, chatRecord, modelCheckVO, null);
    }

    /**
     * 构建文本对话 Prompt，挂载历史上下文、Token 统计与工具调用能力。
     */
    private ChatClient.ChatClientRequestSpec buildTextPrompt(ChatClient chatClient,
                                                             String content,
                                                             AiChatRecordDetailVO chatRecord,
                                                             AiModelCheckVO modelCheckVO,
                                                             List<UserMessageAttachment> attachments) {
        ChatHistoryAdvisor historyAdvisor = chatKit.historyAdvisor();
        ToolCallAdvisor toolCallAdvisor = chatKit.toolCallAdvisor();
        TokenUsageStatisticsAdvisor tokenUsageAdvisor = chatKit.tokenUsageAdvisor();
        KnowledgeRetrievalAdvisor knowledgeAdvisor = chatKit.knowledgeAdvisor();
        PromptAugmentationAdvisor promptAdvisor = chatKit.promptAdvisor();
        Map<String, Object> context = buildAdvisorContext(chatRecord, modelCheckVO, attachments);
        AiChatOptions sessionOptions = chatRecord.getChatOptions();
        List<ToolCallback> toolCallbacks = resolveToolCallbacks(sessionOptions.getTools(), sessionOptions.getMcpIds());
        ChatOptions chatOptions = chatKit.getChatOptions(
                modelCheckVO.getPlatform(), chatRecord, modelCheckVO, toolCallbacks, context);

        ChatClient.ChatClientRequestSpec requestSpec = chatClient.prompt();
        if (StringUtil.isNotBlank(chatRecord.getSystemPrompt())) {
            requestSpec.system(chatRecord.getSystemPrompt());
        }
        return requestSpec.options(chatOptions)
                .advisors(spec -> {
                    spec.params(context).advisors(historyAdvisor,
                            tokenUsageAdvisor, promptAdvisor);
                    if (!toolCallbacks.isEmpty()) {
                        spec.advisors(toolCallAdvisor);
                    }
                    if (CollectionUtil.isNotEmpty(sessionOptions.getKnowledgeParams())) {
                        spec.advisors(knowledgeAdvisor);
                    }
                })
                .user(content);
    }

    /**
     * 构建 Advisor 上下文，供多轮历史、Token 统计与消息持久化使用。
     * <p>
     * 写入的 key 会被 {@link org.bedrock.common.ai.advisor.TokenUsageStatisticsAdvisor} 读取，
     * 用于关联用户、模型、API Key、会话等信息。
     * </p>
     */
    private Map<String, Object> buildAdvisorContext(AiChatRecordDetailVO chatRecord,
                                                    AiModelCheckVO modelCheckVO,
                                                    List<UserMessageAttachment> attachments) {
        Map<String, Object> context = new HashMap<>();
        // 会话 id，ChatHistory 与 TokenUsage 共用
        context.put(ChatHistoryStore.CONVERSATION_ID, chatRecord.getId().toString());
        context.put(ChatHistoryStore.MAX_MESSAGES, chatRecord.getChatOptions().getMaxMessages());
        // 会话快照：title、modelId 等
        context.put(AiConstant.CTX_CHAT_RECORD, chatRecord);
        // 模型快照：modelName、model、apiKeyId、apiKeyName、platform
        context.put(AiConstant.CTX_MODEL_DETAIL, modelCheckVO);
        if (attachments != null && !attachments.isEmpty()) {
            context.put(AiConstant.CTX_USER_ATTACHMENT, attachments);
        }
        AuthUser authUser = AuthUtil.getAuthUser();
        if (authUser != null) {
            // 当前用户：userId、nickname/username
            context.put(AiConstant.CTX_USER_INFO, authUser);
        }
        return context;
    }

    /**
     * 根据会话配置解析工具回调（内置工具组 + MCP）。
     */
    private List<ToolCallback> resolveToolCallbacks(List<String> tools, List<String> mcpIds) {
        List<ToolCallback> callbacks = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(tools)) {
            tools.stream()
                    .map(String::trim)
                    .filter(StringUtil::isNotBlank)
                    .map(toolName -> chatKit.toolManager().getCallbacks(toolName))
                    .flatMap(List::stream)
                    .forEach(callbacks::add);
        }
        if (CollectionUtil.isNotEmpty(mcpIds)) {
            mcpIds.stream()
                    .map(String::trim)
                    .filter(StringUtil::isNotBlank)
                    .map(mcpId -> chatKit.mcpManager().getCallbacks(mcpId))
                    .flatMap(List::stream)
                    .forEach(callbacks::add);
        }
        return callbacks;
    }

    /**
     * 组装对话响应。
     */
    private AiChatSendVO buildSendVO(AiChatRecordDetailVO chatRecord, AiModelCheckVO modelCheckVO, String content) {
        return AiChatSendVO.builder()
                .id(chatRecord.getId())
                .modelId(modelCheckVO.getId())
                .model(modelCheckVO.getModel())
                .modelName(modelCheckVO.getModelName())
                .content(content)
                .build();
    }

    /**
     * 根据首条消息生成会话标题。
     */
    private String buildTitle(String content) {
        String title = content.trim().replaceAll("\\s+", " ");
        if (title.length() <= TITLE_MAX_LENGTH) {
            return title;
        }
        return title.substring(0, TITLE_MAX_LENGTH);
    }

}
