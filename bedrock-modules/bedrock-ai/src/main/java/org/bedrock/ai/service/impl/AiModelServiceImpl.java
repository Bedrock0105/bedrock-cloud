package org.bedrock.ai.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.bedrock.ai.cache.AiCache;
import org.bedrock.ai.entity.AiApiKey;
import org.bedrock.ai.entity.AiModel;
import org.bedrock.ai.enums.AiErrorEnum;
import org.bedrock.ai.mapper.AiApiKeyMapper;
import org.bedrock.ai.mapper.AiModelMapper;
import org.bedrock.ai.param.AiModelListParam;
import org.bedrock.ai.param.AiModelSubmitParam;
import org.bedrock.ai.service.IAiModelService;
import org.bedrock.ai.vo.AiModelCheckVO;
import org.bedrock.ai.vo.AiModelDetailVO;
import org.bedrock.ai.vo.AiModelListVO;
import org.bedrock.common.ai.enums.AiModelTypeEnum;
import org.bedrock.common.ai.factory.model.AiModelFactory;
import org.bedrock.common.ai.factory.vectorstore.VectorStoreFactory;
import org.bedrock.common.ai.model.factory.model.ChatModelCreateParam;
import org.bedrock.common.ai.model.factory.model.EmbeddingModelCreateParam;
import org.bedrock.common.ai.model.factory.model.ImageModelCreateParam;
import org.bedrock.common.auth.util.AuthUtil;
import org.bedrock.common.code.constant.CacheConstant;
import org.bedrock.common.code.util.BeanUtil;
import org.bedrock.common.code.util.StringUtil;
import org.bedrock.common.log.exception.ServiceException;
import org.bedrock.common.log.operation.support.LogRecordContext;
import org.bedrock.common.mybatisplus.base.BaseEntity;
import org.bedrock.common.mybatisplus.base.BaseServiceImpl;
import org.bedrock.common.mybatisplus.constant.BedrockDBConstant;
import org.bedrock.common.tenant.base.TenantEntity;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.image.ImageModel;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AiModelServiceImpl extends BaseServiceImpl<AiModelMapper, AiModel> implements IAiModelService {

    private final AiApiKeyMapper aiApiKeyMapper;

    private final AiModelFactory aiModelFactory;

    private final VectorStoreFactory vectorStoreFactory;

    /**
     * 添加 AI 模型，默认禁用状态
     */
    @Override
    public boolean submit(AiModelSubmitParam param) {
        validateApiKey(param.getApiKeyId());
        if (exists(Wrappers.<AiModel>lambdaQuery()
                .eq(AiModel::getModel, param.getModel())
                .eq(TenantEntity::getTenantId, AuthUtil.getTenantId())
                .eq(AiModel::getPlatform, param.getPlatform())
                .eq(BaseEntity::getIsDeleted, BedrockDBConstant.DB_NOT_DELETED))) {
            throw new ServiceException(AiErrorEnum.MODEL_ALREADY_EXISTS.getCode(), AiErrorEnum.MODEL_ALREADY_EXISTS.getMessage());
        }
        AiModel aiModel = BeanUtil.copyProperties(param, AiModel.class);
        normalizeSupportMultimodal(aiModel);
        clearOtherDefaultModel(aiModel);
        aiModel.setStatus(BedrockDBConstant.DB_STATUS_DISABLE);
        return save(aiModel);
    }

    /**
     * 修改 AI 模型
     */
    @Override
    @CacheEvict(cacheNames = CacheConstant.AI_CACHE, key = "'" + AiCache.MODEL_CHECK_ID + "' + #param.id")
    public boolean edit(AiModelSubmitParam param) {
        validateApiKey(param.getApiKeyId());
        if (exists(Wrappers.<AiModel>lambdaQuery()
                .eq(AiModel::getModel, param.getModel())
                .eq(AiModel::getPlatform, param.getPlatform())
                .eq(TenantEntity::getTenantId, AuthUtil.getTenantId())
                .eq(BaseEntity::getIsDeleted, BedrockDBConstant.DB_NOT_DELETED)
                .ne(BaseEntity::getId, param.getId()))) {
            throw new ServiceException(AiErrorEnum.MODEL_ALREADY_EXISTS.getCode(), AiErrorEnum.MODEL_ALREADY_EXISTS.getMessage());
        }
        aiModelFactory.removeAll(param.getId().toString());
        vectorStoreFactory.removeVectorStoreContains(param.getId().toString());
        AiModel existing = getById(param.getId());
        if (existing == null) {
            throw new ServiceException(AiErrorEnum.MODEL_NOT_FOUND.getCode(), AiErrorEnum.MODEL_NOT_FOUND.getMessage());
        }
        AiModel aiModel = BeanUtil.copyProperties(param, AiModel.class);
        // 模型类型创建后不可修改
        aiModel.setModelType(existing.getModelType());
        normalizeSupportMultimodal(aiModel);
        clearOtherDefaultModel(aiModel);
        return updateById(aiModel);
    }

    /**
     * 删除 AI 模型
     */
    @Override
    @CacheEvict(cacheNames = CacheConstant.AI_CACHE, key = "'" + AiCache.MODEL_CHECK_ID + "' + #id")
    public boolean removeById(Long id) {
        aiModelFactory.removeAll(id.toString());
        vectorStoreFactory.removeVectorStoreContains(id.toString());
        return logicRemoveById(id);
    }

    /**
     * 查询 AI 模型详情
     */
    @Override
    public AiModelDetailVO detail(Long id) {
        return baseMapper.selectDetailById(id);
    }

    /**
     * 查询 AI 模型无分页列表
     */
    @Override
    public List<AiModelListVO> selectAiModelList(AiModelListParam param) {
        return baseMapper.selectAiModelList(null, param);
    }

    /**
     * 查询 AI 模型分页列表
     */
    @Override
    public IPage<AiModelListVO> selectAiModelListPage(IPage<AiModelListVO> iPage, AiModelListParam param) {
        return iPage.setRecords(baseMapper.selectAiModelList(iPage, param));
    }

    /**
     * 启用禁用 AI 模型
     */
    @Override
    @CacheEvict(cacheNames = CacheConstant.AI_CACHE, key = "'" + AiCache.MODEL_CHECK_ID + "' + #id")
    public boolean enableStatus(Long id, Integer status) {
        AiModel aiModel = this.getById(id);
        LogRecordContext.putVariable("modelName", aiModel.getModelName());
        LogRecordContext.putVariable("status", status);
        return this.update(Wrappers.<AiModel>lambdaUpdate()
                .eq(BaseEntity::getId, id)
                .set(AiModel::getStatus, status));
    }

    /**
     * 检测模型是否存在且已启用，并校验关联 API Key
     */
    @Override
    @Cacheable(cacheNames = CacheConstant.AI_CACHE, key = "'" + AiCache.MODEL_CHECK_ID + "' + #modelId")
    public AiModelCheckVO checkAiModel(Long modelId) {
        AiModelCheckVO checkVO = baseMapper.selectCheckById(modelId);
        validateCheckAiModel(checkVO);
        return checkVO;
    }

    /**
     * 获取 ChatModel
     */
    @Override
    public ChatModel getChatModel(AiModelCheckVO checkVO) {
        return aiModelFactory.getChatModel(buildChatModelCreateParam(checkVO));
    }

    /**
     * 获取 ChatClient
     */
    @Override
    public ChatClient getChatClient(AiModelCheckVO checkVO) {
        return aiModelFactory.getChatClient(buildChatModelCreateParam(checkVO));
    }

    /**
     * 获取 EmbeddingModel
     */
    @Override
    public EmbeddingModel getEmbeddingModel(AiModelCheckVO checkVO) {
        return aiModelFactory.getEmbeddingModel(buildEmbeddingModelCreateParam(checkVO));
    }

    @Override
    public ImageModel getImageModel(AiModelCheckVO checkVO) {
        return aiModelFactory.getImageModel(buildImageModelCreateParam(checkVO));
    }

    /**
     * 校验关联的 API Key 配置是否存在
     *
     * @param apiKeyId bedrock_ai_api_key 表主键 id
     */
    private void validateApiKey(Long apiKeyId) {
        AiApiKey aiApiKey = aiApiKeyMapper.selectById(apiKeyId);
        if (aiApiKey == null || !BedrockDBConstant.DB_NOT_DELETED.equals(aiApiKey.getIsDeleted())) {
            throw new ServiceException(AiErrorEnum.API_KEY_NOT_FOUND.getCode(), AiErrorEnum.API_KEY_NOT_FOUND.getMessage());
        }
    }

    /**
     * 校验模型及关联 API Key 是否可用
     */
    private void validateCheckAiModel(AiModelCheckVO checkVO) {
        if (checkVO == null) {
            throw new ServiceException(AiErrorEnum.MODEL_NOT_FOUND.getCode(), AiErrorEnum.MODEL_NOT_FOUND.getMessage());
        }
        if (BedrockDBConstant.DB_STATUS_DISABLE.equals(checkVO.getStatus())) {
            throw new ServiceException(AiErrorEnum.MODEL_DISABLED.getCode(), AiErrorEnum.MODEL_DISABLED.getMessage());
        }
        if (StringUtil.isBlank(checkVO.getApiKey())) {
            throw new ServiceException(AiErrorEnum.API_KEY_NOT_FOUND.getCode(), AiErrorEnum.API_KEY_NOT_FOUND.getMessage());
        }
        if (BedrockDBConstant.DB_STATUS_DISABLE.equals(checkVO.getApiKeyStatus())) {
            throw new ServiceException(AiErrorEnum.API_KEY_DISABLED.getCode(), AiErrorEnum.API_KEY_DISABLED.getMessage());
        }
    }

    /**
     * 构建 ChatModel 创建参数
     */
    private ChatModelCreateParam buildChatModelCreateParam(AiModelCheckVO checkVO) {
        /**
         * 同一个厂商平台 可以复用同一个 ChatModel和ChatClient
         * 所以使用 apiKeyId 作为标识
         */
        return new ChatModelCreateParam(
                checkVO.getApiKeyId().toString(),
                checkVO.getPlatform(),
                checkVO.getBaseUrl(),
                checkVO.getApiKey(),
                checkVO.getApiPath()
        );
    }

    /**
     * 构建 EmbeddingModel 创建参数
     */
    private EmbeddingModelCreateParam buildEmbeddingModelCreateParam(AiModelCheckVO checkVO) {
        return new EmbeddingModelCreateParam(
                checkVO.getId().toString(),
                checkVO.getPlatform(),
                checkVO.getBaseUrl(),
                checkVO.getApiKey(),
                checkVO.getApiPath(),
                checkVO.getModel()
        );
    }

    /**
     * 构建 ImageModel 创建参数
     */
    private ImageModelCreateParam buildImageModelCreateParam(AiModelCheckVO checkVO) {
        return new ImageModelCreateParam(
                checkVO.getId().toString(),
                checkVO.getPlatform(),
                checkVO.getBaseUrl(),
                checkVO.getApiKey(),
                checkVO.getApiPath()
        );
    }

    /**
     * 同租户、同模型类型下仅保留一个默认模型
     */
    private void clearOtherDefaultModel(AiModel aiModel) {
        if (!BedrockDBConstant.DB_STATUS_NORMAL.equals(aiModel.getIsDefault())) {
            return;
        }
        this.update(Wrappers.<AiModel>lambdaUpdate()
                .set(AiModel::getIsDefault, BedrockDBConstant.DB_STATUS_DISABLE)
                .eq(TenantEntity::getTenantId, AuthUtil.getTenantId())
                .eq(AiModel::getModelType, aiModel.getModelType())
                .eq(BaseEntity::getIsDeleted, BedrockDBConstant.DB_NOT_DELETED)
                .ne(aiModel.getId() != null, BaseEntity::getId, aiModel.getId()));
    }

    /**
     * 非对话模型不保留多模态标记，避免脏数据
     */
    private void normalizeSupportMultimodal(AiModel aiModel) {
        if (!AiModelTypeEnum.CHAT.equals(aiModel.getModelType())) {
            aiModel.setSupportMultimodal(BedrockDBConstant.DB_STATUS_DISABLE);
            return;
        }
        if (aiModel.getSupportMultimodal() == null) {
            aiModel.setSupportMultimodal(BedrockDBConstant.DB_STATUS_DISABLE);
        }
    }

}
