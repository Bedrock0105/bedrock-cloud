package org.bedrock.ai.service;

import org.bedrock.ai.entity.AiCsConfig;
import org.bedrock.ai.param.AiCsConfigSaveParam;
import org.bedrock.ai.vo.AiCsConfigDetailVO;
import org.bedrock.common.mybatisplus.base.IBaseService;

/**
 * AI 客服配置服务。
 */
public interface IAiCsConfigService extends IBaseService<AiCsConfig> {

	/**
	 * 管理端：查询当前租户客服配置（无则返回 null）。
	 */
	AiCsConfigDetailVO detail();

	/**
	 * 管理端：保存当前租户配置（upsert），并失效租户缓存。
	 */
	boolean saveConfig(AiCsConfigSaveParam param);

	/**
	 * 查询当前租户「已启用」的完整配置（浮窗 runtime / 客服聊天用；未启用返回 enabled=false）。
	 */
	AiCsConfigDetailVO requireEnabledDetail(String tenantId);

}
