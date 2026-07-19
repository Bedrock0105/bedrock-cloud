package org.bedrock.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.bedrock.system.entity.RoleMenu;

import java.util.List;

public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    /**
     * 保存方法
     *
     * @param list
     * @return
     */
    int insertBatch(@Param("list") List<RoleMenu> list);

    /**
     * 根据角色ID查询菜单ID
     */
    List<String> selectMenuIdsByRoleId(@Param("roleId") Long roleId);
}
