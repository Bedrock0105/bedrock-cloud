package org.bedrock.ai.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bedrock.ai.param.AiRoleListParam;
import org.bedrock.ai.param.AiRoleSubmitParam;
import org.bedrock.ai.service.IAiRoleService;
import org.bedrock.ai.vo.AiRoleDetailVO;
import org.bedrock.ai.vo.AiRoleListVO;
import org.bedrock.common.auth.constant.RoleAliasConstant;
import org.bedrock.common.boot.base.BaseController;
import org.bedrock.common.code.api.R;
import org.bedrock.common.log.annotation.OperationLog;
import org.bedrock.common.mybatisplus.base.Query;
import org.bedrock.common.mybatisplus.util.PageUtil;
import org.bedrock.common.security.annotation.PrePermissionCheck;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "AI 角色 控制器")
@RestController
@RequestMapping("/ai-role")
@RequiredArgsConstructor
public class AiRoleController extends BaseController {

    private final IAiRoleService aiRoleService;

    @Operation(summary = "添加 AI 角色")
    @PostMapping("/submit")
    @ApiOperationSupport(order = 1)
    @OperationLog(type = "AI 角色管理", subType = "添加", success = "新增 AI 角色: {{#param.roleName}}", extra = "{TO_JSON{#param}}", condition = "{{#_errorMsg == null}}")
    public R<Void> submit(@RequestBody AiRoleSubmitParam param) {
        return status(aiRoleService.submit(param));
    }

    @Operation(summary = "修改 AI 角色")
    @PutMapping("/edit")
    @ApiOperationSupport(order = 2)
    @OperationLog(type = "AI 角色管理", subType = "修改", success = "修改 AI 角色: {{#param.roleName}}", extra = "{TO_JSON{#param}}", condition = "{{#_errorMsg == null}}")
    public R<Void> edit(@RequestBody AiRoleSubmitParam param) {
        return status(aiRoleService.edit(param));
    }

    @Operation(summary = "删除 AI 角色")
    @DeleteMapping("/remove")
    @ApiOperationSupport(order = 3)
    @OperationLog(type = "AI 角色管理", subType = "删除", success = "删除 AI 角色 id: {{#id}}", extra = "{{#id}}", condition = "{{#_errorMsg == null}}")
    @PrePermissionCheck("hasAnyRole('" + RoleAliasConstant.ROLE_ADMIN + "','" + RoleAliasConstant.ROLE_ADMINISTRATOR + "')")
    public R<Void> remove(@Parameter(description = "角色 id", required = true, name = "id", in = ParameterIn.QUERY) Long id) {
        return status(aiRoleService.removeById(id));
    }

    @Operation(summary = "AI 角色详情")
    @GetMapping("/detail")
    @ApiOperationSupport(order = 4)
    public R<AiRoleDetailVO> detail(@Parameter(description = "角色 id", required = true, name = "id", in = ParameterIn.QUERY) Long id) {
        return success(aiRoleService.detail(id));
    }

    @Operation(summary = "启用禁用 AI 角色")
    @PutMapping("/enable-status")
    @ApiOperationSupport(order = 5)
    @OperationLog(type = "AI 角色管理", subType = "启用禁用", success = "AI 角色【{{#roleName}}】状态修改为【{{#status == 1 ? '启用':'禁用'}}】", condition = "{{#_errorMsg == null}}")
    public R<Void> enableStatus(@Parameter(description = "角色 id", required = true, name = "id", in = ParameterIn.QUERY) Long id,
                                @Parameter(description = "配置状态（1=启用，0=禁用）", required = true, name = "status", in = ParameterIn.QUERY) Integer status) {
        return status(aiRoleService.enableStatus(id, status));
    }

    @Operation(summary = "AI 角色列表")
    @GetMapping("/list")
    @ApiOperationSupport(order = 6)
    public R<List<AiRoleListVO>> list(AiRoleListParam param) {
        return success(aiRoleService.selectAiRoleList(param));
    }

    @Operation(summary = "AI 角色分页列表")
    @GetMapping("/list-page")
    @ApiOperationSupport(order = 7)
    public R<IPage<AiRoleListVO>> listPage(Query query, AiRoleListParam param) {
        return success(aiRoleService.selectAiRoleListPage(PageUtil.getPage(query), param));
    }

}
