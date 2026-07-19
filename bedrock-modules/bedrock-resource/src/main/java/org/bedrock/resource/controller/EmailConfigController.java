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
import org.bedrock.resource.param.EmailConfigListParam;
import org.bedrock.resource.param.EmailConfigSubmitParam;
import org.bedrock.resource.service.IEmailConfigService;
import org.bedrock.resource.support.EmailSupport;
import org.bedrock.resource.vo.EmailConfigDetailVO;
import org.bedrock.resource.vo.EmailConfigListVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "email配置 控制器")
@RestController
@RequestMapping("/email-config")
@RequiredArgsConstructor
public class EmailConfigController extends BaseController {

    private final IEmailConfigService emailConfigService;

    @Operation(summary = "添加email配置")
    @PostMapping("/submit")
    @ApiOperationSupport(order = 1)
    public R<Void> submit(@RequestBody EmailConfigSubmitParam param) {
        return status(emailConfigService.submit(param));
    }

    @Operation(summary = "修改email配置")
    @PutMapping("/edit")
    @ApiOperationSupport(order = 2)
    public R<Void> edit(@RequestBody EmailConfigSubmitParam param) {
        EmailSupport.clearCache(param.getId());
        return status(emailConfigService.edit(param));
    }

    @Operation(summary = "删除email配置")
    @DeleteMapping("/remove")
    @ApiOperationSupport(order = 3)
    public R<Void> remove(@Parameter(description = "email配置id", required = true, name = "id", in = ParameterIn.QUERY) Long id) {
        EmailSupport.clearCache(id);
        return status(emailConfigService.removeById(id));
    }

    @Operation(summary = "email配置详情")
    @GetMapping("/detail")
    @ApiOperationSupport(order = 4)
    public R<EmailConfigDetailVO> detail(@Parameter(description = "email配置id", required = true, name = "id", in = ParameterIn.QUERY) Long id) {
        return success(emailConfigService.detail(id));
    }

    @Operation(summary = "启用禁用email配置")
    @PutMapping("/enable-status")
    @ApiOperationSupport(order = 5)
    public R<Void> enableStatus(@Parameter(description = "email配置id", required = true, name = "id", in = ParameterIn.QUERY) Long id,
                                @Parameter(description = "配置状态（1=启用，0=禁用，下线时设为0）", required = true, name = "status", in = ParameterIn.QUERY) Integer status) {
        EmailSupport.clearCache(id);
        return status(emailConfigService.enableStatus(id, status));
    }

    @Operation(summary = "email配置列表")
    @GetMapping("/list")
    @ApiOperationSupport(order = 6)
    public R<List<EmailConfigListVO>> list(EmailConfigListParam param) {
        return success(emailConfigService.selectEmailConfigList(param));
    }

    @Operation(summary = "email配置分页列表")
    @GetMapping("/list-page")
    @ApiOperationSupport(order = 7)
    public R<IPage<EmailConfigListVO>> listPage(Query query, EmailConfigListParam param) {
        return success(emailConfigService.selectEmailConfigListPage(PageUtil.getPage(query), param));
    }
}
