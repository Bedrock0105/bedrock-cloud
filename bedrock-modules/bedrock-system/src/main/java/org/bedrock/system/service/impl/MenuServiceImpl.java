package org.bedrock.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.bedrock.common.auth.util.AuthUtil;
import org.bedrock.common.code.constant.CacheConstant;
import org.bedrock.common.code.constant.StringPool;
import org.bedrock.common.code.util.BeanUtil;
import org.bedrock.common.code.util.CollectionUtil;
import org.bedrock.common.code.util.ObjectUtil;
import org.bedrock.common.code.util.TreeUtil;
import org.bedrock.common.log.exception.ServiceException;
import org.bedrock.common.mybatisplus.base.BaseEntity;
import org.bedrock.common.mybatisplus.base.BaseServiceImpl;
import org.bedrock.common.mybatisplus.constant.BedrockDBConstant;
import org.bedrock.system.cache.SystemCache;
import org.bedrock.system.entity.Menu;
import org.bedrock.system.enums.MenuTypeEnum;
import org.bedrock.system.enums.SystemErrorEnum;
import org.bedrock.system.mapper.MenuMapper;
import org.bedrock.system.param.MenuSubmitParam;
import org.bedrock.system.service.IMenuService;
import org.bedrock.system.service.ITenantPackageService;
import org.bedrock.system.vo.MenuDetailVO;
import org.bedrock.system.vo.MenuTreeVO;
import org.bedrock.system.vo.TenantDetailVO;
import org.bedrock.system.vo.TenantPackageDetailVO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl extends BaseServiceImpl<MenuMapper, Menu> implements IMenuService {

    private final ITenantPackageService packageService;

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheConstant.ADMIN_CACHE, allEntries = true)
    })
    public boolean submit(MenuSubmitParam param) {
        if (exists(Wrappers.<Menu>lambdaQuery()
                .eq(param.getMenuType() == MenuTypeEnum.BUTTON.getCode(), Menu::getParentId, param.getParentId())
                .eq(Menu::getMenuCode, param.getMenuCode()))) {
            throw new ServiceException(SystemErrorEnum.MENU_CODE_EXISTS.getCode(), SystemErrorEnum.MENU_CODE_EXISTS.getMessage());
        }
        Menu menu = BeanUtil.copyProperties(param, Menu.class);
        menu.setId(IdWorker.getId());
        if (menu.getParentId() == null || menu.getParentId() == BedrockDBConstant.DB_TOP_PARENT_ID) {
            menu.setParentId(BedrockDBConstant.DB_TOP_PARENT_ID);
            menu.setAncestors(menu.getId().toString());
        } else {
            Menu parentMenu = baseMapper.selectById(menu.getParentId());
            menu.setAncestors(parentMenu.getAncestors() + StringPool.COMMA + menu.getId());
        }
        return save(menu);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheConstant.ADMIN_CACHE, allEntries = true)
    })
    public boolean edit(MenuSubmitParam param) {
        if (exists(Wrappers.<Menu>lambdaQuery()
                .eq(Menu::getParentId, ObjectUtil.isEmpty(param.getParentId()) ? BedrockDBConstant.DB_TOP_PARENT_ID : param.getParentId())
                .eq(Menu::getMenuCode, param.getMenuCode())
                .ne(BaseEntity::getId, param.getId()))) {
            throw new ServiceException(SystemErrorEnum.MENU_CODE_EXISTS.getCode(), SystemErrorEnum.MENU_CODE_EXISTS.getMessage());
        }
        Menu menu = BeanUtil.copyProperties(param, Menu.class);
        if (menu.getParentId() == null || menu.getParentId() == BedrockDBConstant.DB_TOP_PARENT_ID) {
            menu.setParentId(BedrockDBConstant.DB_TOP_PARENT_ID);
            menu.setAncestors(menu.getId().toString());
        } else {
            Menu parentMenu = baseMapper.selectById(menu.getParentId());
            menu.setAncestors(parentMenu.getAncestors() + StringPool.COMMA + menu.getId());
        }
        return updateById(menu);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheConstant.ADMIN_CACHE, allEntries = true)
    })
    public boolean removeById(Long id) {
        if (exists(Wrappers.<Menu>lambdaQuery()
                .eq(Menu::getParentId, id)
                .eq(BaseEntity::getIsDeleted, BedrockDBConstant.DB_NOT_DELETED))) {
            throw new ServiceException(SystemErrorEnum.MENU_HAS_CHILD);
        }
        return logicRemoveById(id);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheConstant.ADMIN_CACHE, allEntries = true)
    })
    public boolean enableStatus(Long id, Integer status) {
        return this.update(Wrappers.<Menu>lambdaUpdate()
                .like(status == BedrockDBConstant.DB_STATUS_DISABLE, Menu::getAncestors, id)
                .eq(status == BedrockDBConstant.DB_STATUS_NORMAL, Menu::getId, id)
                .set(BaseEntity::getUpdateTime, LocalDateTime.now())
                .set(BaseEntity::getUpdateUserId, AuthUtil.getUserId())
                .set(Menu::getStatus, status));
    }

    @Override
    public MenuDetailVO detail(Long id) {
        return baseMapper.selectDetail(id);
    }

    @Override
    @Cacheable(cacheNames = CacheConstant.ADMIN_CACHE, key = "'menu:auth:userid'+#userId")
    public List<MenuTreeVO> routers(Long userId) {
        List<MenuTreeVO> menuTreeVOS = null;
        if (AuthUtil.isAdministrator()) {
            menuTreeVOS = baseMapper.selectMenuByRoleIds(null);
        } else if (AuthUtil.isAdmin()) {
            List<MenuTreeVO> menuTreeVOAll = baseMapper.selectMenuByRoleIds(null);
            menuTreeVOS = combineWithParents(menuTreeVOAll, filterPackage(menuTreeVOAll));
        } else {
            List<Long> roleIds = AuthUtil.getRoleIds();
            if (CollectionUtil.isEmpty(roleIds)) {
                return Collections.emptyList();
            }
            menuTreeVOS = combineWithParents(baseMapper.selectMenuByRoleIds(null), filterPackage(baseMapper.selectMenuByRoleIds(roleIds)));
        }
        return TreeUtil.buildTree(menuTreeVOS);
    }

    /**
     * 过滤产品包菜单
     */
    public List<MenuTreeVO> filterPackage(List<MenuTreeVO> menuTreeVOS) {
        TenantDetailVO tenantDetail = SystemCache.getTenantDetail(AuthUtil.getTenantId());
        TenantPackageDetailVO detail = packageService.detail(tenantDetail.getPackageId());
        return menuTreeVOS.stream()
                .filter(menu -> detail.getMenuIdList().contains(menu.getId()))
                .collect(Collectors.toList());
    }

    /**
     * 组合过滤后的菜单及其所有上级菜单
     *
     * @param allMenus      全部菜单（包含完整层级）
     * @param filteredMenus 按角色权限过滤后的菜单（可能缺少上级）
     * @return 包含过滤菜单及其所有上级的完整集合
     */
    public List<MenuTreeVO> combineWithParents(List<MenuTreeVO> allMenus, List<MenuTreeVO> filteredMenus) {
        // 1. 构建"菜单ID->菜单对象"的映射，方便快速查找上级
        Map<Long, MenuTreeVO> idToMenuMap = allMenus.stream()
                .collect(Collectors.toMap(MenuTreeVO::getId, menu -> menu));

        // 2. 收集所有需要保留的菜单ID（过滤后的菜单 + 所有上级）
        Set<Long> requiredMenuIds = new HashSet<>();
        for (MenuTreeVO filteredMenu : filteredMenus) {
            // 递归收集当前菜单及其所有上级的ID
            collectAllParentIds(filteredMenu.getId(), idToMenuMap, requiredMenuIds);
        }
        // 3. 从全部菜单中筛选出需要保留的菜单，按原排序保持顺序
        return allMenus.stream()
                .filter(menu -> requiredMenuIds.contains(menu.getId()))
                .collect(Collectors.toList());
    }

    /**
     * 递归收集菜单ID及其所有上级ID
     *
     * @param currentMenuId   当前菜单ID
     * @param idToMenuMap     菜单ID映射表
     * @param requiredMenuIds 收集结果（存储需要保留的ID）
     */
    private void collectAllParentIds(Long currentMenuId, Map<Long, MenuTreeVO> idToMenuMap, Set<Long> requiredMenuIds) {
        // 1. 将当前菜单ID加入集合
        requiredMenuIds.add(currentMenuId);
        // 2. 查找上级菜单
        MenuTreeVO currentMenu = idToMenuMap.get(currentMenuId);
        if (currentMenu == null) {
            return; // 菜单不存在（数据异常），终止递归
        }

        Long parentId = currentMenu.getParentId();
        // 3. 终止条件：上级ID为顶级标识（如"0"或null），无需继续向上查找
        if (parentId == null || parentId.equals(BedrockDBConstant.DB_TOP_PARENT_ID)) {
            return;
        }
        // 4. 递归查找上级的上级
        collectAllParentIds(parentId, idToMenuMap, requiredMenuIds);
    }
}
