package org.bedrock.ai.service;

import org.bedrock.ai.param.send.AiArticleParam;
import org.bedrock.ai.param.send.AiChatSendParam;
import org.bedrock.ai.param.send.AiImageParam;
import org.bedrock.ai.param.send.AiMindmapParam;
import org.bedrock.ai.vo.AiChatSendVO;
import org.bedrock.ai.vo.AiImageResultVO;
import org.bedrock.common.code.api.R;
import reactor.core.publisher.Flux;

/**
 * AI 对话执行服务。
 * <p>
 * 负责各类模态的对话推理与响应组装，与会话 CRUD（{@link IAiChatRecordService}）解耦。
 * </p>
 */
public interface IAiChatService {

    /**
     * 文本对话（同步）。
     */
    AiChatSendVO sendChar(AiChatSendParam param);

    /**
     * 文本对话（流式）。
     */
    Flux<R<AiChatSendVO>> sendCharStream(AiChatSendParam param);

    /**
     * 图片生成
     */
    AiImageResultVO generateImage(AiImageParam param);

    /**
     * 思维导图生成（流式）。
     */
    Flux<R<AiChatSendVO>> generateMindmapStream(AiMindmapParam param);

    /**
     * 文章写作（流式）。
     */
    Flux<R<AiChatSendVO>> generateArticleStream(AiArticleParam param);

}