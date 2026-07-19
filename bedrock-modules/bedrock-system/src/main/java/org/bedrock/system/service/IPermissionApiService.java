package org.bedrock.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.bedrock.common.mybatisplus.base.IBaseService;
import org.bedrock.system.entity.PermissionApi;
import org.bedrock.system.vo.PermissionTreeNode;

import java.util.List;

public interface IPermissionApiService extends IBaseService<PermissionApi> {

    /**
     * 提交
     */
    boolean submit(PermissionApi permissionApi);

    /**
     * 修改权限接口
     */
    boolean edit(PermissionApi permissionApi);

    /**
     * 详情
     */
    PermissionApi detail(Long id);

    /**
     * 分页
     */
    IPage<PermissionApi> page(IPage<PermissionApi> page, PermissionApi permissionApi);

    /**
     * 数据树形结构
     */
    List<PermissionTreeNode> treePermissionApi();

    /**
     * 删除
     */
    boolean removeById(Long id);
}
