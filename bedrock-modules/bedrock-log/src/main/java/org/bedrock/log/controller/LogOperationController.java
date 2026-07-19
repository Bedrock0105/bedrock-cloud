package org.bedrock.log.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bedrock.common.code.api.R;
import org.bedrock.common.log.model.LogOperation;
import org.bedrock.common.mybatisplus.base.Query;
import org.bedrock.common.mybatisplus.util.PageUtil;
import org.bedrock.log.param.LogOperationParam;
import org.bedrock.log.service.ILogOperationService;
import org.bedrock.log.vo.LogOperationListVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "操作日志 控制器")
@RestController
@RequestMapping("/log-operation")
@RequiredArgsConstructor
public class LogOperationController {

    private final ILogOperationService logOperationService;

    @GetMapping("/page")
    @Operation(summary = "分页查询")
    @ApiOperationSupport(order = 1)
    public R<IPage<LogOperationListVO>> selectPage(Query query,
                                                   LogOperationParam param) {
        return R.success(logOperationService.selectPage(PageUtil.getPage(query), param));
    }

    @GetMapping("/detail")
    @Operation(summary = "详情查询")
    @ApiOperationSupport(order = 2)
    public R<LogOperation> detail(Long id) {
        return R.success(logOperationService.detail(id));
    }
}
