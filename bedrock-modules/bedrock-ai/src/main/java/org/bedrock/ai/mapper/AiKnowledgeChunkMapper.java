package org.bedrock.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.bedrock.ai.entity.AiKnowledgeChunk;
import org.bedrock.ai.param.AiKnowledgeChunkListParam;
import org.bedrock.ai.vo.AiKnowledgeChunkDetailVO;
import org.bedrock.ai.vo.AiKnowledgeChunkListVO;

import java.util.List;

/**
 * 知识库文档分片 Mapper
 */
public interface AiKnowledgeChunkMapper extends BaseMapper<AiKnowledgeChunk> {

    /**
     * 根据 id 查询分片详情
     */
    AiKnowledgeChunkDetailVO selectDetailById(@Param("id") Long id);

    /**
     * 查询分片列表，支持分页
     */
    List<AiKnowledgeChunkListVO> selectAiKnowledgeChunkList(IPage<AiKnowledgeChunkListVO> iPage,
                                                          @Param("param") AiKnowledgeChunkListParam param);


    /**
     * 根据文档id获取最大分片序号
     */
    int getMaxChunkNo(Long docId);

    /**
     * 批量插入分片
     */
    int insertBatch(@Param("list") List<AiKnowledgeChunk> list);

    /**
     * 分片召回次数 +1（按 vector_id）
     */
    int updateRetrievalCountIncr(@Param("vectorIds") List<String> vectorIds);
}
