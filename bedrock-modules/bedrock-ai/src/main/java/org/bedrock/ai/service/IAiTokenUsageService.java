package org.bedrock.ai.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.bedrock.ai.entity.AiTokenUsage;
import org.bedrock.ai.param.AiTokenUsageListParam;
import org.bedrock.ai.vo.AiTokenUsageDetailVO;
import org.bedrock.ai.vo.AiTokenUsageListVO;
import org.bedrock.ai.vo.AiTokenUsageStatsVO;

/**
 * Token 用量业务接口。
 * <p>明细写入由 {@link org.bedrock.common.ai.advisor.usage.TokenUsageRecorder#recordTokenUsage} 触发。</p>
 */
public interface IAiTokenUsageService extends IService<AiTokenUsage> {

    /**
     * 查询单条用量详情。
     */
    AiTokenUsageDetailVO detail(Long id);

    /**
     * 分页查询用量列表。
     */
    IPage<AiTokenUsageListVO> selectTokenUsageListPage(IPage<AiTokenUsageListVO> page, AiTokenUsageListParam param);

    /**
     * 统计概览（Dashboard 卡片与图表）。
     */
    AiTokenUsageStatsVO statsOverview();

}
