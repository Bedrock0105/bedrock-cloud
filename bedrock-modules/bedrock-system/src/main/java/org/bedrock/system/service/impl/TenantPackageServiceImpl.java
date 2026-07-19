package org.bedrock.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.bedrock.common.code.constant.CacheConstant;
import org.bedrock.common.code.constant.StringPool;
import org.bedrock.common.code.util.NumberUtil;
import org.bedrock.common.code.util.StringUtil;
import org.bedrock.common.log.exception.ServiceException;
import org.bedrock.common.mybatisplus.base.BaseEntity;
import org.bedrock.common.mybatisplus.base.BaseServiceImpl;
import org.bedrock.common.mybatisplus.constant.BedrockDBConstant;
import org.bedrock.system.cache.SystemCache;
import org.bedrock.system.entity.Tenant;
import org.bedrock.system.entity.TenantPackage;
import org.bedrock.system.enums.SystemErrorEnum;
import org.bedrock.system.mapper.TenantMapper;
import org.bedrock.system.mapper.TenantPackageMapper;
import org.bedrock.system.param.TenantPackageSubmitParam;
import org.bedrock.system.service.ITenantPackageService;
import org.bedrock.system.vo.TenantDetailVO;
import org.bedrock.system.vo.TenantPackageDetailVO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TenantPackageServiceImpl extends BaseServiceImpl<TenantPackageMapper, TenantPackage> implements ITenantPackageService {

    private final TenantMapper tenantMapper;

    @Override
    public boolean submit(TenantPackageSubmitParam param) {
        TenantPackage entity = new TenantPackage();
        entity.setName(param.getName());
        entity.setRemark(param.getRemark());
        entity.setMenuIds(String.join(StringPool.COMMA, param.getMenuIdList()));
        return save(entity);
    }

    @Override
    @CacheEvict(cacheNames = CacheConstant.SYS_CACHE, key = "'" + SystemCache.TENANT_PACKAGE_DETAIL_ID + "'+ #param.id")
    public boolean edit(TenantPackageSubmitParam param) {
        TenantPackage entity = new TenantPackage();
        entity.setId(param.getId());
        entity.setName(param.getName());
        entity.setRemark(param.getRemark());
        entity.setMenuIds(String.join(StringPool.COMMA, param.getMenuIdList()));
        return updateById(entity);
    }

    @Override
    @Cacheable(cacheNames = CacheConstant.SYS_CACHE, key = "'" + SystemCache.TENANT_PACKAGE_DETAIL_ID + "'+ #id")
    public TenantPackageDetailVO detail(Long id) {
        TenantPackage byId = this.getById(id);
        if (byId == null) {
            return null;
        }
        TenantPackageDetailVO vo = new TenantPackageDetailVO();
        vo.setId(byId.getId());
        vo.setName(byId.getName());
        vo.setRemark(byId.getRemark());
        if (StringUtil.isNotBlank(byId.getMenuIds())) {
            vo.setMenuIdList(StringUtil.toListStr(byId.getMenuIds(), StringPool.COMMA));
        }
        return vo;
    }

    @Override
    public boolean removeById(Long id) {
        Long count = tenantMapper.selectCount(Wrappers.<Tenant>lambdaQuery()
                .eq(Tenant::getPackageId, id)
                .eq(BaseEntity::getIsDeleted, BedrockDBConstant.DB_NOT_DELETED));
        if (count > 0) {
            throw new ServiceException(SystemErrorEnum.PACKAGE_HAS_TENANT);
        }
        return logicRemoveById(id);
    }

    @Override
    public IPage<TenantPackage> pageTenantPackage(IPage<TenantPackage> page, TenantPackage param) {
        return page.setRecords(baseMapper.listTenantPackage(page, param));
    }

    @Override
    public List<TenantPackage> listTenantPackage(TenantPackage param) {
        return baseMapper.listTenantPackage(null, param);
    }
}
