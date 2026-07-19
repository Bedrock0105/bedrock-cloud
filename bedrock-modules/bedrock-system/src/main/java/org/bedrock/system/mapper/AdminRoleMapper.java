package org.bedrock.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.bedrock.system.entity.AdminRole;

import java.util.List;

public interface AdminRoleMapper extends BaseMapper<AdminRole> {

    int insertAdminRoleBatch(@Param("list") List<AdminRole> list);
}
