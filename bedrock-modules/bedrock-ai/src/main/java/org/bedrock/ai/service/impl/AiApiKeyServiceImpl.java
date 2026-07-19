package org.bedrock.ai.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.bedrock.ai.entity.AiApiKey;
import org.bedrock.ai.entity.AiModel;
import org.bedrock.ai.enums.AiErrorEnum;
import org.bedrock.ai.cache.AiCache;
import org.bedrock.ai.mapper.AiApiKeyMapper;
import org.bedrock.ai.mapper.AiModelMapper;
import org.bedrock.ai.param.AiApiKeyListParam;
import org.bedrock.ai.param.AiApiKeySubmitParam;
import org.bedrock.ai.service.IAiApiKeyService;
import org.bedrock.ai.vo.AiApiKeyDetailVO;
import org.bedrock.ai.vo.AiApiKeyListVO;
import org.bedrock.common.ai.factory.model.AiModelFactory;
import org.bedrock.common.ai.factory.vectorstore.VectorStoreFactory;
import org.bedrock.common.code.constant.CacheConstant;
import org.bedrock.common.code.util.BeanUtil;
import org.bedrock.common.code.util.CacheUtil;
import org.bedrock.common.log.exception.ServiceException;
import org.bedrock.common.log.operation.support.LogRecordContext;
import org.bedrock.common.mybatisplus.base.BaseEntity;
import org.bedrock.common.mybatisplus.base.BaseServiceImpl;
import org.bedrock.common.mybatisplus.constant.BedrockDBConstant;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AiApiKeyServiceImpl extends BaseServiceImpl<AiApiKeyMapper, AiApiKey> implements IAiApiKeyService {

    private final AiModelMapper aiModelMapper;

    private final AiModelFactory aiModelFactory;

    private final VectorStoreFactory vectorStoreFactory;

    /**
     * 添加 API Key 配置，默认禁用状态
     */
    @Override
    public boolean submit(AiApiKeySubmitParam param) {
        if (exists(Wrappers.<AiApiKey>lambdaQuery()
                .eq(AiApiKey::getKeyName, param.getKeyName())
                .eq(BaseEntity::getIsDeleted, BedrockDBConstant.DB_NOT_DELETED))) {
            throw new ServiceException(AiErrorEnum.KEY_NAME_ALREADY_EXISTS.getCode(), AiErrorEnum.KEY_NAME_ALREADY_EXISTS.getMessage());
        }
        AiApiKey aiApiKey = BeanUtil.copyProperties(param, AiApiKey.class);
        aiApiKey.setStatus(BedrockDBConstant.DB_STATUS_DISABLE);
        return save(aiApiKey);
    }

    /**
     * 修改 API Key 配置
     */
    @Override
    public boolean edit(AiApiKeySubmitParam param) {
        if (exists(Wrappers.<AiApiKey>lambdaQuery()
                .eq(AiApiKey::getKeyName, param.getKeyName())
                .eq(BaseEntity::getIsDeleted, BedrockDBConstant.DB_NOT_DELETED)
                .ne(BaseEntity::getId, param.getId()))) {
            throw new ServiceException(AiErrorEnum.KEY_NAME_ALREADY_EXISTS.getCode(), AiErrorEnum.KEY_NAME_ALREADY_EXISTS.getMessage());
        }
        AiApiKey aiApiKey = BeanUtil.copyProperties(param, AiApiKey.class);
        clearRelatedModelCache(aiApiKey.getId());
        return updateById(aiApiKey);
    }

    /**
     * 删除 API Key 配置，若已被模型引用则不允许删除
     */
    @Override
    public boolean removeById(Long id) {
        if (aiModelMapper.selectCount(Wrappers.<AiModel>lambdaQuery()
                .eq(AiModel::getApiKeyId, id)
                .eq(BaseEntity::getIsDeleted, BedrockDBConstant.DB_NOT_DELETED)) > 0) {
            throw new ServiceException(AiErrorEnum.API_KEY_IN_USE.getCode(), AiErrorEnum.API_KEY_IN_USE.getMessage());
        }
        return logicRemoveById(id);
    }

    /**
     * 查询 API Key 配置详情
     */
    @Override
    public AiApiKeyDetailVO detail(Long id) {
        return baseMapper.selectDetailById(id);
    }

    /**
     * 查询 API Key 配置无分页列表
     */
    @Override
    public List<AiApiKeyListVO> selectAiApiKeyList(AiApiKeyListParam param) {
        return baseMapper.selectAiApiKeyList(null, param);
    }

    /**
     * 查询 API Key 配置分页列表
     */
    @Override
    public IPage<AiApiKeyListVO> selectAiApiKeyListPage(IPage<AiApiKeyListVO> iPage, AiApiKeyListParam param) {
        return iPage.setRecords(baseMapper.selectAiApiKeyList(iPage, param));
    }

    /**
     * 启用禁用 API Key 配置
     */
    @Override
    public boolean enableStatus(Long id, Integer status) {
        AiApiKey aiApiKey = this.getById(id);
        LogRecordContext.putVariable("keyName", aiApiKey.getKeyName());
        LogRecordContext.putVariable("status", status);
        if (BedrockDBConstant.DB_STATUS_DISABLE.equals(status)) {
            clearRelatedModelCache(id);
        }
        return this.update(Wrappers.<AiApiKey>lambdaUpdate()
                .eq(BaseEntity::getId, id)
                .set(AiApiKey::getStatus, status));
    }

    /**
     * 清除关联模型的 Chat 实例及校验缓存
     */
    private void clearRelatedModelCache(Long apiKeyId) {
        aiModelMapper.selectList(Wrappers.<AiModel>lambdaQuery()
                        .select(BaseEntity::getId)
                        .eq(AiModel::getApiKeyId, apiKeyId)
                        .eq(BaseEntity::getIsDeleted, BedrockDBConstant.DB_NOT_DELETED))
                .forEach(aiModel -> {
                    aiModelFactory.removeAll(aiModel.getId().toString());
                    vectorStoreFactory.removeVectorStoreContains(aiModel.toString());
                    CacheUtil.evict(CacheConstant.AI_CACHE, AiCache.MODEL_CHECK_ID + aiModel.getId());
                });
        aiModelFactory.removeAll(apiKeyId.toString());
        vectorStoreFactory.removeVectorStoreContains(apiKeyId.toString());
    }

}