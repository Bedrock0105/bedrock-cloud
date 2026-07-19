package org.bedrock.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.bedrock.common.code.util.ObjectUtil;
import org.bedrock.common.code.util.StringUtil;
import org.bedrock.common.constant.CacheCommonConstant;
import org.bedrock.common.log.exception.ServiceException;
import org.bedrock.common.mybatisplus.base.BaseEntity;
import org.bedrock.common.mybatisplus.base.BaseServiceImpl;
import org.bedrock.common.mybatisplus.constant.BedrockDBConstant;
import org.bedrock.system.entity.ParamConfig;
import org.bedrock.system.enums.SystemErrorEnum;
import org.bedrock.system.mapper.ParamConfigMapper;
import org.bedrock.system.service.IParamConfigService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static org.bedrock.system.cache.SystemCache.PARAM_CONFIG_DETAIL;

@Service
public class ParamConfigServiceImpl extends BaseServiceImpl<ParamConfigMapper, ParamConfig> implements IParamConfigService {

    @Override
    @CacheEvict(cacheNames = CacheCommonConstant.PARAM_CONFIG_DETAIL_CACHE_KEY, key = "'" + PARAM_CONFIG_DETAIL + "'+ #paramConfig.configKey")
    public boolean submit(ParamConfig paramConfig) {
        if (exists(Wrappers.<ParamConfig>lambdaQuery()
                .eq(ParamConfig::getConfigKey, paramConfig.getConfigKey())
                .ne(ObjectUtil.isNotEmpty(paramConfig.getId()), BaseEntity::getId, paramConfig.getId())
                .eq(ParamConfig::getIsDeleted, BedrockDBConstant.DB_NOT_DELETED))) {
            throw new ServiceException(SystemErrorEnum.PARAM_CONFIG_KEY_EXISTS.getCode(), SystemErrorEnum.PARAM_CONFIG_KEY_EXISTS.getMessage());
        }
        return saveOrUpdate(paramConfig);
    }

    @Override
    @Cacheable(cacheNames = CacheCommonConstant.PARAM_CONFIG_DETAIL_CACHE_KEY,key = "'" + PARAM_CONFIG_DETAIL + "'+ #configKey")
    public ParamConfig detail(String configKey) {
        return this.getOne(Wrappers.<ParamConfig>lambdaQuery()
                .eq(ParamConfig::getConfigKey, configKey)
                .eq(BaseEntity::getIsDeleted, BedrockDBConstant.DB_NOT_DELETED));
    }

    @Override
    public IPage<ParamConfig> page(IPage<ParamConfig> page, ParamConfig paramConfig) {
        return page(page, Wrappers.<ParamConfig>lambdaQuery()
                .eq(ParamConfig::getIsDeleted, BedrockDBConstant.DB_NOT_DELETED)
                .eq(StringUtil.isNotBlank(paramConfig.getConfigKey()), ParamConfig::getConfigKey, paramConfig.getConfigKey())
                .like(StringUtil.isNotBlank(paramConfig.getConfigName()), ParamConfig::getConfigName, paramConfig.getConfigName()));
    }

    @Override
    @CacheEvict(cacheNames = CacheCommonConstant.PARAM_CONFIG_DETAIL_CACHE_KEY, key = "'" + PARAM_CONFIG_DETAIL + "'+ #configKey")
    public boolean removeByCode(String configKey) {
        return this.update(Wrappers.<ParamConfig>lambdaUpdate()
                .eq(ParamConfig::getConfigKey, configKey)
                .set(ParamConfig::getIsDeleted, BedrockDBConstant.DB_IS_DELETED));
    }
}
