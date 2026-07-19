package org.bedrock.ai.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bedrock.ai.param.AiVectorDbListParam;
import org.bedrock.ai.param.AiVectorDbSubmitParam;
import org.bedrock.ai.service.IAiVectorDbService;
import org.bedrock.ai.vo.AiVectorDbDetailVO;
import org.bedrock.ai.vo.AiVectorDbListVO;
import org.bedrock.common.auth.constant.RoleAliasConstant;
import org.bedrock.common.boot.base.BaseController;
import org.bedrock.common.code.api.R;
import org.bedrock.common.log.annotation.OperationLog;
import org.bedrock.common.mybatisplus.base.Query;
import org.bedrock.common.mybatisplus.util.PageUtil;
import org.bedrock.common.security.annotation.PrePermissionCheck;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 向量数据库配置控制器
 */
@Tag(name = "向量数据库 控制器")
@RestController
@RequestMapping("/ai-vector-db")
@RequiredArgsConstructor
public class AiVectorDbController extends BaseController {

    private final IAiVectorDbService aiVectorDbService;

    @Operation(summary = "添加向量数据库配置")
    @PostMapping("/submit")
    @ApiOperationSupport(order = 1)
    @OperationLog(type = "向量数据库管理", subType = "添加", success = "新增向量库: {{#param.dbName}}", extra = "{TO_JSON{#param}}", condition = "{{#_errorMsg == null}}")
    public R<Void> submit(@RequestBody AiVectorDbSubmitParam param) {
        return status(aiVectorDbService.submit(param));
    }

    @Operation(summary = "修改向量数据库配置")
    @PutMapping("/edit")
    @ApiOperationSupport(order = 2)
    @OperationLog(type = "向量数据库管理", subType = "修改", success = "修改向量库: {{#param.dbName}}", extra = "{TO_JSON{#param}}", condition = "{{#_errorMsg == null}}")
    public R<Void> edit(@RequestBody AiVectorDbSubmitParam param) {
        return status(aiVectorDbService.edit(param));
    }

    @Operation(summary = "删除向量数据库配置")
    @DeleteMapping("/remove")
    @ApiOperationSupport(order = 3)
    @OperationLog(type = "向量数据库管理", subType = "删除", success = "删除向量库 id: {{#id}}", extra = "{{#id}}", condition = "{{#_errorMsg == null}}")
    @PrePermissionCheck("hasAnyRole('" + RoleAliasConstant.ROLE_ADMIN + "','" + RoleAliasConstant.ROLE_ADMINISTRATOR + "')")
    public R<Void> remove(@Parameter(description = "向量库 id", required = true, name = "id", in = ParameterIn.QUERY) Long id) {
        return status(aiVectorDbService.removeById(id));
    }

    @Operation(summary = "向量数据库配置详情")
    @GetMapping("/detail")
    @ApiOperationSupport(order = 4)
    public R<AiVectorDbDetailVO> detail(@Parameter(description = "向量库 id", required = true, name = "id", in = ParameterIn.QUERY) Long id) {
        return success(aiVectorDbService.detail(id));
    }

    @Operation(summary = "启用禁用向量数据库配置")
    @PutMapping("/enable-status")
    @ApiOperationSupport(order = 5)
    @OperationLog(type = "向量数据库管理", subType = "启用禁用", success = "向量库【{{#dbName}}】状态修改为【{{#status == 1 ? '启用':'禁用'}}】", condition = "{{#_errorMsg == null}}")
    public R<Void> enableStatus(@Parameter(description = "向量库 id", required = true, name = "id", in = ParameterIn.QUERY) Long id,
            @Parameter(description = "配置状态（1=启用，0=禁用）", required = true, name = "status", in = ParameterIn.QUERY) Integer status) {
        return status(aiVectorDbService.enableStatus(id, status));
    }

    @Operation(summary = "向量数据库配置列表")
    @GetMapping("/list")
    @ApiOperationSupport(order = 6)
    public R<List<AiVectorDbListVO>> list(AiVectorDbListParam param) {
        return success(aiVectorDbService.selectAiVectorDbList(param));
    }

    @Operation(summary = "向量数据库配置分页列表")
    @GetMapping("/list-page")
    @ApiOperationSupport(order = 7)
    public R<IPage<AiVectorDbListVO>> listPage(Query query, AiVectorDbListParam param) {
        return success(aiVectorDbService.selectAiVectorDbListPage(PageUtil.getPage(query), param));
    }

}
