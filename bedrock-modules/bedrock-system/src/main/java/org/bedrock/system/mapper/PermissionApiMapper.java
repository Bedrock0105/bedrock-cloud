package org.bedrock.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.bedrock.system.entity.PermissionApi;
import org.bedrock.system.vo.PermissionTreeNode;

import java.util.List;

public interface PermissionApiMapper extends BaseMapper<PermissionApi> {
    /**
     * 数据树形结构
     *
     */
    List<PermissionTreeNode> treePermissionApi(@Param("roleIds") List<Long> roleIds);
}
