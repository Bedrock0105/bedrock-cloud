package org.bedrock.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.bedrock.ai.entity.AiKnowledge;
import org.bedrock.ai.param.AiKnowledgeListParam;
import org.bedrock.ai.vo.AiKnowledgeDetailVO;
import org.bedrock.ai.vo.AiKnowledgeListVO;

import java.util.List;

/**
 * AI 知识库 Mapper
 */
public interface AiKnowledgeMapper extends BaseMapper<AiKnowledge> {

    /**
     * 根据 id 查询知识库详情
     */
    AiKnowledgeDetailVO selectDetailById(@Param("id") Long id);

    /**
     * 查询知识库列表，支持分页
     */
    List<AiKnowledgeListVO> selectAiKnowledgeList(IPage<AiKnowledgeListVO> iPage,
            @Param("param") AiKnowledgeListParam param);

}
