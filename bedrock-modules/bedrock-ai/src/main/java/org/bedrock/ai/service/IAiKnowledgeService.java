package org.bedrock.ai.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.bedrock.ai.entity.AiKnowledge;
import org.bedrock.ai.param.AiKnowledgeListParam;
import org.bedrock.ai.param.AiKnowledgeSubmitParam;
import org.bedrock.ai.vo.AiKnowledgeDetailVO;
import org.bedrock.ai.vo.AiKnowledgeListVO;
import org.bedrock.common.mybatisplus.base.IBaseService;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * AI 知识库服务
 */
public interface IAiKnowledgeService extends IBaseService<AiKnowledge> {

    /**
     * 新增知识库，默认禁用状态
     */
    boolean submit(AiKnowledgeSubmitParam param);

    /**
     * 修改知识库
     */
    boolean edit(AiKnowledgeSubmitParam param);

    /**
     * 逻辑删除知识库
     */
    boolean removeById(Long id);

    /**
     * 查询知识库详情
     */
    AiKnowledgeDetailVO detail(Long id);

    /**
     * 无分页列表
     */
    List<AiKnowledgeListVO> selectAiKnowledgeList(AiKnowledgeListParam param);

    /**
     * 分页列表
     */
    IPage<AiKnowledgeListVO> selectAiKnowledgeListPage(IPage<AiKnowledgeListVO> iPage,
                                                       AiKnowledgeListParam param);

    /**
     * 启用/禁用知识库
     */
    boolean enableStatus(Long id, Integer status);

    /**
     * 获取向量数据库
     *
     * @param id
     * @return
     */
    @Nullable
    VectorStore getVectorStore(Long id);

    /**
     * 获取向量数据库
     * @param modelId
     * @param vectorDbId
     * @return
     */
    VectorStore getVectorStore(Long modelId, Long vectorDbId);
}
