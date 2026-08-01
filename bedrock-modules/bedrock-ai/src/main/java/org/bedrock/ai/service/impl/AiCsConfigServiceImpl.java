package org.bedrock.ai.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.bedrock.ai.cache.AiCache;
import org.bedrock.ai.dto.AiChatOptions;
import org.bedrock.ai.entity.AiCsConfig;
import org.bedrock.ai.enums.AiErrorEnum;
import org.bedrock.ai.mapper.AiCsConfigMapper;
import org.bedrock.ai.param.AiCsConfigSaveParam;
import org.bedrock.ai.service.IAiCsConfigService;
import org.bedrock.ai.vo.AiCsConfigDetailVO;
import org.bedrock.common.auth.util.AuthUtil;
import org.bedrock.common.code.constant.CacheConstant;
import org.bedrock.common.code.util.BeanUtil;
import org.bedrock.common.code.util.CacheUtil;
import org.bedrock.common.code.util.CollectionUtil;
import org.bedrock.common.code.util.StringUtil;
import org.bedrock.common.log.exception.ServiceException;
import org.bedrock.common.mybatisplus.base.BaseEntity;
import org.bedrock.common.mybatisplus.base.BaseServiceImpl;
import org.bedrock.common.mybatisplus.constant.BedrockDBConstant;
import org.bedrock.common.tenant.base.TenantEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AI 客服配置服务实现。
 * <p>runtime 按租户缓存；save 时按 tenantId 失效，保证浮窗开关及时生效。</p>
 */
@Service
public class AiCsConfigServiceImpl extends BaseServiceImpl<AiCsConfigMapper, AiCsConfig> implements IAiCsConfigService {

    /**
     * 管理端拉取本租户配置。
     */
    @Override
    public AiCsConfigDetailVO detail() {
        return baseMapper.selectDetailByTenantId(AuthUtil.getTenantId());
    }

    /**
     * upsert 本租户配置：强制校验模型与知识库，并清理 runtime 缓存。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveConfig(AiCsConfigSaveParam param) {
        validateSaveParam(param);
        String tenantId = AuthUtil.getTenantId();
        AiCsConfig existing = getOne(Wrappers.<AiCsConfig>lambdaQuery()
                .eq(TenantEntity::getTenantId, tenantId)
                .eq(BaseEntity::getIsDeleted, BedrockDBConstant.DB_NOT_DELETED));
        AiCsConfig entity = BeanUtil.copyProperties(param, AiCsConfig.class);
        boolean ok;
        if (existing == null) {
            entity.setTenantId(tenantId);
            ok = save(entity);
        } else {
            entity.setId(existing.getId());
            entity.setTenantId(tenantId);
            ok = updateById(entity);
        }
        CacheUtil.evict(CacheConstant.AI_CACHE, AiCache.CS_CONFIG_TENANT_ID + tenantId);
        return ok;
    }

    /**
     * 浮窗 / 聊天前：未启用或不完整配置返回 enabled=false（不抛异常）。
     */
    @Override
    @Cacheable(cacheNames = CacheConstant.AI_CACHE, key = "'" + AiCache.CS_CONFIG_TENANT_ID + "' + #tenantId")
    public AiCsConfigDetailVO requireEnabledDetail(String tenantId) {
        AiCsConfigDetailVO detail = baseMapper.selectDetailByTenantId(AuthUtil.getTenantId());
        if (detail == null || detail.getStatus() == null
                || !detail.getStatus().equals(BedrockDBConstant.DB_STATUS_NORMAL)
                || detail.getModelId() == null) {
            return AiCsConfigDetailVO.disabled();
        }
        return detail;
    }

    private void validateSaveParam(AiCsConfigSaveParam param) {
        if (StringUtil.isBlank(param.getName())) {
            throw new ServiceException(AiErrorEnum.CS_CONFIG_DISABLED.getCode(), "客服名称不能为空");
        }
        if (param.getModelId() == null) {
            throw new ServiceException(AiErrorEnum.MODEL_NOT_FOUND.getCode(), AiErrorEnum.MODEL_NOT_FOUND.getMessage());
        }
        if (StringUtil.isBlank(param.getSystemPrompt())) {
            throw new ServiceException(AiErrorEnum.CS_CONFIG_DISABLED.getCode(), "系统提示词不能为空");
        }
        AiChatOptions options = param.getChatOptions();
        if (options == null || CollectionUtil.isEmpty(options.getKnowledgeParams())) {
            throw new ServiceException(AiErrorEnum.CS_KNOWLEDGE_REQUIRED.getCode(),
                    AiErrorEnum.CS_KNOWLEDGE_REQUIRED.getMessage());
        }
    }

}
