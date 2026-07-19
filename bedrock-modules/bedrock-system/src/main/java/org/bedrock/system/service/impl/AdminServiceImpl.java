package org.bedrock.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.bedrock.common.auth.constant.RoleAliasConstant;
import org.bedrock.common.auth.util.AuthUtil;
import org.bedrock.common.code.constant.CacheConstant;
import org.bedrock.common.code.util.BeanUtil;
import org.bedrock.common.code.util.CacheUtil;
import org.bedrock.common.code.util.StringUtil;
import org.bedrock.common.log.exception.ServiceException;
import org.bedrock.common.log.operation.support.LogRecordContext;
import org.bedrock.common.mybatisplus.base.BaseEntity;
import org.bedrock.common.mybatisplus.base.BaseServiceImpl;
import org.bedrock.common.tenant.base.TenantEntity;
import org.bedrock.system.cache.SystemCache;
import org.bedrock.system.constant.ParamConstant;
import org.bedrock.system.dto.LoginInfo;
import org.bedrock.system.entity.*;
import org.bedrock.system.excel.AdminExportExcel;
import org.bedrock.system.excel.AdminImportExcel;
import org.bedrock.system.mapper.AdminDeptMapper;
import org.bedrock.system.mapper.AdminMapper;
import org.bedrock.system.mapper.AdminRoleMapper;
import org.bedrock.system.param.AdminPageParam;
import org.bedrock.system.param.AdminProfileUpdateParam;
import org.bedrock.system.param.AdminSubmitParam;
import org.bedrock.system.service.IAdminService;
import org.bedrock.system.service.IDeptService;
import org.bedrock.system.service.IRoleService;
import org.bedrock.system.util.PasswordUtil;
import org.bedrock.system.vo.AdminDetailVO;
import org.bedrock.system.vo.AdminListVO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl extends BaseServiceImpl<AdminMapper, Admin> implements IAdminService {

    private final AdminDeptMapper adminDeptMapper;

    private final AdminRoleMapper adminRoleMapper;

    private final IDeptService deptService;

    private final IRoleService roleService;

    @Override
    @CacheEvict(cacheNames = CacheConstant.ADMIN_CACHE, key = "'detail:'+ #param.username")
    public boolean submit(AdminSubmitParam param) {
        Admin admin = BeanUtil.copyProperties(param, Admin.class);
        if (StringUtil.isNotBlank(admin.getPassword())) {
            admin.setPassword(PasswordUtil.encryptionPassword(param.getPassword()));
        }
        admin.setId(IdWorker.getId());
        return save(admin) && saveAdminDept(admin.getId(), param.getDeptIds()) && saveAdminRole(admin.getId(), param.getRoleIds());
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheConstant.ADMIN_CACHE, key = "'" + SystemCache.ADMIN_DETAIL_ID + "'+ #param.id"),
            @CacheEvict(cacheNames = CacheConstant.ADMIN_CACHE, key = "'menu:auth:userid' + #param.id"),
            @CacheEvict(cacheNames = CacheConstant.ADMIN_CACHE, key = "'dept:list:adminid:'+#param.id"),
            @CacheEvict(cacheNames = CacheConstant.ADMIN_CACHE, key = "'role:list:adminid:'+#param.id")
    })
    public boolean edit(AdminSubmitParam param) {
        Admin adminDetail = this.getById(param.getId());
        if (adminDetail == null) {
            return true;
        }
        CacheUtil.evict(CacheConstant.ADMIN_CACHE, "detail:tenantid" + adminDetail.getTenantId() + ":username:" + adminDetail.getUsername());
        Admin admin = BeanUtil.copyProperties(param, Admin.class);
        admin.setPassword(null);
        admin.setUsername(null);
        admin.setPassword(null);
        return updateById(admin) && saveAdminDept(admin.getId(), param.getDeptIds()) && saveAdminRole(admin.getId(), param.getRoleIds());
    }

    @Override
    @Cacheable(cacheNames = CacheConstant.ADMIN_CACHE, key = "'" + SystemCache.ADMIN_DETAIL_ID + "'+ #id")
    public AdminDetailVO detail(Long id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheConstant.ADMIN_CACHE, key = "'" + SystemCache.ADMIN_DETAIL_ID + "'+ #id"),
            @CacheEvict(cacheNames = CacheConstant.ADMIN_CACHE, key = "'dept:list:adminid:'+#id"),
            @CacheEvict(cacheNames = CacheConstant.ADMIN_CACHE, key = "'role:list:adminid:'+#id")
    })
    public boolean removeById(Long id) {
        Admin admin = this.getById(id);
        if (admin == null) {
            return true;
        }
        LogRecordContext.putVariable("username", admin.getUsername());
        CacheUtil.evict(CacheConstant.ADMIN_CACHE, "detail:tenantid" + admin.getTenantId() + ":username:" + admin.getUsername());
        return logicRemoveById(id);
    }

    @Override
    public IPage<AdminListVO> pageAdmin(IPage<AdminListVO> page, AdminPageParam param) {
        List<String> noRoleAlias = AuthUtil.isAdministrator() ?
                Collections.singletonList(RoleAliasConstant.ROLE_ADMINISTRATOR) :
                List.of(RoleAliasConstant.ROLE_ADMINISTRATOR, RoleAliasConstant.ROLE_ADMIN);
        return page.setRecords(baseMapper.selectAdminDetailList(page, param, noRoleAlias));
    }

    @Override
    public List<AdminListVO> listAdmin(AdminPageParam param) {
        List<String> noRoleAlias = AuthUtil.isAdministrator() ?
                Collections.singletonList(RoleAliasConstant.ROLE_ADMINISTRATOR) :
                List.of(RoleAliasConstant.ROLE_ADMINISTRATOR, RoleAliasConstant.ROLE_ADMIN);
        return baseMapper.selectAdminDetailList(null, param, noRoleAlias);
    }

    @Override
    public boolean enableStatus(Long id, Integer status) {
        Admin admin = this.getById(id);
        LogRecordContext.putVariable("username", admin.getUsername());
        CacheUtil.evict(CacheConstant.ADMIN_CACHE, "detail:tenantid" + admin.getTenantId() + ":username:" + admin.getUsername());
        return this.update(Wrappers.<Admin>lambdaUpdate()
                .eq(BaseEntity::getId, id)
                .set(Admin::getStatus, status));
    }

    @Override
    public boolean updateProfile(AdminProfileUpdateParam param) {
        Long id = AuthUtil.getUserId();
        Admin admin = this.getById(id);
        if (admin == null) {
            return false;
        }
        LogRecordContext.putVariable("username", admin.getUsername());
        CacheUtil.evict(CacheConstant.ADMIN_CACHE, SystemCache.ADMIN_DETAIL_ID + id);
        CacheUtil.evict(CacheConstant.ADMIN_CACHE, "menu:auth:userid" + id);
        CacheUtil.evict(CacheConstant.ADMIN_CACHE, "detail:tenantid" + admin.getTenantId() + ":username:" + admin.getUsername());
        return this.update(Wrappers.<Admin>lambdaUpdate()
                .eq(BaseEntity::getId, id)
                .set(Admin::getAvatar, param.getAvatar())
                .set(Admin::getNickname, param.getNickname())
                .set(Admin::getSex, param.getSex())
                .set(Admin::getPhone, param.getPhone())
                .set(Admin::getEmail, param.getEmail())
                .set(Admin::getRemark, param.getRemark()));
    }

    @Override
    public boolean updatePassword(Long id, String oldPassword, String newPassword) {
        Admin admin = this.getById(id);
        if (!PasswordUtil.encryptionPassword(oldPassword).equals(admin.getPassword())) {
            throw new ServiceException("旧密码错误");
        }
        /**
         * 登录用户名
         */
        LogRecordContext.putVariable("username", admin.getUsername());
        CacheUtil.evict(CacheConstant.ADMIN_CACHE, "detail:username:" + admin.getUsername());
        newPassword = PasswordUtil.encryptionPassword(newPassword);
        return this.update(Wrappers.<Admin>lambdaUpdate()
                .eq(BaseEntity::getId, id)
                .set(Admin::getPassword, newPassword));
    }

    @Override
    public boolean resetPassword(List<Long> ids, String password) {
        ids.forEach(id -> {
            Admin admin = this.getById(id);
            CacheUtil.evict(CacheConstant.ADMIN_CACHE, "detail:tenantid" + admin.getTenantId() + ":username:" + admin.getUsername());
            this.update(Wrappers.<Admin>lambdaUpdate()
                    .eq(BaseEntity::getId, id)
                    .set(Admin::getPassword, PasswordUtil.encryptionPassword(password)));
        });
        return true;
    }

    @Override
    @Cacheable(cacheNames = CacheConstant.ADMIN_CACHE, key = "'detail:tenantid'+#tenantId+':username:' + #username")
    public LoginInfo loginInfo(String username, String tenantId) {
        Admin admin = this.getOne(Wrappers.<Admin>lambdaQuery()
                .eq(TenantEntity::getTenantId, tenantId)
                .eq(Admin::getUsername, username));
        if (admin == null) {
            return null;
        }
        return new LoginInfo(admin, null,
                deptService.selectDeptListByAdminId(admin.getId()), roleService.selectRoleListByAdminId(admin.getId()));
    }

    @Override
    public boolean saveAdminDept(Long adminId, List<Long> deptIds) {
        this.adminDeptMapper.delete(Wrappers.<AdminDept>lambdaQuery()
                .eq(AdminDept::getAdminId, adminId));
        List<AdminDept> deptList = deptIds.stream().map(deptId -> {
            AdminDept adminDept = new AdminDept();
            adminDept.setId(IdWorker.getId());
            adminDept.setAdminId(adminId);
            adminDept.setDeptId(deptId);
            return adminDept;
        }).toList();
        return this.adminDeptMapper.insertAdminDeptBatch(deptList) > 0;
    }

    @Override
    public boolean saveAdminRole(Long adminId, List<Long> roleIds) {
        this.adminRoleMapper.delete(Wrappers.<AdminRole>lambdaQuery()
                .eq(AdminRole::getAdminId, adminId));
        List<AdminRole> list = roleIds.stream().map(roleId -> {
            AdminRole adminRole = new AdminRole();
            adminRole.setId(IdWorker.getId());
            adminRole.setAdminId(adminId);
            adminRole.setRoleId(roleId);
            return adminRole;
        }).toList();
        return this.adminRoleMapper.insertAdminRoleBatch(list) > 0;
    }

    @Override
    @Transactional
    public boolean importExcel(List<AdminImportExcel> list) {
        Map<String, Long> deptMap = new HashMap<>();
        Map<String, Long> roleMap = new HashMap<>();
        for (AdminImportExcel excel : list) {
            AdminSubmitParam param = new AdminSubmitParam();
            param.setUsername(excel.getUsername());
            param.setPassword(PasswordUtil.md5Password(SystemCache.getParamValue(ParamConstant.INIT_ADMIN_PASSWORD)));
            param.setNickname(excel.getNickname());
            param.setSex(excel.getSex());
            param.setPhone(excel.getPhone());
            param.setEmail(excel.getEmail());
            param.setRemark(excel.getRemark());
            /**
             * 根据部门编号查询部门id
             */
            Long deptId = deptMap.computeIfAbsent(excel.getDeptCode(), _deptCode -> {
                if (StringUtil.isBlank(_deptCode)) {
                    return 0L;
                }
                List<Dept> deptList = deptService.list(Wrappers.<Dept>lambdaQuery()
                        .eq(Dept::getDeptCode, _deptCode));
                return deptList == null || deptList.isEmpty() ? 0L : deptList.get(0).getId();
            });
            if (deptId != 0L) {
                param.setDeptIds(List.of(deptId));
            }
            /**
             * 根据角色别名查询角色id
             */
            Long roleId = roleMap.computeIfAbsent(excel.getRoleAlias(), _roleAlias -> {
                if (StringUtil.isBlank(_roleAlias)) {
                    return 0L;
                }
                if (_roleAlias.equals(RoleAliasConstant.ROLE_ADMINISTRATOR) || _roleAlias.equals(RoleAliasConstant.ROLE_ADMIN)) {
                    return 0L;
                }
                List<Role> roleList = roleService.list(Wrappers.<Role>lambdaQuery()
                        .eq(Role::getRoleAlias, _roleAlias));
                return roleList == null || roleList.isEmpty() ? 0L : roleList.get(0).getId();
            });
            if (roleId != 0L) {
                param.setRoleIds(List.of(roleId));
            }
            submit(param);
        }
        return true;
    }

    @Override
    public List<AdminExportExcel> exportExcel(AdminPageParam param) {
        List<String> noRoleAlias = AuthUtil.isAdministrator() ?
                Collections.singletonList(RoleAliasConstant.ROLE_ADMINISTRATOR) :
                List.of(RoleAliasConstant.ROLE_ADMINISTRATOR, RoleAliasConstant.ROLE_ADMIN);
        return baseMapper.selectExcelList(param, noRoleAlias);
    }
}
