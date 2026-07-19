package org.bedrock.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.bedrock.system.entity.RolePermission;
import org.bedrock.system.param.RoleDetailPermissionParam;

import java.util.List;

public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    /**
     * 批量插入
     */
    int insertBatch(@Param("list") List<RolePermission> list);

    /**
     * 根据角色ID查询权限ID列表
     *
     */
    List<String> selectPermissionIdsByRoleId(@Param("param") RoleDetailPermissionParam param);
}
