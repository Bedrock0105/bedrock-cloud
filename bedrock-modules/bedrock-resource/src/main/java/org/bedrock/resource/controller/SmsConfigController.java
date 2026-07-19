package org.bedrock.resource.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bedrock.common.boot.base.BaseController;
import org.bedrock.common.code.api.R;
import org.bedrock.common.mybatisplus.base.Query;
import org.bedrock.common.mybatisplus.util.PageUtil;
import org.bedrock.resource.param.SmsConfigListParam;
import org.bedrock.resource.param.SmsConfigSubmitParam;
import org.bedrock.resource.service.ISmsConfigService;
import org.bedrock.resource.support.SmsSupport;
import org.bedrock.resource.vo.SmsConfigDetailVO;
import org.bedrock.resource.vo.SmsConfigListVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "sms配置 控制器")
@RestController
@RequestMapping("/sms-config")
@RequiredArgsConstructor
public class SmsConfigController extends BaseController {

    private final ISmsConfigService smsConfigService;

    @Operation(summary = "添加sms配置")
    @PostMapping("/submit")
    @ApiOperationSupport(order = 1)
    public R<Void> submit(@RequestBody SmsConfigSubmitParam param) {
        return status(smsConfigService.submit(param));
    }

    @Operation(summary = "修改sms配置")
    @PutMapping("/edit")
    @ApiOperationSupport(order = 2)
    public R<Void> edit(@RequestBody SmsConfigSubmitParam param) {
        SmsSupport.clearCache(param.getId());
        return status(smsConfigService.edit(param));
    }

    @Operation(summary = "删除sms配置")
    @DeleteMapping("/remove")
    @ApiOperationSupport(order = 3)
    public R<Void> remove(@Parameter(description = "sms配置id", required = true, name = "id", in = ParameterIn.QUERY) Long id) {
        SmsSupport.clearCache(id);
        return status(smsConfigService.removeById(id));
    }

    @Operation(summary = "sms配置详情")
    @GetMapping("/detail")
    @ApiOperationSupport(order = 4)
    public R<SmsConfigDetailVO> detail(@Parameter(description = "sms配置id", required = true, name = "id", in = ParameterIn.QUERY) Long id) {
        return success(smsConfigService.detail(id));
    }

    @Operation(summary = "启用禁用sms配置")
    @PutMapping("/enable-status")
    @ApiOperationSupport(order = 5)
    public R<Void> enableStatus(@Parameter(description = "sms配置id", required = true, name = "id", in = ParameterIn.QUERY) Long id,
                                @Parameter(description = "配置状态（1=启用，0=禁用，下线时设为0）", required = true, name = "status", in = ParameterIn.QUERY) Integer status) {
        SmsSupport.clearCache(id);
        return status(smsConfigService.enableStatus(id, status));
    }

    @Operation(summary = "sms配置列表")
    @GetMapping("/list")
    @ApiOperationSupport(order = 6)
    public R<List<SmsConfigListVO>> list(SmsConfigListParam param) {
        return success(smsConfigService.selectSmsConfigList(param));
    }

    @Operation(summary = "sms配置分页列表")
    @GetMapping("/list-page")
    @ApiOperationSupport(order = 7)
    public R<IPage<SmsConfigListVO>> listPage(Query query, SmsConfigListParam param) {
        return success(smsConfigService.selectSmsConfigListPage(PageUtil.getPage(query), param));
    }
}
