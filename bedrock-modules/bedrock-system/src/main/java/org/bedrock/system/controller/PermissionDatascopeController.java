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
import org.bedrock.system.entity.PermissionDatascope;
import org.bedrock.system.service.IPermissionDatascopeService;
import org.bedrock.system.vo.PermissionTreeNode;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "数据权限 控制器")
@RestController
@RequestMapping("/permission-datascope")
@RequiredArgsConstructor
public class PermissionDatascopeController {

    private final IPermissionDatascopeService permissionDatascopeService;

    @PostMapping("/submit")
    @Operation(summary = "添加数据权限")
    @ApiOperationSupport(order = 1)
    @OperationLog(type = "数据权限管理", subType = "添加数据权限", success = "添加数据权限 权限名称：【{{#param.name}}】", extra = "{TO_JSON{#param}}", condition = "{{#_errorMsg == null}}")
    public R<Void> submit(@RequestBody PermissionDatascope param) {
        return R.status(permissionDatascopeService.submit(param));
    }

    @PutMapping("/edit")
    @Operation(summary = "修改数据权限")
    @ApiOperationSupport(order = 2)
    @OperationLog(type = "数据权限管理", subType = "修改数据权限", success = "修改数据权限 权限名称：【{{#param.name}}】", extra = "{TO_JSON{#param}}", condition = "{{#_errorMsg == null}}")
    public R<Void> edit(@RequestBody PermissionDatascope param) {
        return R.status(permissionDatascopeService.edit(param));
    }

    @GetMapping("/detail")
    @Operation(summary = "数据权限详情")
    @ApiOperationSupport(order = 3)
    public R<PermissionDatascope> detail(Long id) {
        return R.success(permissionDatascopeService.detail(id));
    }

    @GetMapping("/page")
    @Operation(summary = "数据权限 分页查询")
    @ApiOperationSupport(order = 4)
    @Parameter(name = "name", description = "权限名称", in = ParameterIn.QUERY)
    @Parameter(name = "mapperId", description = "方法唯一标识", in = ParameterIn.QUERY)
    public R<IPage<PermissionDatascope>> page(Query query, @Parameter(hidden = true) PermissionDatascope param) {
        return R.success(permissionDatascopeService.page(PageUtil.getPage(query), param));
    }

    @GetMapping("/list")
    @Operation(summary = "数据权限 无分页查询")
    @ApiOperationSupport(order = 4)
    @Parameter(name = "name", description = "权限名称", in = ParameterIn.QUERY)
    @Parameter(name = "mapperId", description = "方法唯一标识", in = ParameterIn.QUERY)
    public R<List<PermissionDatascope>> list(@Parameter(hidden = true) PermissionDatascope param) {
        return R.success(permissionDatascopeService.list(param));
    }

    @DeleteMapping("/remove")
    @Operation(summary = "删除数据权限")
    @ApiOperationSupport(order = 5)
    @OperationLog(type = "数据权限管理", subType = "删除数据权限", success = "删除数据权限 权限名称: {{#permissionDatascope.name}}", extra = "{{#id}}", condition = "{{#_errorMsg == null}}")
    public R<Void> edit(Long id) {
        return R.status(permissionDatascopeService.removeById(id));
    }

    @GetMapping("/tree-permission-data")
    @Operation(summary = "获取数据树形结构")
    @ApiOperationSupport(order = 6)
    public R<List<PermissionTreeNode>> treePermissionData() {
        return R.success(permissionDatascopeService.treePermissionDatascope());
    }
}
