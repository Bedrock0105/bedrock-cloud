package org.bedrock.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.bedrock.system.entity.Role;
import org.bedrock.system.param.RoleListParam;
import org.bedrock.system.vo.RoleDetailVO;
import org.bedrock.system.vo.RoleListVO;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据管理员ID查询角色列表
     */
    List<Role> selectRoleListByAdminId(@Param("adminId") Long adminId);

    /**
     * 角色详情
     */
    RoleDetailVO selectRoleDetailById(@Param("id") Long id);

    /**
     * 角色列表
     */
    List<RoleListVO> selectRoleList(IPage<RoleListVO> iPage,
                                    @Param("param") RoleListParam param,
                                    @Param("neRoleAlias") List<String> neRoleAlias);
}
