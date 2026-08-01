package org.bedrock.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.bedrock.ai.entity.AiCsConfig;
import org.bedrock.ai.vo.AiCsConfigDetailVO;

/**
 * AI 客服配置 Mapper。
 */
public interface AiCsConfigMapper extends BaseMapper<AiCsConfig> {

	/**
	 * 按租户查询配置详情（联表模型名）。
	 */
	AiCsConfigDetailVO selectDetailByTenantId(@Param("tenantId") String tenantId);

}
