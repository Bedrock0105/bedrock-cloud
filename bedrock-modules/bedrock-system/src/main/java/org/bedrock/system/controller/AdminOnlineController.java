package org.bedrock.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bedrock.common.code.api.R;
import org.bedrock.common.mybatisplus.base.Query;
import org.bedrock.common.mybatisplus.util.PageUtil;
import org.bedrock.system.service.IAdminOnlineService;
import org.bedrock.system.vo.AdminOnlineListVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "管理员在线 控制器")
@RestController
@RequestMapping("/admin-online")
@RequiredArgsConstructor
public class AdminOnlineController {

    private final IAdminOnlineService adminOnlineService;

    @GetMapping("/page")
    @Operation(summary = "管理员在线 分页查询")
    @ApiOperationSupport(order = 1)
    public R<IPage<AdminOnlineListVO>> page(Query query) {
        return R.success(adminOnlineService.list(PageUtil.getPage(query)));
    }
}
