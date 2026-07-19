package org.bedrock.ai.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.bedrock.ai.entity.AiKnowledgeDoc;
import org.bedrock.ai.param.*;
import org.bedrock.ai.vo.AiKnowledgeDocDetailVO;
import org.bedrock.ai.vo.AiKnowledgeDocListVO;
import org.bedrock.common.mybatisplus.base.IBaseService;
import org.springframework.ai.document.Document;

import java.util.List;

/**
 * 知识库文档服务
 */
public interface IAiKnowledgeDocService extends IBaseService<AiKnowledgeDoc> {

    /**
     * 新增手动文档
     *
     * @param param
     * @return
     */
    boolean create(AiKnowledgeDocCreateParam param);

    /**
     * 文件数据集上传入库（含分片处理与向量写入）
     */
    boolean submit(AiKnowledgeDocUploadParam param);

    /**
     * 重新拆分文档
     */
    boolean againSplit(AiKnowledgeDocAgainSplitParam param);

    /**
     * 修改文档
     */
    boolean edit(AiKnowledgeDocEditParam param);

    /**
     * 逻辑删除文档，并级联删除其下分片
     */
    boolean removeById(Long id);

    /**
     * 查询文档详情
     */
    AiKnowledgeDocDetailVO detail(Long id);

    /**
     * 无分页列表
     */
    List<AiKnowledgeDocListVO> selectAiKnowledgeDocList(AiKnowledgeDocListParam param);

    /**
     * 分页列表
     */
    IPage<AiKnowledgeDocListVO> selectAiKnowledgeDocListPage(IPage<AiKnowledgeDocListVO> iPage,
                                                             AiKnowledgeDocListParam param);

    /**
     * 启用/禁用文档
     */
    boolean enableStatus(Long id, Integer status);

    /**
     * 读取单个文档并进行拆分文档内容
     *
     * @return 文档内容列表
     */
    List<Document> separateDoc(AiKnowledgeDocSeparateParam param);

    /**
     * 读取多个文档并进行拆分文档内容
     *
     * @return 文档内容列表
     */
    List<List<Document>> separateDocs(AiKnowledgeDocSeparateParam param);

}
