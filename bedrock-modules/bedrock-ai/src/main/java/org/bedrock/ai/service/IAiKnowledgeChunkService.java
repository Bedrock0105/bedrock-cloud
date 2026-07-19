package org.bedrock.ai.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.bedrock.ai.entity.AiKnowledgeChunk;
import org.bedrock.ai.entity.AiKnowledgeDoc;
import org.bedrock.ai.param.AiKnowledgeChunkListParam;
import org.bedrock.ai.param.AiKnowledgeChunkSearchParam;
import org.bedrock.ai.param.AiKnowledgeChunkSubmitParam;
import org.bedrock.ai.vo.AiKnowledgeChunkDetailVO;
import org.bedrock.ai.vo.AiKnowledgeChunkListVO;
import org.bedrock.common.mybatisplus.base.IBaseService;
import org.springframework.ai.document.Document;

import java.util.List;

/**
 * 知识库文档分片服务
 */
public interface IAiKnowledgeChunkService extends IBaseService<AiKnowledgeChunk> {

    /**
     * 新增分片，默认待向量化、禁用状态
     */
    boolean submit(AiKnowledgeChunkSubmitParam param);

    /**
     * 修改分片
     */
    boolean edit(AiKnowledgeChunkSubmitParam param);

    /**
     * 逻辑删除分片
     */
    boolean removeById(Long id);

    /**
     * 查询分片详情
     */
    AiKnowledgeChunkDetailVO detail(Long id);

    /**
     * 无分页列表
     */
    List<AiKnowledgeChunkListVO> selectAiKnowledgeChunkList(AiKnowledgeChunkListParam param);

    /**
     * 分页列表
     */
    IPage<AiKnowledgeChunkListVO> selectAiKnowledgeChunkListPage(IPage<AiKnowledgeChunkListVO> iPage,
                                                                 AiKnowledgeChunkListParam param);

    /**
     * 启用/禁用分片
     */
    boolean enableStatus(Long id, Integer status);

    /**
     * 批量保存文档分片（异步写入向量库）
     */
    void saveDocument(AiKnowledgeDoc doc,
                      List<Document> documentList);

    /**
     * 根据文档ID删除分片
     */
    void removeByKnowledgeDocId(Long docId);

    /**
     * 根据知识ID删除分片
     */
    void removeByKnowledgeId(Long knowledgeId);

    /**
     * 召会测试
     */
    List<Document> search(AiKnowledgeChunkSearchParam param);
}
