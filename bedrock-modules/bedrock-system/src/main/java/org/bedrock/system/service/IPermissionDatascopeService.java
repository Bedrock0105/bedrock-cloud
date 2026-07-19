package org.bedrock.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.bedrock.common.mybatisplus.base.IBaseService;
import org.bedrock.system.entity.PermissionDatascope;
import org.bedrock.system.vo.PermissionTreeNode;

import java.util.List;

public interface IPermissionDatascopeService extends IBaseService<PermissionDatascope> {

    /**
     * 提交
     */
    boolean submit(PermissionDatascope permissionDatascope);

    /**
     * 修改权限接口
     */
    boolean edit(PermissionDatascope permissionDatascope);

    /**
     * 详情
     */
    PermissionDatascope detail(Long id);

    /**
     * 分页
     */
    IPage<PermissionDatascope> page(IPage<PermissionDatascope> page, PermissionDatascope permissionDatascope);

    /**
     * 无分页
     */
    List<PermissionDatascope> list(PermissionDatascope permissionDatascope);

    /**
     * 数据树形结构
     */
    List<PermissionTreeNode> treePermissionDatascope();

    /**
     * 删除
     */
    boolean removeById(Long id);
}
