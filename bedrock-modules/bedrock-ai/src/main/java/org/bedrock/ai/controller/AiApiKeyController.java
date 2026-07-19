package org.bedrock.ai.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bedrock.ai.param.AiApiKeyListParam;
import org.bedrock.ai.param.AiApiKeySubmitParam;
import org.bedrock.ai.service.IAiApiKeyService;
import org.bedrock.ai.vo.AiApiKeyDetailVO;
import org.bedrock.ai.vo.AiApiKeyListVO;
import org.bedrock.common.auth.constant.RoleAliasConstant;
import org.bedrock.common.boot.base.BaseController;
import org.bedrock.common.code.api.R;
import org.bedrock.common.log.annotation.OperationLog;
import org.bedrock.common.mybatisplus.base.Query;
import org.bedrock.common.mybatisplus.util.PageUtil;
import org.bedrock.common.security.annotation.PrePermissionCheck;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "AI API Key 控制器")
@RestController
@RequestMapping("/ai-api-key")
@RequiredArgsConstructor
public class AiApiKeyController extends BaseController {

    private final IAiApiKeyService aiApiKeyService;

    @Operation(summary = "添加 API Key")
    @PostMapping("/submit")
    @ApiOperationSupport(order = 1)
    @OperationLog(type = "AI API Key管理", subType = "添加", success = "新增 API Key: {{#param.keyName}}", extra = "{TO_JSON{#param}}", condition = "{{#_errorMsg == null}}")
    public R<Void> submit(@RequestBody AiApiKeySubmitParam param) {
        return status(aiApiKeyService.submit(param));
    }

    @Operation(summary = "修改 API Key")
    @PutMapping("/edit")
    @ApiOperationSupport(order = 2)
    @OperationLog(type = "AI API Key管理", subType = "修改", success = "修改 API Key: {{#param.keyName}}", extra = "{TO_JSON{#param}}", condition = "{{#_errorMsg == null}}")
    public R<Void> edit(@RequestBody AiApiKeySubmitParam param) {
        return status(aiApiKeyService.edit(param));
    }

    @Operation(summary = "删除 API Key")
    @DeleteMapping("/remove")
    @ApiOperationSupport(order = 3)
    @OperationLog(type = "AI API Key管理", subType = "删除", success = "删除 API Key id: {{#id}}", extra = "{{#id}}", condition = "{{#_errorMsg == null}}")
    @PrePermissionCheck("hasAnyRole('" + RoleAliasConstant.ROLE_ADMIN + "','" + RoleAliasConstant.ROLE_ADMINISTRATOR + "')")
    public R<Void> remove(@Parameter(description = "API Key id", required = true, name = "id", in = ParameterIn.QUERY) Long id) {
        return status(aiApiKeyService.removeById(id));
    }

    @Operation(summary = "API Key 详情")
    @GetMapping("/detail")
    @ApiOperationSupport(order = 4)
    public R<AiApiKeyDetailVO> detail(@Parameter(description = "API Key id", required = true, name = "id", in = ParameterIn.QUERY) Long id) {
        return success(aiApiKeyService.detail(id));
    }

    @Operation(summary = "启用禁用 API Key")
    @PutMapping("/enable-status")
    @ApiOperationSupport(order = 5)
    @OperationLog(type = "AI API Key管理", subType = "启用禁用", success = "API Key【{{#keyName}}】状态修改为【{{#status == 1 ? '启用':'禁用'}}】", condition = "{{#_errorMsg == null}}")
    public R<Void> enableStatus(@Parameter(description = "API Key id", required = true, name = "id", in = ParameterIn.QUERY) Long id,
                                @Parameter(description = "配置状态（1=启用，0=禁用）", required = true, name = "status", in = ParameterIn.QUERY) Integer status) {
        return status(aiApiKeyService.enableStatus(id, status));
    }

    @Operation(summary = "API Key 列表")
    @GetMapping("/list")
    @ApiOperationSupport(order = 6)
    public R<List<AiApiKeyListVO>> list(AiApiKeyListParam param) {
        return success(aiApiKeyService.selectAiApiKeyList(param));
    }

    @Operation(summary = "API Key 分页列表")
    @GetMapping("/list-page")
    @ApiOperationSupport(order = 7)
    public R<IPage<AiApiKeyListVO>> listPage(Query query, AiApiKeyListParam param) {
        return success(aiApiKeyService.selectAiApiKeyListPage(PageUtil.getPage(query), param));
    }

}
