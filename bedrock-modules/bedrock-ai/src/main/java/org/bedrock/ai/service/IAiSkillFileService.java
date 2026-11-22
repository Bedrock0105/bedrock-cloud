package org.bedrock.ai.service;

import org.bedrock.ai.entity.AiSkillFile;
import org.bedrock.ai.vo.AiSkillFileVO;
import org.bedrock.common.mybatisplus.base.IBaseService;

import java.util.List;

public interface IAiSkillFileService extends IBaseService<AiSkillFile> {

	/**
	 * 按 skillId 查询文件节点列表（扁平）
	 */
	List<AiSkillFileVO> listBySkillId(Long skillId);

	/**
	 * 按 skillId 逻辑删除全部文件节点
	 */
	boolean removeBySkillId(Long skillId);

}
