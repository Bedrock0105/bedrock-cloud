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
import org.bedrock.resource.param.OssConfigListParam;
import org.bedrock.resource.param.OssConfigSubmitParam;
import org.bedrock.resource.service.IOssConfigService;
import org.bedrock.resource.support.OssSupport;
import org.bedrock.resource.vo.OssConfigDetailVO;
import org.bedrock.resource.vo.OssConfigListVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "oss配置 控制器")
@RestController
@RequestMapping("/oss-config")
@RequiredArgsConstructor
public class OssConfigController extends BaseController {

    private final IOssConfigService ossConfigService;

    @Operation(summary = "添加oss配置")
    @PostMapping("/submit")
    @ApiOperationSupport(order = 1)
    public R<Void> submit(@RequestBody OssConfigSubmitParam param) {
        return status(ossConfigService.submit(param));
    }

    @Operation(summary = "修改oss配置")
    @PutMapping("/edit")
    @ApiOperationSupport(order = 2)
    public R<Void> edit(@RequestBody OssConfigSubmitParam param) {
        OssSupport.clearCache(param.getId());
        return status(ossConfigService.edit(param));
    }

    @Operation(summary = "删除oss配置")
    @DeleteMapping("/remove")
    @ApiOperationSupport(order = 3)
    public R<Void> remove(@Parameter(description = "oss配置id", required = true, name = "id", in = ParameterIn.QUERY) Long id) {
        OssSupport.clearCache(id);
        return status(ossConfigService.removeById(id));
    }

    @Operation(summary = "oss配置详情")
    @GetMapping("/detail")
    @ApiOperationSupport(order = 4)
    public R<OssConfigDetailVO> detail(@Parameter(description = "oss配置id", required = true, name = "id", in = ParameterIn.QUERY) Long id) {
        return success(ossConfigService.detail(id));
    }

    @Operation(summary = "启用禁用oss配置")
    @PutMapping("/enable-status")
    @ApiOperationSupport(order = 5)
    public R<Void> enableStatus(@Parameter(description = "oss配置id", required = true, name = "id", in = ParameterIn.QUERY) Long id,
                                @Parameter(description = "配置状态（1=启用，0=禁用，下线时设为0）", required = true, name = "status", in = ParameterIn.QUERY) Integer status) {
        OssSupport.clearCache(id);
        return status(ossConfigService.enableStatus(id, status));
    }

    @Operation(summary = "oss配置列表")
    @GetMapping("/list")
    @ApiOperationSupport(order = 6)
    public R<List<OssConfigListVO>> list(OssConfigListParam param) {
        return success(ossConfigService.selectOssConfigList(param));
    }

    @Operation(summary = "oss配置分页列表")
    @GetMapping("/list-page")
    @ApiOperationSupport(order = 7)
    public R<IPage<OssConfigListVO>> listPage(Query query, OssConfigListParam param) {
        return success(ossConfigService.selectOssConfigListPage(PageUtil.getPage(query), param));
    }
}
