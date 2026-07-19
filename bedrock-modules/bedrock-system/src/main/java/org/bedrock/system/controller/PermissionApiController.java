package org.bedrock.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bedrock.common.code.api.R;
import org.bedrock.common.log.annotation.OperationLog;
import org.bedrock.common.mybatisplus.base.Query;
import org.bedrock.common.mybatisplus.util.PageUtil;
import org.bedrock.system.entity.PermissionApi;
import org.bedrock.system.service.IPermissionApiService;
import org.bedrock.system.vo.PermissionTreeNode;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "接口权限 控制器")
@RestController
@RequestMapping("/permission-api")
@RequiredArgsConstructor
public class PermissionApiController {

    private final IPermissionApiService permissionApiService;

    @PostMapping("/submit")
    @Operation(summary = "添加接口权限")
    @ApiOperationSupport(order = 1)
    @OperationLog(type = "接口权限管理", subType = "添加接口权限", success = "添加接口权限 权限名称：【{{#param.name}}】", extra = "{TO_JSON{#param}}", condition = "{{#_errorMsg == null}}")
    public R<Void> submit(@RequestBody PermissionApi param) {
        return R.status(permissionApiService.submit(param));
    }

    @PutMapping("/edit")
    @Operation(summary = "修改接口权限")
    @ApiOperationSupport(order = 2)
    @OperationLog(type = "接口权限管理", subType = "修改接口权限", success = "修改接口权限 权限名称：【{{#param.name}}】", extra = "{TO_JSON{#param}}", condition = "{{#_errorMsg == null}}")
    public R<Void> edit(@RequestBody PermissionApi param) {
        return R.status(permissionApiService.edit(param));
    }

    @GetMapping("/detail")
    @Operation(summary = "接口权限详情")
    @ApiOperationSupport(order = 3)
    public R<PermissionApi> detail(Long id) {
        return R.success(permissionApiService.detail(id));
    }

    @GetMapping("/page")
    @Operation(summary = "接口权限 分页查询")
    @ApiOperationSupport(order = 4)
    @Parameter(name = "name", description = "权限名称", in = ParameterIn.QUERY)
    @Parameter(name = "permission", description = "权限标识", in = ParameterIn.QUERY)
    public R<IPage<PermissionApi>> page(Query query, @Parameter(hidden = true) PermissionApi param) {
        return R.success(permissionApiService.page(PageUtil.getPage(query), param));
    }

    @DeleteMapping("/remove")
    @Operation(summary = "删除接口权限")
    @ApiOperationSupport(order = 5)
    @OperationLog(type = "接口权限管理", subType = "删除接口权限", success = "删除接口权限 权限名称: {{#permissionApi.name}}", extra = "{{#id}}", condition = "{{#_errorMsg == null}}")
    public R<Void> edit(Long id) {
        return R.status(permissionApiService.removeById(id));
    }

    @GetMapping("/tree-permission-api")
    @Operation(summary = "获取数据树形结构")
    @ApiOperationSupport(order = 6)
    public R<List<PermissionTreeNode>> treePermissionApi() {
        return R.success(permissionApiService.treePermissionApi());
    }
}
