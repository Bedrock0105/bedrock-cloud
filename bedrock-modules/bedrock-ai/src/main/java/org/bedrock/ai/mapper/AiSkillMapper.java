package org.bedrock.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.bedrock.ai.entity.AiSkill;
import org.bedrock.ai.param.AiSkillListParam;
import org.bedrock.ai.vo.AiSkillDetailVO;
import org.bedrock.ai.vo.AiSkillListVO;

import java.util.List;

public interface AiSkillMapper extends BaseMapper<AiSkill> {

	/**
	 * 根据 ID 查询详情
	 */
	AiSkillDetailVO selectDetailById(@Param("id") Long id);

	/**
	 * 查询列表
	 */
	List<AiSkillListVO> selectAiSkillList(IPage<AiSkillListVO> iPage, @Param("param") AiSkillListParam param);

}
