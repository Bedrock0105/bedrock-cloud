package org.bedrock.ai.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.modelcontextprotocol.spec.McpSchema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bedrock.ai.param.AiMcpListParam;
import org.bedrock.ai.param.AiMcpSubmitParam;
import org.bedrock.ai.service.IAiMcpService;
import org.bedrock.ai.vo.AiMcpDetailVO;
import org.bedrock.ai.vo.AiMcpListVO;
import org.bedrock.common.auth.constant.RoleAliasConstant;
import org.bedrock.common.boot.base.BaseController;
import org.bedrock.common.code.api.R;
import org.bedrock.common.log.annotation.OperationLog;
import org.bedrock.common.mybatisplus.base.Query;
import org.bedrock.common.mybatisplus.util.PageUtil;
import org.bedrock.common.security.annotation.PrePermissionCheck;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "AI MCP 控制器")
@RestController
@RequestMapping("/ai-mcp")
@RequiredArgsConstructor
public class AiMcpController extends BaseController {

    private final IAiMcpService aiMcpService;

    @Operation(summary = "添加 MCP")
    @PostMapping("/submit")
    @ApiOperationSupport(order = 1)
    @OperationLog(type = "AI MCP管理", subType = "添加", success = "新增 MCP: {{#param.name}}",
            extra = "{TO_JSON{#param}}", condition = "{{#_errorMsg == null}}")
    public R<Void> submit(@RequestBody AiMcpSubmitParam param) {
        return status(aiMcpService.submit(param));
    }

    @Operation(summary = "修改 MCP")
    @PutMapping("/edit")
    @ApiOperationSupport(order = 2)
    @OperationLog(type = "AI MCP管理", subType = "修改", success = "修改 MCP: {{#param.name}}",
            extra = "{TO_JSON{#param}}", condition = "{{#_errorMsg == null}}")
    public R<Void> edit(@RequestBody AiMcpSubmitParam param) {
        return status(aiMcpService.edit(param));
    }

    @Operation(summary = "删除 MCP")
    @DeleteMapping("/remove")
    @ApiOperationSupport(order = 3)
    @OperationLog(type = "AI MCP管理", subType = "删除", success = "删除 MCP id: {{#id}}",
            extra = "{{#id}}", condition = "{{#_errorMsg == null}}")
    @PrePermissionCheck("hasAnyRole('" + RoleAliasConstant.ROLE_ADMIN + "','"
            + RoleAliasConstant.ROLE_ADMINISTRATOR + "')")
    public R<Void> remove(
            @Parameter(description = "MCP id", required = true, name = "id", in = ParameterIn.QUERY) Long id) {
        return status(aiMcpService.removeById(id));
    }

    @Operation(summary = "MCP 详情")
    @GetMapping("/detail")
    @ApiOperationSupport(order = 4)
    public R<AiMcpDetailVO> detail(
            @Parameter(description = "MCP id", required = true, name = "id", in = ParameterIn.QUERY) Long id) {
        return success(aiMcpService.detail(id));
    }

    @Operation(summary = "启用禁用 MCP")
    @PutMapping("/enable-status")
    @ApiOperationSupport(order = 5)
    @OperationLog(type = "AI MCP管理", subType = "启用禁用",
            success = "MCP【{{#mcpName}}】状态修改为【{{#status == 1 ? '启用':'禁用'}}】",
            condition = "{{#_errorMsg == null}}")
    public R<Void> enableStatus(
            @Parameter(description = "MCP id", required = true, name = "id", in = ParameterIn.QUERY) Long id,
            @Parameter(description = "配置状态（1=启用，0=禁用）", required = true, name = "status",
                    in = ParameterIn.QUERY) Integer status) {
        return status(aiMcpService.enableStatus(id, status));
    }

    @Operation(summary = "MCP 列表")
    @GetMapping("/list")
    @ApiOperationSupport(order = 6)
    public R<List<AiMcpListVO>> list(AiMcpListParam param) {
        return success(aiMcpService.selectAiMcpList(param));
    }

    @Operation(summary = "MCP 分页列表")
    @GetMapping("/list-page")
    @ApiOperationSupport(order = 7)
    public R<IPage<AiMcpListVO>> listPage(Query query, AiMcpListParam param) {
        return success(aiMcpService.selectAiMcpListPage(PageUtil.getPage(query), param));
    }

    @Operation(summary = "连通性测试（拉取 tools，不注册）")
    @PostMapping("/test")
    @ApiOperationSupport(order = 8)
    public R<List<McpSchema.Tool>> test(@RequestBody AiMcpSubmitParam param) {
        return success(aiMcpService.testConnection(param));
    }

}
