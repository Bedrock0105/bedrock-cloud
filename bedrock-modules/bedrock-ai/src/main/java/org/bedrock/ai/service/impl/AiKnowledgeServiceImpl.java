package org.bedrock.ai.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bedrock.ai.cache.AiCache;
import org.bedrock.ai.constant.AiConstant;
import org.bedrock.ai.entity.AiKnowledge;
import org.bedrock.ai.entity.AiKnowledgeDoc;
import org.bedrock.ai.entity.AiModel;
import org.bedrock.ai.entity.AiVectorDb;
import org.bedrock.ai.enums.AiErrorEnum;
import org.bedrock.ai.mapper.AiKnowledgeDocMapper;
import org.bedrock.ai.mapper.AiKnowledgeMapper;
import org.bedrock.ai.param.AiKnowledgeListParam;
import org.bedrock.ai.param.AiKnowledgeSubmitParam;
import org.bedrock.ai.service.IAiKnowledgeChunkService;
import org.bedrock.ai.service.IAiKnowledgeService;
import org.bedrock.ai.service.IAiModelService;
import org.bedrock.ai.service.IAiVectorDbService;
import org.bedrock.ai.vo.AiKnowledgeDetailVO;
import org.bedrock.ai.vo.AiKnowledgeListVO;
import org.bedrock.ai.vo.AiModelCheckVO;
import org.bedrock.ai.vo.AiVectorDbCheckVO;
import org.bedrock.common.ai.enums.AiModelTypeEnum;
import org.bedrock.common.ai.factory.vectorstore.VectorStoreFactory;
import org.bedrock.common.ai.model.factory.vectorstore.VectorStoreCreateParam;
import org.bedrock.common.auth.util.AuthUtil;
import org.bedrock.common.code.constant.CacheConstant;
import org.bedrock.common.code.util.BeanUtil;
import org.bedrock.common.code.util.SpringUtil;
import org.bedrock.common.log.exception.ServiceException;
import org.bedrock.common.log.operation.support.LogRecordContext;
import org.bedrock.common.mybatisplus.base.BaseEntity;
import org.bedrock.common.mybatisplus.base.BaseServiceImpl;
import org.bedrock.common.mybatisplus.constant.BedrockDBConstant;
import org.bedrock.common.tenant.base.TenantEntity;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * AI 知识库服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiKnowledgeServiceImpl extends BaseServiceImpl<AiKnowledgeMapper, AiKnowledge>
        implements IAiKnowledgeService {

    private final IAiModelService aiModelService;

    private final IAiVectorDbService aiVectorDbService;

    private final VectorStoreFactory vectorStoreFactory;

    private final AiKnowledgeDocMapper aiKnowledgeDocMapper;

    private final IAiKnowledgeChunkService aiKnowledgeChunkService;

    private IAiKnowledgeService self;

    /**
     * 新增知识库，同租户下名称不可重复，默认禁用
     */
    @Override
    public boolean submit(AiKnowledgeSubmitParam param) {
        validateSubmitParam(param);
        if (exists(Wrappers.<AiKnowledge>lambdaQuery()
                .eq(AiKnowledge::getKnowledgeName, param.getKnowledgeName())
                .eq(TenantEntity::getTenantId, AuthUtil.getTenantId())
                .eq(BaseEntity::getIsDeleted, BedrockDBConstant.DB_NOT_DELETED))) {
            throw new ServiceException(AiErrorEnum.KNOWLEDGE_NAME_ALREADY_EXISTS.getCode(),
                    AiErrorEnum.KNOWLEDGE_NAME_ALREADY_EXISTS.getMessage());
        }
        AiKnowledge aiKnowledge = BeanUtil.copyProperties(param, AiKnowledge.class);
        aiKnowledge.setStatus(BedrockDBConstant.DB_STATUS_DISABLE);
        return save(aiKnowledge);
    }

    /**
     * 修改知识库，名称唯一性校验通过后更新，并清除详情缓存
     */
    @Override
    @CacheEvict(cacheNames = CacheConstant.AI_CACHE, key = "'" + AiCache.KNOWLEDGE_DETAIL_ID + "' + #param.id")
    public boolean edit(AiKnowledgeSubmitParam param) {
        validateSubmitParam(param);
        if (exists(Wrappers.<AiKnowledge>lambdaQuery()
                .eq(AiKnowledge::getKnowledgeName, param.getKnowledgeName())
                .eq(TenantEntity::getTenantId, AuthUtil.getTenantId())
                .eq(BaseEntity::getIsDeleted, BedrockDBConstant.DB_NOT_DELETED)
                .ne(BaseEntity::getId, param.getId()))) {
            throw new ServiceException(AiErrorEnum.KNOWLEDGE_NAME_ALREADY_EXISTS.getCode(),
                    AiErrorEnum.KNOWLEDGE_NAME_ALREADY_EXISTS.getMessage());
        }
        AiKnowledge aiKnowledge = BeanUtil.copyProperties(param, AiKnowledge.class);
        return updateById(aiKnowledge);
    }

    /**
     * 逻辑删除知识库，并清除详情缓存
     */
    @Override
    @CacheEvict(cacheNames = CacheConstant.AI_CACHE, key = "'" + AiCache.KNOWLEDGE_DETAIL_ID + "' + #id")
    public boolean removeById(Long id) {
        /**
         * 删除知识库的话里面的文档要不要同步删除
         */
        logicRemoveChunksByDocId(id);
        return logicRemoveById(id);
    }

    /**
     * 查询知识库详情
     */
    @Override
    @Cacheable(cacheNames = CacheConstant.AI_CACHE, key = "'" + AiCache.KNOWLEDGE_DETAIL_ID + "' + #id")
    public AiKnowledgeDetailVO detail(Long id) {
        return baseMapper.selectDetailById(id);
    }

    /**
     * 无分页列表
     */
    @Override
    public List<AiKnowledgeListVO> selectAiKnowledgeList(AiKnowledgeListParam param) {
        return baseMapper.selectAiKnowledgeList(null, param);
    }

    /**
     * 分页列表
     */
    @Override
    public IPage<AiKnowledgeListVO> selectAiKnowledgeListPage(IPage<AiKnowledgeListVO> iPage,
                                                              AiKnowledgeListParam param) {
        return iPage.setRecords(baseMapper.selectAiKnowledgeList(iPage, param));
    }

    /**
     * 启用/禁用知识库，并清除详情缓存
     */
    @Override
    @CacheEvict(cacheNames = CacheConstant.AI_CACHE, key = "'" + AiCache.KNOWLEDGE_DETAIL_ID + "' + #id")
    public boolean enableStatus(Long id, Integer status) {
        AiKnowledge aiKnowledge = this.getById(id);
        LogRecordContext.putVariable("knowledgeName", aiKnowledge.getKnowledgeName());
        LogRecordContext.putVariable("status", status);
        return this.update(Wrappers.<AiKnowledge>lambdaUpdate()
                .eq(BaseEntity::getId, id)
                .set(AiKnowledge::getStatus, status));
    }

    @Override
    public VectorStore getVectorStore(Long id) {
        AiKnowledgeDetailVO detail = getSelf().detail(id);
        if (detail == null || Objects.equals(detail.getStatus(), BedrockDBConstant.DB_STATUS_DISABLE)) {
            return null;
        }
        try {
            return getVectorStore(detail.getEmbeddingModelId(), detail.getVectorDbId());
        } catch (ServiceException e) {
            log.error("校验嵌入模型与向量库配置是否有效失败", e);
            return null;
        }
    }

    @Override
    public VectorStore getVectorStore(Long modelId, Long vectorDbId) {
        AiModelCheckVO aiModelCheckVO = aiModelService.checkAiModel(modelId);
        AiVectorDbCheckVO aiVectorDbCheckVO = aiVectorDbService.checkAiVectorDb(vectorDbId);
        EmbeddingModel embeddingModel = aiModelService.getEmbeddingModel(aiModelCheckVO);
        Map<String, Class<?>> metadataFieldTypes = Map.of(
                AiConstant.VECTOR_META_KEY_KNOWLEDGE_ID, String.class,
                AiConstant.VECTOR_META_KEY_DOC_ID, String.class,
                AiConstant.VECTOR_META_KEY_CHUNK_ID, String.class
        );
        VectorStoreCreateParam param = new VectorStoreCreateParam(
                vectorDbId + "-" + modelId,
                aiVectorDbCheckVO.getVectorDbType(),
                embeddingModel,
                aiVectorDbCheckVO.getDatabaseName(),
                aiVectorDbCheckVO.getCollectionName(),
                aiVectorDbCheckVO.getEmbeddingDimension(),
                aiVectorDbCheckVO.getVectorStoreConnectionParam(),
                Map.of(),
                metadataFieldTypes
        );
        return vectorStoreFactory.getVectorStore(param);
    }

    /**
     * 校验嵌入模型与向量库配置是否有效
     */
    private void validateSubmitParam(AiKnowledgeSubmitParam param) {
        // 校验嵌入模型存在且类型为 EMBEDDING
        AiModel embeddingModel = aiModelService.getById(param.getEmbeddingModelId());
        if (embeddingModel == null) {
            throw new ServiceException(AiErrorEnum.EMBEDDING_MODEL_NOT_FOUND.getCode(),
                    AiErrorEnum.EMBEDDING_MODEL_NOT_FOUND.getMessage());
        }
        if (!AiModelTypeEnum.EMBEDDING.equals(embeddingModel.getModelType())) {
            throw new ServiceException(AiErrorEnum.EMBEDDING_MODEL_TYPE_INVALID.getCode(),
                    AiErrorEnum.EMBEDDING_MODEL_TYPE_INVALID.getMessage());
        }
        // 校验向量库配置存在
        AiVectorDb vectorDb = aiVectorDbService.getById(param.getVectorDbId());
        if (vectorDb == null) {
            throw new ServiceException(AiErrorEnum.VECTOR_DB_NOT_FOUND.getCode(),
                    AiErrorEnum.VECTOR_DB_NOT_FOUND.getMessage());
        }
    }

    public IAiKnowledgeService getSelf() {
        if (self == null) {
            self = SpringUtil.getBean(IAiKnowledgeService.class);
        }
        return self;
    }

    private void logicRemoveChunksByDocId(Long id) {
        aiKnowledgeDocMapper.update(null, Wrappers.<AiKnowledgeDoc>lambdaUpdate()
                .eq(AiKnowledgeDoc::getKnowledgeId, id)
                .set(BaseEntity::getUpdateTime, LocalDateTime.now())
                .set(BaseEntity::getUpdateUserId, AuthUtil.getUserId())
                .set(AiKnowledgeDoc::getIsDeleted, BedrockDBConstant.DB_IS_DELETED));
        aiKnowledgeChunkService.removeByKnowledgeId(id);
    }
}
