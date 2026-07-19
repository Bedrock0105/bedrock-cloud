package org.bedrock.system.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bedrock.common.auth.constant.RoleAliasConstant;
import org.bedrock.common.auth.util.AuthUtil;
import org.bedrock.common.boot.base.BaseController;
import org.bedrock.common.code.api.R;
import org.bedrock.common.security.annotation.PrePermissionCheck;
import org.bedrock.system.entity.Menu;
import org.bedrock.system.param.MenuSubmitParam;
import org.bedrock.system.service.IMenuService;
import org.bedrock.system.vo.MenuDetailVO;
import org.bedrock.system.vo.MenuTreeVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "菜单管理 控制器")
@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController extends BaseController {

    private final IMenuService menuService;

    @PostMapping("/submit")
    @Operation(summary = "添加菜单")
    @ApiOperationSupport(order = 1)
    @PrePermissionCheck("hasRole('" + RoleAliasConstant.ROLE_ADMINISTRATOR + "')")
    public R<Void> submit(@RequestBody MenuSubmitParam param) {
        return R.status(menuService.submit(param));
    }

    @PutMapping("/edit")
    @Operation(summary = "修改菜单")
    @ApiOperationSupport(order = 2)
    @PrePermissionCheck("hasRole('" + RoleAliasConstant.ROLE_ADMINISTRATOR + "')")
    public R<Void> edit(@RequestBody MenuSubmitParam param) {
        return R.status(menuService.edit(param));
    }

    @GetMapping("/detail")
    @Operation(summary = "菜单详情")
    @ApiOperationSupport(order = 3)
    public R<MenuDetailVO> detail(Long id) {
        return R.success(menuService.detail(id));
    }

    @DeleteMapping("/remove")
    @Operation(summary = "删除菜单")
    @ApiOperationSupport(order = 4)
    @PrePermissionCheck("hasRole('" + RoleAliasConstant.ROLE_ADMINISTRATOR + "')")
    public R<Void> remove(Long id) {
        return R.status(menuService.removeById(id));
    }

    @PutMapping("/enable-status")
    @Operation(summary = "启用禁用 ", description = "禁用的时候会把下级都禁用掉")
    @ApiOperationSupport(order = 5)
    @Parameters({
            @Parameter(name = "id", description = "菜单id", required = false, in = ParameterIn.QUERY),
            @Parameter(name = "status", description = "状态：0-禁用，1-启用", required = false, in = ParameterIn.QUERY),
    })
    @PrePermissionCheck("hasRole('" + RoleAliasConstant.ROLE_ADMINISTRATOR + "')")
    public R<Void> enableStatus(@Parameter(hidden = true) Menu menu) {
        return R.status(menuService.enableStatus(menu.getId(), menu.getStatus()));
    }

    @GetMapping("/routers")
    @Operation(summary = "菜单 查询当前登录人的菜单")
    @ApiOperationSupport(order = 7)
    public R<List<MenuTreeVO>> routers() {
        return R.success(menuService.routers(AuthUtil.getUserId()));
    }

}
