package org.bedrock.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.bedrock.common.auth.util.AuthUtil;
import org.bedrock.common.code.constant.CacheConstant;
import org.bedrock.common.code.util.CollectionUtil;
import org.bedrock.common.code.util.StringUtil;
import org.bedrock.common.code.util.TreeUtil;
import org.bedrock.common.log.operation.support.LogRecordContext;
import org.bedrock.common.mybatisplus.base.BaseEntity;
import org.bedrock.common.mybatisplus.base.BaseServiceImpl;
import org.bedrock.common.mybatisplus.constant.BedrockDBConstant;
import org.bedrock.system.entity.PermissionApi;
import org.bedrock.system.entity.PermissionDatascope;
import org.bedrock.system.mapper.PermissionApiMapper;
import org.bedrock.system.service.IPermissionApiService;
import org.bedrock.system.vo.PermissionTreeNode;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class PermissionApiServiceImpl extends BaseServiceImpl<PermissionApiMapper, PermissionApi> implements IPermissionApiService {

    @Override
    public boolean submit(PermissionApi permissionApi) {
        return save(permissionApi);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheConstant.API_CACHE, allEntries = true),
            @CacheEvict(cacheNames = CacheConstant.API_CACHE, allEntries = true)
    })
    public boolean edit(PermissionApi permissionApi) {
        return updateById(permissionApi);
    }

    @Override
    public PermissionApi detail(Long id) {
        return getById(id);
    }

    @Override
    public IPage<PermissionApi> page(IPage<PermissionApi> page, PermissionApi permissionApi) {
        return page(page, Wrappers.<PermissionApi>lambdaQuery()
                .eq(PermissionApi::getMenuId, permissionApi.getMenuId())
                .eq(BaseEntity::getIsDeleted, BedrockDBConstant.DB_NOT_DELETED)
                .like(StringUtil.isNotBlank(permissionApi.getPermission()), PermissionApi::getPermission, permissionApi.getPermission())
                .like(StringUtil.isNotBlank(permissionApi.getName()), PermissionApi::getName, permissionApi.getName()));
    }

    @Override
    public List<PermissionTreeNode> treePermissionApi() {
        List<PermissionTreeNode> permissionTreeNodes = null;
        if (AuthUtil.isAdministrator()) {
            permissionTreeNodes = baseMapper.treePermissionApi(null);
        } else {
            List<Long> roleIds = AuthUtil.getRoleIds();
            if (CollectionUtil.isEmpty(roleIds)) {
                return Collections.emptyList();
            }
            permissionTreeNodes = baseMapper.treePermissionApi(roleIds);
        }
        return filterPermissionTreeNodes(TreeUtil.buildTree(permissionTreeNodes));
    }

    private List<PermissionTreeNode> filterPermissionTreeNodes(List<PermissionTreeNode> permissionTreeNodes) {
        return permissionTreeNodes.stream()
                .map(node -> {
                    // 1. 先递归过滤当前节点的子节点（只算一次）
                    List<PermissionTreeNode> filteredChildren = filterPermissionTreeNodes(node.getChildren());
                    // 2. 基于子节点过滤结果和节点类型，决定是否保留当前节点
                    boolean keepNode;
                    if (node.getType() == 2) {
                        keepNode = true; // 类型为2的节点，无论子节点如何都保留
                    } else {
                        keepNode = !filteredChildren.isEmpty(); // 非类型2节点，仅当子节点非空时保留
                    }
                    // 3. 若保留节点，更新其子节点为过滤后的列表；否则返回null（后续过滤掉）
                    if (keepNode) {
                        node.getChildren().clear();
                        node.getChildren().addAll(filteredChildren);
                        return node;
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull) // 过滤掉被标记为不保留的节点（null）
                .toList();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = CacheConstant.API_CACHE, allEntries = true),
            @CacheEvict(cacheNames = CacheConstant.API_CACHE, allEntries = true)
    })
    public boolean removeById(Long id) {
        PermissionApi byId = this.getById(id);
        LogRecordContext.putVariable("permissionApi", byId);
        return logicRemoveById(id);
    }
}
