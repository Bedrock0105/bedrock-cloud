package org.bedrock.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bedrock.common.auth.constant.RoleAliasConstant;
import org.bedrock.common.code.api.R;
import org.bedrock.common.mybatisplus.base.Query;
import org.bedrock.common.mybatisplus.util.PageUtil;
import org.bedrock.common.security.annotation.PrePermissionCheck;
import org.bedrock.system.entity.TenantPackage;
import org.bedrock.system.param.TenantPackageSubmitParam;
import org.bedrock.system.service.ITenantPackageService;
import org.bedrock.system.vo.TenantPackageDetailVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "产品包 控制器")
@RestController
@RequestMapping("/tenant-package")
@RequiredArgsConstructor
@PrePermissionCheck("hasRole('" + RoleAliasConstant.ROLE_ADMINISTRATOR + "')")
public class TenantPackageController {

    private final ITenantPackageService tenantPackageService;

    @PostMapping("/submit")
    @Operation(summary = "添加产品包")
    @ApiOperationSupport(order = 1)
    public R<Void> submit(@RequestBody TenantPackageSubmitParam param) {
        return R.status(tenantPackageService.submit(param));
    }

    @PutMapping("/edit")
    @Operation(summary = "修改产品包")
    @ApiOperationSupport(order = 2)
    public R<Void> edit(@RequestBody TenantPackageSubmitParam param) {
        return R.status(tenantPackageService.edit(param));
    }

    @GetMapping("/detail")
    @Operation(summary = "产品包详情")
    @ApiOperationSupport(order = 3)
    public R<TenantPackageDetailVO> detail(Long id) {
        return R.ok(tenantPackageService.detail(id));
    }

    @GetMapping("/page")
    @Operation(summary = "产品包分页列表")
    @ApiOperationSupport(order = 4)
    @Parameter(name = "name", description = "产品包名称", required = false, in = ParameterIn.QUERY)
    public R<IPage<TenantPackage>> page(Query query,
                                        @Parameter(hidden = true) TenantPackage param) {
        return R.ok(tenantPackageService.pageTenantPackage(PageUtil.getPage(query), param));
    }

    @GetMapping("/list")
    @Operation(summary = "产品包无分页列表")
    @ApiOperationSupport(order = 5)
    @Parameter(name = "name", description = "产品包名称", required = false, in = ParameterIn.QUERY)
    public R<List<TenantPackage>> list(@Parameter(hidden = true) TenantPackage param) {
        return R.ok(tenantPackageService.listTenantPackage(param));
    }

    @DeleteMapping("/remove")
    @Operation(summary = "删除产品包")
    @ApiOperationSupport(order = 6)
    public R<Void> remove(Long id) {
        return R.status(tenantPackageService.removeById(id));
    }
}
