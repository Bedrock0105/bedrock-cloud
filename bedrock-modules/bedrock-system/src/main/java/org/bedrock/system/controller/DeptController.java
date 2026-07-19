package org.bedrock.system.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bedrock.common.auth.util.AuthUtil;
import org.bedrock.common.boot.base.BaseController;
import org.bedrock.common.code.api.R;
import org.bedrock.common.log.annotation.OperationLog;
import org.bedrock.system.param.DeptListParam;
import org.bedrock.system.param.DeptSubmitParam;
import org.bedrock.system.service.IDeptService;
import org.bedrock.system.vo.DeptDetailVO;
import org.bedrock.system.vo.DeptListVO;
import org.bedrock.system.vo.DeptTreeVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "组织 控制器")
@RestController
@RequestMapping("/dept")
@RequiredArgsConstructor
public class DeptController extends BaseController {

    private final IDeptService deptService;

    @PostMapping("/submit")
    @Operation(summary = "添加组织机构")
    @ApiOperationSupport(order = 1)
    @OperationLog(type = "组织管理", subType = "添加组织", success = "添加组织 deptName: {{#param.deptName}", extra = "{TO_JSON{#param}}", condition = "{{#_errorMsg == null}}")
    public R<Void> submit(@RequestBody DeptSubmitParam param) {
        return R.status(deptService.submit(param));
    }

    @PutMapping("/edit")
    @Operation(summary = "修改组织机构")
    @ApiOperationSupport(order = 2)
    @OperationLog(type = "组织管理", subType = "修改组织", success = "修改组织 deptName: {{#param.deptName}", extra = "{TO_JSON{#param}}", condition = "{{#_errorMsg == null}}")
    public R<Void> edit(@RequestBody DeptSubmitParam param) {
        return R.status(deptService.edit(param));
    }

    @GetMapping("/detail")
    @Operation(summary = "组织机构详情")
    @ApiOperationSupport(order = 3)
    public R<DeptDetailVO> detail(Long id) {
        return R.success(deptService.detail(id));
    }

    @GetMapping("/tree")
    @Operation(summary = "组织机构 树结构")
    @ApiOperationSupport(order = 4)
    public R<List<DeptTreeVO>> tree() {
        return R.success(deptService.tree());
    }

    @GetMapping("/tree-lazy")
    @Operation(summary = "组织机构 树结构 懒加载")
    @ApiOperationSupport(order = 5)
    public R<List<DeptListVO>> treeLazy(DeptListParam param) {
        return R.success(deptService.listDeptByParentId(param));
    }

    @DeleteMapping("/remove")
    @Operation(summary = "删除组织机构")
    @ApiOperationSupport(order = 6)
    @OperationLog(type = "组织管理", subType = "删除组织", success = "删除组织 id: {{#id}}", extra = "{{#id}}", condition = "{{#_errorMsg == null}}")
    public R<Void> remove(Long id) {
        return R.status(deptService.removeById(id));
    }
}
