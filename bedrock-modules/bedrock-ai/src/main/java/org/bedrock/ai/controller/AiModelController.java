package org.bedrock.ai.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bedrock.ai.param.AiModelListParam;
import org.bedrock.ai.param.AiModelSubmitParam;
import org.bedrock.ai.service.IAiModelService;
import org.bedrock.ai.vo.AiModelDetailVO;
import org.bedrock.ai.vo.AiModelListVO;
import org.bedrock.common.auth.constant.RoleAliasConstant;
import org.bedrock.common.boot.base.BaseController;
import org.bedrock.common.code.api.R;
import org.bedrock.common.log.annotation.OperationLog;
import org.bedrock.common.mybatisplus.base.Query;
import org.bedrock.common.mybatisplus.util.PageUtil;
import org.bedrock.common.security.annotation.PrePermissionCheck;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "AI 模型 控制器")
@RestController
@RequestMapping("/ai-model")
@RequiredArgsConstructor
public class AiModelController extends BaseController {

    private final IAiModelService aiModelService;

    @Operation(summary = "添加模型")
    @PostMapping("/submit")
    @ApiOperationSupport(order = 1)
    @OperationLog(type = "AI 模型管理", subType = "添加", success = "新增模型: {{#param.modelName}}", extra = "{TO_JSON{#param}}", condition = "{{#_errorMsg == null}}")
    public R<Void> submit(@RequestBody AiModelSubmitParam param) {
        return status(aiModelService.submit(param));
    }

    @Operation(summary = "修改模型")
    @PutMapping("/edit")
    @ApiOperationSupport(order = 2)
    @OperationLog(type = "AI 模型管理", subType = "修改", success = "修改模型: {{#param.modelName}}", extra = "{TO_JSON{#param}}", condition = "{{#_errorMsg == null}}")
    public R<Void> edit(@RequestBody AiModelSubmitParam param) {
        return status(aiModelService.edit(param));
    }

    @Operation(summary = "删除模型")
    @DeleteMapping("/remove")
    @ApiOperationSupport(order = 3)
    @OperationLog(type = "AI 模型管理", subType = "删除", success = "删除模型 id: {{#id}}", extra = "{{#id}}", condition = "{{#_errorMsg == null}}")
    @PrePermissionCheck("hasAnyRole('" + RoleAliasConstant.ROLE_ADMIN + "','" + RoleAliasConstant.ROLE_ADMINISTRATOR + "')")
    public R<Void> remove(@Parameter(description = "模型 id", required = true, name = "id", in = ParameterIn.QUERY) Long id) {
        return status(aiModelService.removeById(id));
    }

    @Operation(summary = "模型详情")
    @GetMapping("/detail")
    @ApiOperationSupport(order = 4)
    public R<AiModelDetailVO> detail(@Parameter(description = "模型 id", required = true, name = "id", in = ParameterIn.QUERY) Long id) {
        return success(aiModelService.detail(id));
    }

    @Operation(summary = "启用禁用模型")
    @PutMapping("/enable-status")
    @ApiOperationSupport(order = 5)
    @OperationLog(type = "AI 模型管理", subType = "启用禁用", success = "模型【{{#modelName}}】状态修改为【{{#status == 1 ? '启用':'禁用'}}】", condition = "{{#_errorMsg == null}}")
    public R<Void> enableStatus(@Parameter(description = "模型 id", required = true, name = "id", in = ParameterIn.QUERY) Long id,
                                @Parameter(description = "配置状态（1=启用，0=禁用）", required = true, name = "status", in = ParameterIn.QUERY) Integer status) {
        return status(aiModelService.enableStatus(id, status));
    }

    @Operation(summary = "模型列表")
    @GetMapping("/list")
    @ApiOperationSupport(order = 6)
    public R<List<AiModelListVO>> list(AiModelListParam param) {
        return success(aiModelService.selectAiModelList(param));
    }

    @Operation(summary = "模型分页列表")
    @GetMapping("/list-page")
    @ApiOperationSupport(order = 7)
    public R<IPage<AiModelListVO>> listPage(Query query, AiModelListParam param) {
        return success(aiModelService.selectAiModelListPage(PageUtil.getPage(query), param));
    }

}
