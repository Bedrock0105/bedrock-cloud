package org.bedrock.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.bedrock.system.entity.Dept;
import org.bedrock.system.param.DeptListParam;
import org.bedrock.system.vo.DeptDetailVO;
import org.bedrock.system.vo.DeptListVO;
import org.bedrock.system.vo.DeptTreeVO;

import java.util.List;

public interface DeptMapper extends BaseMapper<Dept> {

    /**
     * 根据管理员ID查询部门列表
     */
    List<Dept> selectDeptListByAdminId(@Param("adminId") Long adminId);

    /**
     * 查询部门详情
     */
    DeptDetailVO selectDetailVO(@Param("id") Long id);

    /**
     * 查询部门树结构
     */
    List<DeptTreeVO> selectTreeNodeDept();

    /**
     * 根据上级部门ID查询部门列表
     */
    List<DeptListVO> selectDeptByParentId(@Param("param") DeptListParam param);
}
