package org.bedrock.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.bedrock.ai.entity.AiTokenUsage;
import org.bedrock.ai.param.AiTokenUsageListParam;
import org.bedrock.ai.vo.AiTokenUsageDetailVO;
import org.bedrock.ai.vo.AiTokenUsageListVO;
import org.bedrock.ai.vo.AiTokenUsageModelStatsVO;
import org.bedrock.ai.vo.AiTokenUsageTrendVO;

import java.util.List;

/**
 * Token 用量 Mapper。
 */
public interface AiTokenUsageMapper extends BaseMapper<AiTokenUsage> {

    /** 查询详情（含耗时、吞吐等完整字段） */
    AiTokenUsageDetailVO selectDetailById(@Param("id") Long id);

    /** 分页列表查询 */
    List<AiTokenUsageListVO> selectTokenUsageList(IPage<AiTokenUsageListVO> page,
                                                  @Param("param") AiTokenUsageListParam param);

    /** 累计调用次数 */
    Long selectTotalCalls();

    /** 累计总 token */
    Long selectTotalTokens();

    /** 累计输入 token */
    Long selectTotalPromptTokens();

    /** 累计输出 token */
    Long selectTotalCompletionTokens();

    /** 今日调用次数 */
    Long selectTodayCalls();

    /** 今日总 token */
    Long selectTodayTokens();

    /** 按模型聚合：调用次数 TOP10 */
    List<AiTokenUsageModelStatsVO> selectModelStats();

    /** 近 N 日按天聚合趋势 */
    List<AiTokenUsageTrendVO> selectRecentTrends(@Param("days") int days);

}
