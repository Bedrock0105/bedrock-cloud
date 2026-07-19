package org.bedrock.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.bedrock.ai.entity.AiKnowledgeDoc;
import org.bedrock.ai.param.AiKnowledgeDocListParam;
import org.bedrock.ai.vo.AiKnowledgeDocDetailVO;
import org.bedrock.ai.vo.AiKnowledgeDocListVO;

import java.util.List;

/**
 * 知识库文档 Mapper
 */
public interface AiKnowledgeDocMapper extends BaseMapper<AiKnowledgeDoc> {

    /**
     * 根据 id 查询文档详情
     */
    AiKnowledgeDocDetailVO selectDetailById(@Param("id") Long id);

    /**
     * 查询文档列表，支持分页
     */
    List<AiKnowledgeDocListVO> selectAiKnowledgeDocList(IPage<AiKnowledgeDocListVO> iPage,
                                                        @Param("param") AiKnowledgeDocListParam param);

    /**
     * 文档召回次数 +1（按 doc id 去重后批量更新）
     */
    int updateRecallCountIncr(@Param("docIds") List<Long> docIds);

}
