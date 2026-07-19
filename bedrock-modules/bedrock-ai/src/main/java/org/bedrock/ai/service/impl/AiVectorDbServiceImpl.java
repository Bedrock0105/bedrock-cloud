package org.bedrock.ai.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.bedrock.ai.cache.AiCache;
import org.bedrock.ai.entity.AiVectorDb;
import org.bedrock.ai.enums.AiErrorEnum;
import org.bedrock.ai.mapper.AiVectorDbMapper;
import org.bedrock.ai.param.AiVectorDbListParam;
import org.bedrock.ai.param.AiVectorDbSubmitParam;
import org.bedrock.ai.service.IAiVectorDbService;
import org.bedrock.ai.vo.AiVectorDbCheckVO;
import org.bedrock.ai.vo.AiVectorDbDetailVO;
import org.bedrock.ai.vo.AiVectorDbListVO;
import org.bedrock.common.ai.factory.vectorstore.VectorStoreFactory;
import org.bedrock.common.auth.util.AuthUtil;
import org.bedrock.common.code.constant.CacheConstant;
import org.bedrock.common.code.util.BeanUtil;
import org.bedrock.common.log.exception.ServiceException;
import org.bedrock.common.log.operation.support.LogRecordContext;
import org.bedrock.common.mybatisplus.base.BaseEntity;
import org.bedrock.common.mybatisplus.base.BaseServiceImpl;
import org.bedrock.common.mybatisplus.constant.BedrockDBConstant;
import org.bedrock.common.tenant.base.TenantEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 向量数据库配置服务实现
 */
@Service
@RequiredArgsConstructor
public class AiVectorDbServiceImpl extends BaseServiceImpl<AiVectorDbMapper, AiVectorDb>
        implements IAiVectorDbService {

    private final VectorStoreFactory vectorStoreFactory;

    /**
     * 新增向量数据库配置，同租户下名称不可重复，默认禁用
     */
    @Override
    public boolean submit(AiVectorDbSubmitParam param) {
        if (exists(Wrappers.<AiVectorDb>lambdaQuery()
                .eq(AiVectorDb::getDbName, param.getDbName())
                .eq(TenantEntity::getTenantId, AuthUtil.getTenantId())
                .eq(BaseEntity::getIsDeleted, BedrockDBConstant.DB_NOT_DELETED))) {
            throw new ServiceException(AiErrorEnum.VECTOR_DB_NAME_ALREADY_EXISTS.getCode(),
                    AiErrorEnum.VECTOR_DB_NAME_ALREADY_EXISTS.getMessage());
        }
        AiVectorDb aiVectorDb = BeanUtil.copyProperties(param, AiVectorDb.class);
        aiVectorDb.setStatus(BedrockDBConstant.DB_STATUS_DISABLE);
        return save(aiVectorDb);
    }

    /**
     * 修改向量数据库配置，同步清除校验缓存与向量存储工厂缓存
     */
    @Override
    @CacheEvict(cacheNames = CacheConstant.AI_CACHE, key = "'" + AiCache.VECTOR_DB_CHECK_ID + "' + #param.id")
    public boolean edit(AiVectorDbSubmitParam param) {
        if (exists(Wrappers.<AiVectorDb>lambdaQuery()
                .eq(AiVectorDb::getDbName, param.getDbName())
                .eq(TenantEntity::getTenantId, AuthUtil.getTenantId())
                .eq(BaseEntity::getIsDeleted, BedrockDBConstant.DB_NOT_DELETED)
                .ne(BaseEntity::getId, param.getId()))) {
            throw new ServiceException(AiErrorEnum.VECTOR_DB_NAME_ALREADY_EXISTS.getCode(),
                    AiErrorEnum.VECTOR_DB_NAME_ALREADY_EXISTS.getMessage());
        }
        vectorStoreFactory.removeVectorStoreContains(param.getId().toString());
        AiVectorDb aiVectorDb = BeanUtil.copyProperties(param, AiVectorDb.class);
        return updateById(aiVectorDb);
    }

    /**
     * 逻辑删除向量数据库配置，同步清除校验缓存与向量存储工厂缓存
     */
    @Override
    @CacheEvict(cacheNames = CacheConstant.AI_CACHE, key = "'" + AiCache.VECTOR_DB_CHECK_ID + "' + #id")
    public boolean removeById(Long id) {
        vectorStoreFactory.removeVectorStoreContains(id.toString());
        return logicRemoveById(id);
    }

    /**
     * 查询向量数据库配置详情
     */
    @Override
    public AiVectorDbDetailVO detail(Long id) {
        return baseMapper.selectDetailById(id);
    }

    /**
     * 无分页列表
     */
    @Override
    public List<AiVectorDbListVO> selectAiVectorDbList(AiVectorDbListParam param) {
        return baseMapper.selectAiVectorDbList(null, param);
    }

    /**
     * 分页列表
     */
    @Override
    public IPage<AiVectorDbListVO> selectAiVectorDbListPage(IPage<AiVectorDbListVO> iPage,
                                                            AiVectorDbListParam param) {
        return iPage.setRecords(baseMapper.selectAiVectorDbList(iPage, param));
    }

    /**
     * 启用/禁用向量数据库配置，禁用时同步清除向量存储工厂缓存
     */
    @Override
    @CacheEvict(cacheNames = CacheConstant.AI_CACHE, key = "'" + AiCache.VECTOR_DB_CHECK_ID + "' + #id")
    public boolean enableStatus(Long id, Integer status) {
        AiVectorDb aiVectorDb = this.getById(id);
        LogRecordContext.putVariable("dbName", aiVectorDb.getDbName());
        LogRecordContext.putVariable("status", status);
        if (BedrockDBConstant.DB_STATUS_DISABLE.equals(status)) {
            vectorStoreFactory.removeVectorStoreContains(id.toString());
        }
        return this.update(Wrappers.<AiVectorDb>lambdaUpdate()
                .eq(BaseEntity::getId, id)
                .set(AiVectorDb::getStatus, status));
    }

    /**
     * 检测向量库是否存在且已启用，并返回连接配置
     */
    @Override
    @Cacheable(cacheNames = CacheConstant.AI_CACHE, key = "'" + AiCache.VECTOR_DB_CHECK_ID + "' + #vectorDbId")
    public AiVectorDbCheckVO checkAiVectorDb(Long vectorDbId) {
        AiVectorDbCheckVO checkVO = baseMapper.selectCheckById(vectorDbId);
        validateCheckAiVectorDb(checkVO);
        return checkVO;
    }

    /**
     * 校验向量库配置是否可用
     */
    private void validateCheckAiVectorDb(AiVectorDbCheckVO checkVO) {
        if (checkVO == null) {
            throw new ServiceException(AiErrorEnum.VECTOR_DB_NOT_FOUND.getCode(),
                    AiErrorEnum.VECTOR_DB_NOT_FOUND.getMessage());
        }
        if (BedrockDBConstant.DB_STATUS_DISABLE.equals(checkVO.getStatus())) {
            throw new ServiceException(AiErrorEnum.VECTOR_DB_DISABLED.getCode(),
                    AiErrorEnum.VECTOR_DB_DISABLED.getMessage());
        }
    }

}
