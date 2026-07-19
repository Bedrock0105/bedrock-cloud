package org.bedrock.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bedrock.common.boot.base.BaseController;
import org.bedrock.common.code.api.R;
import org.bedrock.common.log.annotation.OperationLog;
import org.bedrock.common.mybatisplus.base.Query;
import org.bedrock.common.mybatisplus.util.PageUtil;
import org.bedrock.system.param.*;
import org.bedrock.system.service.IRoleService;
import org.bedrock.system.vo.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "角色 控制器")
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController extends BaseController {

    private final IRoleService roleService;

    @PostMapping("/submit")
    @Operation(summary = "添加角色")
    @ApiOperationSupport(order = 1)
    @OperationLog(type = "角色管理", subType = "添加角色", success = "添加角色 roleName: {{#param.roleName}}", extra = "{TO_JSON{#param}}", condition = "{{#_errorMsg == null}}")
    public R<Void> submit(@RequestBody RoleSubmitParam param) {
        return R.status(roleService.submit(param));
    }

    @PutMapping("/edit")
    @Operation(summary = "修改角色")
    @ApiOperationSupport(order = 2)
    @OperationLog(type = "角色管理", subType = "修改角色", success = "修改角色 roleName: {{#param.roleName}}", extra = "{TO_JSON{#param}}", condition = "{{#_errorMsg == null}}")
    public R<Void> edit(@RequestBody RoleSubmitParam param) {
        return R.status(roleService.edit(param));
    }

    @GetMapping("/detail")
    @Operation(summary = "角色详情")
    @ApiOperationSupport(order = 3)
    public R<RoleDetailVO> detail(Long id) {
        return R.success(roleService.detail(id));
    }

    @GetMapping("/page")
    @Operation(summary = "角色 分页查询")
    @ApiOperationSupport(order = 4)
    public R<IPage<RoleListVO>> page(Query query, RoleListParam param) {
        return R.success(roleService.rolePage(PageUtil.getPage(query), param));
    }

    @GetMapping("/list")
    @Operation(summary = "角色 无分页")
    @ApiOperationSupport(order = 5)
    public R<List<RoleListVO>> list(RoleListParam param) {
        return R.success(roleService.roleList(param));
    }

    @DeleteMapping("/remove")
    @Operation(summary = "删除角色")
    @ApiOperationSupport(order = 6)
    public R<Void> remove(Long id) {
        return R.status(roleService.removeById(id));
    }

    @PostMapping("/save-permission-menu")
    @Operation(summary = "保存菜单权限", description = "permType 不需要传")
    @ApiOperationSupport(order = 7)
    public R<Void> savePermissionMenu(@RequestBody RoleSubmitPermissionParam param) {
        return R.status(roleService.saveMenuIds(param));
    }

    @GetMapping("/permission-menu")
    @Operation(summary = "查看菜单id")
    @ApiOperationSupport(order = 8)
    public R<List<String>> permissionMenuIdsByRoleId(Long roleId) {
        return R.success(roleService.selectMenuIdsByRoleId(roleId));
    }

    @PostMapping("/save-permission")
    @Operation(summary = "保存权限")
    @ApiOperationSupport(order = 9)
    public R<Void> savePermission(@RequestBody RoleSubmitPermissionParam param) {
        return R.status(roleService.savePermission(param));
    }

    @GetMapping("/permission")
    @Operation(summary = "查看权限id")
    @ApiOperationSupport(order = 10)
    public R<List<String>> permissionIdsByRoleId(RoleDetailPermissionParam param) {
        return R.success(roleService.selectPermissionIdsByRoleId(param));
    }
}
