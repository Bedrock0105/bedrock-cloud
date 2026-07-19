package org.bedrock.ai.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bedrock.ai.param.AiTokenUsageListParam;
import org.bedrock.ai.service.IAiTokenUsageService;
import org.bedrock.ai.vo.AiTokenUsageDetailVO;
import org.bedrock.ai.vo.AiTokenUsageListVO;
import org.bedrock.ai.vo.AiTokenUsageStatsVO;
import org.bedrock.common.boot.base.BaseController;
import org.bedrock.common.code.api.R;
import org.bedrock.common.mybatisplus.base.Query;
import org.bedrock.common.mybatisplus.util.PageUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AI Token 用量控制器。
 * <p>
 * 提供 Token 用量明细的只读查询接口，数据由 {@link org.bedrock.common.ai.advisor.TokenUsageStatisticsAdvisor}
 * 在每次模型调用结束后自动写入。
 * </p>
 */
@Tag(name = "AI Token 用量 控制器")
@RestController
@RequestMapping("/ai-token-usage")
@RequiredArgsConstructor
public class AiTokenUsageController extends BaseController {

    private final IAiTokenUsageService aiTokenUsageService;

    /**
     * 查询单条用量详情（含耗时、吞吐等扩展字段）。
     */
    @Operation(summary = "Token 用量详情")
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    public R<AiTokenUsageDetailVO> detail(@Parameter(description = "记录 id", required = true, name = "id", in = ParameterIn.QUERY) Long id) {
        return success(aiTokenUsageService.detail(id));
    }

    /**
     * 分页查询用量列表，支持按模型、用户、平台、来源、时间范围筛选。
     */
    @Operation(summary = "Token 用量分页列表")
    @GetMapping("/list-page")
    @ApiOperationSupport(order = 2)
    public R<IPage<AiTokenUsageListVO>> listPage(Query query, AiTokenUsageListParam param) {
        return success(aiTokenUsageService.selectTokenUsageListPage(PageUtil.getPage(query), param));
    }

    /**
     * 统计概览：累计/今日调用与 Token、模型 TOP10、近 7 日趋势。
     * <p>供工作台 Dashboard 使用。</p>
     */
    @Operation(summary = "Token 用量统计概览")
    @GetMapping("/stats")
    @ApiOperationSupport(order = 3)
    public R<AiTokenUsageStatsVO> stats() {
        return success(aiTokenUsageService.statsOverview());
    }

}
