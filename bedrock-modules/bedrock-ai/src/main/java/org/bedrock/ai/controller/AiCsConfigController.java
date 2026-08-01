package org.bedrock.ai.controller;

import org.bedrock.common.crypto.annotation.ApiCrypto;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bedrock.ai.param.AiCsConfigSaveParam;
import org.bedrock.ai.service.IAiCsConfigService;
import org.bedrock.ai.vo.AiCsConfigDetailVO;
import org.bedrock.common.auth.util.AuthUtil;
import org.bedrock.common.boot.base.BaseController;
import org.bedrock.common.code.api.R;
import org.bedrock.common.log.annotation.OperationLog;
import org.springframework.web.bind.annotation.*;

/**
 * AI 客服配置控制器（租户级单条，无列表）。
 */
@Tag(name = "AI 客服配置")
@ApiCrypto
@RestController
@RequestMapping("/ai-cs-config")
@RequiredArgsConstructor
public class AiCsConfigController extends BaseController {

    private final IAiCsConfigService aiCsConfigService;

    @Operation(summary = "本租户客服配置详情（管理端）")
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    public R<AiCsConfigDetailVO> detail() {
        return success(aiCsConfigService.detail());
    }

    @Operation(summary = "保存本租户客服配置")
    @PostMapping("/save")
    @ApiOperationSupport(order = 2)
    @OperationLog(type = "AI 客服配置", subType = "保存", success = "保存 AI 客服配置: {{#param.name}}",
            extra = "{TO_JSON{#param}}", condition = "{{#_errorMsg == null}}")
    public R<Void> save(@RequestBody AiCsConfigSaveParam param) {
        return status(aiCsConfigService.saveConfig(param));
    }

    @Operation(summary = "客服浮窗运行时配置（登录用户）")
    @GetMapping("/runtime")
    @ApiOperationSupport(order = 3)
    public R<AiCsConfigDetailVO> runtime() {
        return success(aiCsConfigService.requireEnabledDetail(AuthUtil.getTenantId()));
    }

}
