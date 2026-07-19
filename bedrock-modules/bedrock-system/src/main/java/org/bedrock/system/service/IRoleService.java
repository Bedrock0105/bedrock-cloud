package org.bedrock.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.bedrock.common.mybatisplus.base.IBaseService;
import org.bedrock.system.entity.Role;
import org.bedrock.system.param.RoleDetailPermissionParam;
import org.bedrock.system.param.RoleListParam;
import org.bedrock.system.param.RoleSubmitParam;
import org.bedrock.system.param.RoleSubmitPermissionParam;
import org.bedrock.system.vo.RoleDetailVO;
import org.bedrock.system.vo.RoleListVO;

import java.util.List;

public interface IRoleService extends IBaseService<Role> {

    /**
     * 根据管理员ID查询角色列表
     *
     * @param adminId 管理员ID
     * @return 角色列表
     */
    List<Role> selectRoleListByAdminId(Long adminId);

    /**
     * 提交
     *
     * @param param 参数
     * @return 是否成功
     */
    boolean submit(RoleSubmitParam param);

    /**
     * 编辑
     *
     * @param param
     * @return
     */
    boolean edit(RoleSubmitParam param);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    boolean removeById(Long id);

    /**
     * 详情
     */
    RoleDetailVO detail(Long id);

    /**
     * 角色分页
     */
    IPage<RoleListVO> rolePage(IPage<RoleListVO> iPage, RoleListParam param);

    /**
     * 角色列表
     */
    List<RoleListVO> roleList(RoleListParam param);

    /**
     * 保存菜单ID
     */
    boolean saveMenuIds(RoleSubmitPermissionParam param);

    /**
     * 获取菜单ID
     */
    List<String> selectMenuIdsByRoleId(Long roleId);

    /**
     * 保存权限
     *
     * @param param
     * @return
     */
    boolean savePermission(RoleSubmitPermissionParam param);

    /**
     * 获取权限
     *
     * @param param
     * @return
     */
    List<String> selectPermissionIdsByRoleId(RoleDetailPermissionParam param);

}
