package org.bedrock.system.service;

import org.bedrock.common.mybatisplus.base.IBaseService;
import org.bedrock.system.entity.Menu;
import org.bedrock.system.param.MenuSubmitParam;
import org.bedrock.system.vo.MenuDetailVO;
import org.bedrock.system.vo.MenuTreeVO;

import java.util.List;

public interface IMenuService extends IBaseService<Menu> {

    /**
     * 提交
     */
    boolean submit(MenuSubmitParam param);

    /**
     * 修改菜单
     */
    boolean edit(MenuSubmitParam param);

    /**
     * 删除
     */
    boolean removeById(Long id);

    /**
     * 启用禁用
     */
    boolean enableStatus(Long id, Integer status);

    /**
     * 详情
     */
    MenuDetailVO detail(Long id);


    /**
     * 获取菜单，更具权限
     */
    List<MenuTreeVO> routers(Long userId);
}
