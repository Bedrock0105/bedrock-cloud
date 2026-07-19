package org.bedrock.ai.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.bedrock.ai.entity.AiModel;
import org.bedrock.ai.param.AiModelListParam;
import org.bedrock.ai.param.AiModelSubmitParam;
import org.bedrock.ai.vo.AiModelCheckVO;
import org.bedrock.ai.vo.AiModelDetailVO;
import org.bedrock.ai.vo.AiModelListVO;
import org.bedrock.common.mybatisplus.base.IBaseService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.image.ImageModel;

import java.util.List;

public interface IAiModelService extends IBaseService<AiModel> {

    /**
     * 添加 AI 模型
     */
    boolean submit(AiModelSubmitParam param);

    /**
     * 修改 AI 模型
     */
    boolean edit(AiModelSubmitParam param);

    /**
     * 删除 AI 模型
     */
    boolean removeById(Long id);

    /**
     * 详情
     */
    AiModelDetailVO detail(Long id);

    /**
     * 无分页列表
     */
    List<AiModelListVO> selectAiModelList(AiModelListParam param);

    /**
     * 分页列表
     */
    IPage<AiModelListVO> selectAiModelListPage(IPage<AiModelListVO> iPage, AiModelListParam param);

    /**
     * 启用禁用
     */
    boolean enableStatus(Long id, Integer status);

    /**
     * 检测模型是否存在且已启用，并校验关联 API Key
     * 不存在或已禁用则抛出异常
     */
    AiModelCheckVO checkAiModel(Long modelId);

    /**
     * 获取 ChatModel
     */
    ChatModel getChatModel(AiModelCheckVO checkVO);

    /**
     * 获取 ChatClient
     */
    ChatClient getChatClient(AiModelCheckVO checkVO);

    /**
     * 获取 EmbeddingModel
     */
    EmbeddingModel getEmbeddingModel(AiModelCheckVO checkVO);

    /**
     * 获取 ImageModel
     */
    ImageModel getImageModel(AiModelCheckVO checkVO);

}
