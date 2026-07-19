package org.bedrock.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.bedrock.common.auth.constant.RoleAliasConstant;
import org.bedrock.common.auth.util.AuthUtil;
import org.bedrock.common.code.constant.CacheConstant;
import org.bedrock.common.code.util.BeanUtil;
import org.bedrock.common.code.util.CollectionUtil;
import org.bedrock.common.datascope.support.DefaultScopeModelSupport;
import org.bedrock.common.log.exception.ServiceException;
import org.bedrock.common.mybatisplus.base.BaseEntity;
import org.bedrock.common.mybatisplus.base.BaseServiceImpl;
import org.bedrock.common.security.expression.SecurityExpressionExpressionRoot;
import org.bedrock.system.cache.SystemCache;
import org.bedrock.system.entity.Role;
import org.bedrock.system.entity.RoleMenu;
import org.bedrock.system.entity.RolePermission;
import org.bedrock.system.enums.SystemErrorEnum;
import org.bedrock.system.mapper.RoleMapper;
import org.bedrock.system.mapper.RoleMenuMapper;
import org.bedrock.system.mapper.RolePermissionMapper;
import org.bedrock.system.param.RoleDetailPermissionParam;
import org.bedrock.system.param.RoleListParam;
import org.bedrock.system.param.RoleSubmitParam;
import org.bedrock.system.param.RoleSubmitPermissionParam;
import org.bedrock.system.service.IRoleService;
import org.bedrock.system.vo.RoleDetailVO;
import org.bedrock.system.vo.RoleListVO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends BaseServiceImpl<RoleMapper, Role> implements IRoleService {

    private final RoleMenuMapper roleMenuMapper;

    private final RolePermissionMapper rolePermissionMapper;

    @Override
    @Cacheable(cacheNames = CacheConstant.ADMIN_CACHE, key = "'role:list:adminid:'+#adminId")
    public List<Role> selectRoleListByAdminId(Long adminId) {
        return baseMapper.selectRoleListByAdminId(adminId);
    }

    @Override
    public boolean submit(RoleSubmitParam param) {
        if (RoleAliasConstant.ROLE_ADMINISTRATOR.equals(param.getRoleAlias())) {
            throw new ServiceException(SystemErrorEnum.NOT_AUTH_CREATE_SUPER_ADMIN.getCode(), SystemErrorEnum.NOT_AUTH_CREATE_SUPER_ADMIN.getMessage());
        }
        if (RoleAliasConstant.ROLE_ADMIN.equals(param.getRoleAlias())) {
            throw new ServiceException(SystemErrorEnum.NOT_AUTH_CREATE_ADMIN.getCode(), SystemErrorEnum.NOT_AUTH_CREATE_ADMIN.getMessage());
        }
        Role role = BeanUtil.copyProperties(param, getEntityClass());
        if (exists(Wrappers.<Role>lambdaQuery()
                .eq(Role::getRoleName, param.getRoleName())
                .or()
                .eq(Role::getRoleAlias, param.getRoleAlias()))) {
            throw new ServiceException(SystemErrorEnum.THE_NAME_ALREADY_EXISTS.getCode(), SystemErrorEnum.THE_NAME_ALREADY_EXISTS.getMessage());
        }
        role.setId(IdWorker.getId());

        return save(role);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheConstant.ADMIN_CACHE, allEntries = true)
    })
    public boolean edit(RoleSubmitParam param) {
        if (RoleAliasConstant.ROLE_ADMINISTRATOR.equals(param.getRoleAlias())) {
            throw new ServiceException(SystemErrorEnum.NOT_AUTH_CREATE_SUPER_ADMIN.getCode(), SystemErrorEnum.NOT_AUTH_CREATE_SUPER_ADMIN.getMessage());
        }
        if (RoleAliasConstant.ROLE_ADMIN.equals(param.getRoleAlias())) {
            throw new ServiceException(SystemErrorEnum.NOT_AUTH_CREATE_ADMIN.getCode(), SystemErrorEnum.NOT_AUTH_CREATE_ADMIN.getMessage());
        }
        Role role = BeanUtil.copyProperties(param, getEntityClass());
        if (exists(Wrappers.<Role>lambdaQuery()
                .and(wrapper -> wrapper
                        .eq(Role::getRoleName, param.getRoleName())
                        .ne(BaseEntity::getId, role.getId()))
                .or()
                .and(wrapper -> wrapper
                        .eq(Role::getRoleAlias, param.getRoleAlias())
                        .ne(BaseEntity::getId, role.getId()))
        )) {
            throw new ServiceException(SystemErrorEnum.THE_NAME_ALREADY_EXISTS.getCode(), SystemErrorEnum.THE_NAME_ALREADY_EXISTS.getMessage());
        }
        return updateById(role);
    }

    @Override
    public boolean removeById(Long id) {
        return logicRemoveById(id);
    }

    @Override
    @Cacheable(cacheNames = CacheConstant.SYS_CACHE, key = "'" + SystemCache.ROLE_DETAIL_ID + "' + #id")
    public RoleDetailVO detail(Long id) {
        RoleDetailVO roleDetailVO = baseMapper.selectRoleDetailById(id);

        return baseMapper.selectRoleDetailById(id);
    }

    @Override
    public IPage<RoleListVO> rolePage(IPage<RoleListVO> iPage, RoleListParam param) {
        /**
         * 排除特殊角色查询
         */
        List<String> neRoleAlias = !AuthUtil.isAdministrator() ? Arrays.asList(RoleAliasConstant.ROLE_ADMINISTRATOR, RoleAliasConstant.ROLE_ADMIN) : Collections.singletonList(RoleAliasConstant.ROLE_ADMINISTRATOR);
        return iPage.setRecords(baseMapper.selectRoleList(iPage, param, neRoleAlias));
    }

    @Override
    public List<RoleListVO> roleList(RoleListParam param) {
        /**
         * 排除特殊角色查询
         */
        List<String> neRoleAlias = !AuthUtil.isAdministrator() ? Arrays.asList(RoleAliasConstant.ROLE_ADMINISTRATOR, RoleAliasConstant.ROLE_ADMIN) : Collections.singletonList(RoleAliasConstant.ROLE_ADMINISTRATOR);
        return baseMapper.selectRoleList(null, param, neRoleAlias);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheConstant.ADMIN_CACHE, allEntries = true)
    })
    public boolean saveMenuIds(RoleSubmitPermissionParam param) {
        roleMenuMapper.delete(Wrappers.<RoleMenu>lambdaQuery()
                .eq(RoleMenu::getRoleId, param.getRoleId()));
        if (CollectionUtil.isNotEmpty(param.getPermissionIds())) {
            List<RoleMenu> list = param.getPermissionIds()
                    .stream()
                    .map(menuId -> {
                        RoleMenu roleMenu = new RoleMenu();
                        roleMenu.setId(IdWorker.getId());
                        roleMenu.setMenuId(menuId);
                        roleMenu.setRoleId(param.getRoleId());
                        return roleMenu;
                    })
                    .toList();
            roleMenuMapper.insertBatch(list);
        }
        return true;
    }

    @Override
    public List<String> selectMenuIdsByRoleId(Long roleId) {
        return roleMenuMapper.selectMenuIdsByRoleId(roleId);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheConstant.SCOPE_CACHE, key = "'" + DefaultScopeModelSupport.SCOPE_DATA_SCOPE_MAPPER_ROLE_PREFIX + "' + #param.roleId"),
            @CacheEvict(cacheNames = CacheConstant.API_CACHE, key = "'" + SecurityExpressionExpressionRoot.API_PERMISSION_PREFIX + "' + #param.roleId")
    })
    public boolean savePermission(RoleSubmitPermissionParam param) {
        rolePermissionMapper.delete(Wrappers.<RolePermission>lambdaQuery()
                .eq(RolePermission::getRoleId, param.getRoleId())
                .eq(RolePermission::getPermType, param.getPermType()));
        List<RolePermission> list = param.getPermissionIds()
                .stream()
                .map(permissionId -> {
                    RolePermission rolePermission = new RolePermission();
                    rolePermission.setId(IdWorker.getId());
                    rolePermission.setPermissionId(permissionId);
                    rolePermission.setRoleId(param.getRoleId());
                    rolePermission.setPermType(param.getPermType());
                    return rolePermission;
                }).toList();
        return rolePermissionMapper.insertBatch(list) > 0;
    }

    @Override
    public List<String> selectPermissionIdsByRoleId(RoleDetailPermissionParam param) {
        return rolePermissionMapper.selectPermissionIdsByRoleId(param);
    }
}
