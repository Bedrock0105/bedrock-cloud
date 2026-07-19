package org.bedrock.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.bedrock.common.mybatisplus.base.IBaseService;
import org.bedrock.system.dto.LoginInfo;
import org.bedrock.system.entity.Admin;
import org.bedrock.system.excel.AdminExportExcel;
import org.bedrock.system.excel.AdminImportExcel;
import org.bedrock.system.param.AdminPageParam;
import org.bedrock.system.param.AdminProfileUpdateParam;
import org.bedrock.system.param.AdminSubmitParam;
import org.bedrock.system.vo.AdminDetailVO;
import org.bedrock.system.vo.AdminListVO;

import java.util.List;

public interface IAdminService extends IBaseService<Admin> {

    /**
     * 添加管理员
     */
    boolean submit(AdminSubmitParam param);

    /**
     * 修改管理员
     */
    boolean edit(AdminSubmitParam param);

    /**
     * 详情
     */
    AdminDetailVO detail(Long id);

    /**
     * 删除
     */
    boolean removeById(Long id);

    /**
     * 分页查询
     */
    IPage<AdminListVO> pageAdmin(IPage<AdminListVO> page, AdminPageParam param);

    /**
     * 无分页查询
     */
    List<AdminListVO> listAdmin(AdminPageParam param);

    /**
     * 启用禁用
     */
    boolean enableStatus(Long id, Integer status);

    /**
     * 修改密码
     */
    boolean updatePassword(Long id, String oldPassword, String newPassword);

    /**
     * 修改个人信息
     */
    boolean updateProfile(AdminProfileUpdateParam param);

    /**
     * 重置密码
     */
    boolean resetPassword(List<Long> id, String password);

    /**
     * 登录
     */
    LoginInfo loginInfo(String username, String tenantId);

    /**
     * 会先删除之前的 保存管理员部门关系
     */
    boolean saveAdminDept(Long adminId, List<Long> deptIds);

    /**
     * 会先删除之前的 保存管理员角色关系
     */
    boolean saveAdminRole(Long adminId, List<Long> roleIds);

    /**
     * 导入数据
     */
    boolean importExcel(List<AdminImportExcel> list);

    /**
     * 导出数据
     */
    List<AdminExportExcel> exportExcel(AdminPageParam param);
}
