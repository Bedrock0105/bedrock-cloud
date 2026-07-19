package org.bedrock.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.bedrock.system.entity.AdminDept;

import java.util.List;

public interface AdminDeptMapper extends BaseMapper<AdminDept> {

    int insertAdminDeptBatch(@Param("list") List<AdminDept> adminDeptList);
}
