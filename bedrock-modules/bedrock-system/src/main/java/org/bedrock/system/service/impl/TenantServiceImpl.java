package org.bedrock.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bedrock.common.auth.constant.RoleAliasConstant;
import org.bedrock.common.auth.util.AuthUtil;
import org.bedrock.common.code.constant.CacheConstant;
import org.bedrock.common.code.util.BeanUtil;
import org.bedrock.common.code.util.CacheUtil;
import org.bedrock.common.code.util.RandomUtil;
import org.bedrock.common.log.operation.support.LogRecordContext;
import org.bedrock.common.mybatisplus.base.BaseEntity;
import org.bedrock.common.mybatisplus.base.BaseServiceImpl;
import org.bedrock.common.mybatisplus.constant.BedrockDBConstant;
import org.bedrock.common.tenant.annotation.TenantIgnore;
import org.bedrock.system.cache.SystemCache;
import org.bedrock.system.entity.*;
import org.bedrock.system.mapper.TenantMapper;
import org.bedrock.system.param.TenantEditParam;
import org.bedrock.system.param.TenantEnableParam;
import org.bedrock.system.param.TenantListParam;
import org.bedrock.system.param.TenantSubmitParam;
import org.bedrock.system.service.IAdminService;
import org.bedrock.system.service.IDeptService;
import org.bedrock.system.service.IRoleService;
import org.bedrock.system.service.ITenantService;
import org.bedrock.system.util.PasswordUtil;
import org.bedrock.system.vo.TenantDetailVO;
import org.bedrock.system.vo.TenantListVO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@TenantIgnore
@RequiredArgsConstructor
public class TenantServiceImpl extends BaseServiceImpl<TenantMapper, Tenant> implements ITenantService {

    private final IAdminService iAdminService;

    private final IDeptService iDeptService;

    private final IRoleService iRoleService;

    @Override
    @Transactional
    public boolean submit(TenantSubmitParam param) {
        Tenant tenant = BeanUtil.copyProperties(param, Tenant.class);
        /**
         * 赋值租户信息
         */
        buildTenant(tenant, AuthUtil.getUserId());
        /**
         * 创建部门
         */
        Dept dept = buildDept(tenant, AuthUtil.getUserId());

        /**
         * 创建角色
         */
        Role role = buildRole(tenant, AuthUtil.getUserId());

        /**
         * 创建管理员
         */
        Admin admin = buildAdmin(tenant, param, AuthUtil.getUserId());

        iAdminService.save(admin);
        iDeptService.save(dept);
        iRoleService.save(role);
        iAdminService.saveAdminDept(admin.getId(), Collections.singletonList(dept.getId()));
        iAdminService.saveAdminRole(admin.getId(), Collections.singletonList(role.getId()));
        boolean save = save(tenant);
        initTenant(tenant);
        return save;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheConstant.SYS_CACHE, key = "'" + SystemCache.TENANT_DETAIL_ID + "'+ #param.id"),
            @CacheEvict(cacheNames = CacheConstant.SYS_CACHE, key = "'" + SystemCache.TENANT_DETAIL_TENANT_ID + "'+ #param.tenantId")
    })
    public boolean edit(TenantEditParam param) {
        Tenant tenant = BeanUtil.copyProperties(param, Tenant.class);
        tenant.setTenantId(null);
        return updateById(tenant);
    }

    @Override
    public IPage<TenantListVO> pageTenant(IPage<TenantListVO> page, TenantListParam param) {
        return page.setRecords(baseMapper.selectListTenantList(page, param));
    }

    @Override
    public List<TenantListVO> listTenant(TenantListParam param) {
        return baseMapper.selectListTenantList(null, param);
    }

    @Override
    @Cacheable(cacheNames = CacheConstant.SYS_CACHE, key = "'" + SystemCache.TENANT_DETAIL_ID + "'+ #id")
    public TenantDetailVO detail(Long id) {
        return this.baseMapper.selectDetailById(id);
    }

    @Override
    @Cacheable(cacheNames = CacheConstant.SYS_CACHE, key = "'" + SystemCache.TENANT_DETAIL_TENANT_ID + "'+ #tenantId")
    public TenantDetailVO info(String tenantId) {
        return this.baseMapper.selectDetailByTenantId(tenantId);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheConstant.SYS_CACHE, key = "'" + SystemCache.TENANT_DETAIL_ID + "'+ #param.id")
    })
    public boolean enableStatus(TenantEnableParam param) {
        Tenant tenant = this.getById(param.getId());
        LogRecordContext.putVariable("tenant", tenant);
        CacheUtil.evict(CacheConstant.SYS_CACHE, SystemCache.TENANT_DETAIL_TENANT_ID + tenant.getTenantId());
        return this.update(Wrappers.<Tenant>lambdaUpdate()
                .eq(BaseEntity::getId, param.getId())
                .set(Tenant::getStatus, param.getStatus())
                .set(Tenant::getDisableReason, param.getDisableReason())
                .set(BaseEntity::getUpdateUserId, AuthUtil.getUserId())
                .set(BaseEntity::getUpdateTime, LocalDateTime.now()));
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheConstant.SYS_CACHE, key = "'" + SystemCache.TENANT_DETAIL_ID + "'+ #id"),
    })
    public boolean deleteById(Long id) {
        Tenant tenant = this.getById(id);
        LogRecordContext.putVariable("tenant", tenant);
        CacheUtil.evict(CacheConstant.SYS_CACHE, SystemCache.TENANT_DETAIL_TENANT_ID + tenant.getTenantId());
        return logicRemoveById(id);
    }

    /**
     * 创建租户
     *
     * @param tenant
     * @param userId
     */
    public void buildTenant(Tenant tenant, Long userId) {
        tenant.setId(IdWorker.getId());
        tenant.setCreateUserId(userId);
        tenant.setUpdateUserId(userId);
        /**
         * 生成租户id
         */
        List<Tenant> tenants = this.list(Wrappers.<Tenant>lambdaQuery()
                .eq(Tenant::getIsDeleted, BedrockDBConstant.DB_NOT_DELETED));
        List<String> codes = tenants.stream().map(Tenant::getTenantId).collect(Collectors.toList());
        String tenantId = generateTenantId(codes);
        tenant.setTenantId(tenantId);
    }

    /**
     * 创建 部门
     */
    private Dept buildDept(Tenant tenant, Long userId) {
        Dept dept = new Dept();
        dept.setId(IdWorker.getId());
        dept.setParentId(BedrockDBConstant.DB_TOP_PARENT_ID);
        dept.setTenantId(tenant.getTenantId());
        dept.setDeptName(tenant.getTenantName());
        dept.setLevel(1);
        dept.setAncestors(dept.getId().toString());
        dept.setCategory(1);
        dept.setSort(1);
        dept.setCreateUserId(userId);
        dept.setUpdateUserId(userId);
        return dept;
    }

    /**
     * 构建角色
     */
    private Role buildRole(Tenant tenant, Long userId) {
        Role role = new Role();
        role.setId(IdWorker.getId());
        role.setTenantId(tenant.getTenantId());
        role.setRoleName("管理员");
        role.setRoleAlias(RoleAliasConstant.ROLE_ADMIN);
        role.setSort(1);
        role.setCreateUserId(userId);
        role.setUpdateUserId(userId);
        return role;
    }

    /**
     * 创建管理员
     */
    private Admin buildAdmin(Tenant tenant, TenantSubmitParam param, Long userId) {
        // 创建管理员
        Admin admin = new Admin();
        admin.setId(IdWorker.getId());
        admin.setTenantId(tenant.getTenantId());
        admin.setUsername(param.getUsername());
        admin.setPhone(tenant.getContactPhone());
        admin.setEmail(tenant.getContactEmail());
        admin.setPassword(PasswordUtil.encryptionPassword(param.getPassword()));
        admin.setStatus(BedrockDBConstant.DB_STATUS_NORMAL);
        admin.setSex("M");
        admin.setNickname("管理员");
        admin.setCreateUserId(userId);
        admin.setUpdateUserId(userId);
        return admin;
    }

    /**
     * 生成租户id
     */
    private String generateTenantId(List<String> codes) {
        // 随机生成6位
        String numbers = RandomUtil.random(6, RandomUtil.RandomTypeEnum.NUMBER);
        // 判断是否存在，如果存在则重新生成
        if (codes.contains(numbers)) {
            return generateTenantId(codes);
        }
        return numbers;
    }

    /**
     * 新增后租户调用该方法初始化数据
     */
    public void initTenant(Tenant tenant) {

    }
}
