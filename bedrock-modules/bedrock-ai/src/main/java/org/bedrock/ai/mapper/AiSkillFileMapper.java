package org.bedrock.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.bedrock.ai.entity.AiSkillFile;
import org.bedrock.ai.vo.AiSkillFileVO;

import java.util.List;

public interface AiSkillFileMapper extends BaseMapper<AiSkillFile> {

	/**
	 * 按 skillId 查询文件节点列表（扁平，后续可组树）
	 */
	List<AiSkillFileVO> selectBySkillId(@Param("skillId") Long skillId);

}
