package org.bedrock.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.bedrock.system.entity.PermissionDatascope;
import org.bedrock.system.vo.PermissionTreeNode;

import java.util.List;

public interface PermissionDatascopeMapper extends BaseMapper<PermissionDatascope> {

    /**
     * 数据树形结构
     *
     * @return
     */
    List<PermissionTreeNode> treePermissionDatascope(@Param("roleIds") List<Long> roleIds);
}
