package org.bedrock.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bedrock.common.boot.base.BaseController;
import org.bedrock.common.code.api.R;
import org.bedrock.common.log.annotation.OperationLog;
import org.bedrock.common.mybatisplus.base.Query;
import org.bedrock.common.mybatisplus.util.PageUtil;
import org.bedrock.system.entity.ParamConfig;
import org.bedrock.system.service.IParamConfigService;
import org.springframework.web.bind.annotation.*;

@Tag(name = "参数配置 控制器")
@RestController
@RequestMapping("/param-config")
@RequiredArgsConstructor
public class ParamConfigController extends BaseController {

    private final IParamConfigService paramConfigService;

    @PostMapping("/submit")
    @Operation(summary = "添加/修改参数配置机构")
    @ApiOperationSupport(order = 1)
    @OperationLog(type = "参数配置", subType = "添加/修改参数配置", success = "修改参数配置：key->【{{#param.configKey}}】修改为->【{{#param.configValue}}】", extra = "{TO_JSON{#param}}", condition = "{{#_errorMsg == null}}")
    public R<Void> submit(@RequestBody ParamConfig param) {
        return R.status(paramConfigService.submit(param));
    }

    @GetMapping("/detail")
    @Operation(summary = "参数配置详情")
    @ApiOperationSupport(order = 2)
    public R<ParamConfig> detail(String configKey) {
        return R.success(paramConfigService.detail(configKey));
    }

    @GetMapping("/page")
    @Operation(summary = "参数配置分页")
    @ApiOperationSupport(order = 3)
    @Parameters({
            @Parameter(name = "configKey", description = "配置键（唯一标识，"),
            @Parameter(name = "configName", description = "配置名称")
    })
    public R<IPage<ParamConfig>> page(Query query, @Parameter(hidden = true) ParamConfig config) {
        return R.success(paramConfigService.page(PageUtil.getPage(query), config));
    }

    @DeleteMapping("/remove")
    @Operation(summary = "删除参数配置")
    @ApiOperationSupport(order = 4)
    @OperationLog(type = "参数配置", subType = "删除参数配置", success = "删除key->【{{#configKey}}】", extra = "{{#configKey}}", condition = "{{#_errorMsg == null}}")
    public R<Void> remove(String configKey) {
        return R.status(paramConfigService.removeByCode(configKey));
    }
}
