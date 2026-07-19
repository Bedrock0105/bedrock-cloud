package org.bedrock.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.bedrock.system.entity.Menu;
import org.bedrock.system.vo.MenuDetailVO;
import org.bedrock.system.vo.MenuListVO;
import org.bedrock.system.vo.MenuTreeVO;

import java.util.List;

public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 菜单详情
     * @param id
     * @return
     */
    MenuDetailVO selectDetail(@Param("id") Long id);

    /**
     * 菜单列表
     * @param menu
     * @return
     */
    List<MenuTreeVO> selectMenuList(@Param("menu") Menu menu);

    /**
     * 获取菜单
     * @param roleIds
     * @return
     */
    List<MenuTreeVO> selectMenuByRoleIds(@Param("roleIds") List<Long> roleIds);
}
