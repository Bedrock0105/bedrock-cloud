package org.bedrock.system.service;

import org.bedrock.common.mybatisplus.base.IBaseService;
import org.bedrock.system.entity.Dept;
import org.bedrock.system.param.DeptListParam;
import org.bedrock.system.param.DeptSubmitParam;
import org.bedrock.system.vo.DeptDetailVO;
import org.bedrock.system.vo.DeptListVO;
import org.bedrock.system.vo.DeptTreeVO;

import java.util.List;

public interface IDeptService extends IBaseService<Dept> {

    /**
     * 根据管理员ID查询部门列表
     *
     * @param adminId 管理员ID
     * @return 部门列表
     */
    List<Dept> selectDeptListByAdminId(Long adminId);

    /**
     * 添加 部门
     */
    boolean submit(DeptSubmitParam param);

    /**
     * 修改 部门
     */
    boolean edit(DeptSubmitParam param);

    /**
     * 删除
     */
    boolean removeById(Long id);

    /**
     * 部门列表
     */
    List<DeptListVO> listDeptByParentId(DeptListParam param);

    /**
     * 详情
     */
    DeptDetailVO detail(Long id);

    /**
     * 部门树
     */
    List<DeptTreeVO> tree();
}
