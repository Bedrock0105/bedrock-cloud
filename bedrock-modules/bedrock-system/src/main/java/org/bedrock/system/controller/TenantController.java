package org.bedrock.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bedrock.common.auth.constant.RoleAliasConstant;
import org.bedrock.common.auth.util.AuthUtil;
import org.bedrock.common.boot.base.BaseController;
import org.bedrock.common.code.api.R;
import org.bedrock.common.log.annotation.OperationLog;
import org.bedrock.common.mybatisplus.base.Query;
import org.bedrock.common.mybatisplus.util.PageUtil;
import org.bedrock.common.security.annotation.PrePermissionCheck;
import org.bedrock.system.param.TenantEditParam;
import org.bedrock.system.param.TenantEnableParam;
import org.bedrock.system.param.TenantListParam;
import org.bedrock.system.param.TenantSubmitParam;
import org.bedrock.system.service.ITenantService;
import org.bedrock.system.vo.TenantDetailVO;
import org.bedrock.system.vo.TenantListVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "租户管理 控制器")
@RestController
@RequestMapping("/tenant")
@RequiredArgsConstructor
public class TenantController extends BaseController {

    private final ITenantService tenantService;

    @GetMapping("/detail")
    @Operation(summary = "租户详情")
    @ApiOperationSupport(order = 1)
    @PrePermissionCheck("hasRole('" + RoleAliasConstant.ROLE_ADMINISTRATOR + "')")
    public R<TenantDetailVO> detail(Long id) {
        return R.ok(tenantService.detail(id));
    }

    @GetMapping("/info")
    @Operation(summary = "租户详情")
    @ApiOperationSupport(order = 2)
    public R<TenantDetailVO> info() {
        return R.ok(tenantService.info(AuthUtil.getTenantId()));
    }

    @PostMapping("/submit")
    @Operation(summary = "添加 租户")
    @ApiOperationSupport(order = 3)
    @PrePermissionCheck("hasRole('" + RoleAliasConstant.ROLE_ADMINISTRATOR + "')")
    @OperationLog(type = "租户管理", subType = "添加租户", success = "新增租户 名称: {{#param.tenantName}}", extra = "{TO_JSON{#param}}", condition = "{{#_errorMsg == null}}")
    public R<Void> submit(@RequestBody TenantSubmitParam param) {
        return R.status(tenantService.submit(param));
    }

    @PutMapping("/edit")
    @Operation(summary = "修改 租户")
    @ApiOperationSupport(order = 4)
    @PrePermissionCheck("hasRole('" + RoleAliasConstant.ROLE_ADMINISTRATOR + "')")
    @OperationLog(type = "租户管理", subType = "修改租户", success = "修改租户 名称: {{#param.tenantName}}", extra = "{TO_JSON{#param}}", condition = "{{#_errorMsg == null}}")
    public R<Void> edit(@RequestBody TenantEditParam param) {
        return R.status(tenantService.edit(param));
    }

    @GetMapping("/page")
    @Operation(summary = "租户管理分页列表")
    @ApiOperationSupport(order = 5)
    @PrePermissionCheck("hasRole('" + RoleAliasConstant.ROLE_ADMINISTRATOR + "')")
    public R<IPage<TenantListVO>> page(Query query, TenantListParam param) {
        return R.ok(tenantService.pageTenant(PageUtil.getPage(query), param));
    }

    @GetMapping("/list")
    @Operation(summary = "租户管理无分页列表")
    @ApiOperationSupport(order = 6)
    public R<List<TenantListVO>> list(TenantListParam param) {
        return R.ok(tenantService.listTenant(param));
    }

    @PutMapping("/enable-status")
    @Operation(summary = "租户状态")
    @ApiOperationSupport(order = 7)
    @PrePermissionCheck("hasRole('" + RoleAliasConstant.ROLE_ADMINISTRATOR + "')")
    @OperationLog(type = "租户管理", subType = "租户状态", success = "租户【{{#tenant.tenantName}}】状态修改为【{{#status == 1 ? '启用':'禁用'}}】", extra = "{TO_JSON{#param}}", condition = "{{#_errorMsg == null}}")
    public R<Void> enableStatus(TenantEnableParam param) {
        return R.status(tenantService.enableStatus(param));
    }

    @DeleteMapping("/remove")
    @Operation(summary = "删除租户")
    @ApiOperationSupport(order = 7)
    @PrePermissionCheck("hasRole('" + RoleAliasConstant.ROLE_ADMINISTRATOR + "')")
    @OperationLog(type = "租户管理", subType = "删除租户", success = "租户【{{#tenant.tenantName}}】删除", extra = "{TO_JSON{#id}}", condition = "{{#_errorMsg == null}}")
    public R<Void> enableStatus(Long id) {
        return R.status(tenantService.deleteById(id));
    }
}
