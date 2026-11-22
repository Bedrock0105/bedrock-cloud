package org.bedrock.log.controller;

import org.bedrock.common.crypto.annotation.ApiCrypto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bedrock.common.code.api.R;
import org.bedrock.common.log.model.LogError;
import org.bedrock.common.mybatisplus.base.Query;
import org.bedrock.common.mybatisplus.util.PageUtil;
import org.bedrock.log.param.LogErrorParam;
import org.bedrock.log.service.ILogErrorService;
import org.bedrock.log.vo.LogErrorListVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 错误日志控制器（查询兜底 Throwable 持久化记录）
 */
@Tag(name = "错误日志 控制器")
@ApiCrypto
@RestController
@RequestMapping("/log-error")
@RequiredArgsConstructor
public class LogErrorController {

    private final ILogErrorService logErrorService;

    @GetMapping("/page")
    @Operation(summary = "分页查询")
    @ApiOperationSupport(order = 1)
    public R<IPage<LogErrorListVO>> selectPage(Query query, LogErrorParam param) {
        return R.success(logErrorService.selectPage(PageUtil.getPage(query), param));
    }

    @GetMapping("/detail")
    @Operation(summary = "详情查询")
    @ApiOperationSupport(order = 2)
    public R<LogError> detail(Long id) {
        return R.success(logErrorService.detail(id));
    }
}
