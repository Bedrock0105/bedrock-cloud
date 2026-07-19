package org.bedrock.ai.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bedrock.ai.constant.AiConstant;
import org.bedrock.ai.dto.AiChatOptions;
import org.bedrock.ai.entity.AiKnowledgeChunk;
import org.bedrock.ai.entity.AiKnowledgeDoc;
import org.bedrock.ai.enums.AiErrorEnum;
import org.bedrock.ai.mapper.AiKnowledgeChunkMapper;
import org.bedrock.ai.mapper.AiKnowledgeDocMapper;
import org.bedrock.ai.param.AiKnowledgeChunkListParam;
import org.bedrock.ai.param.AiKnowledgeChunkSearchParam;
import org.bedrock.ai.param.AiKnowledgeChunkSubmitParam;
import org.bedrock.ai.service.IAiKnowledgeChunkService;
import org.bedrock.ai.service.IAiKnowledgeService;
import org.bedrock.ai.utils.AiTokenUtil;
import org.bedrock.ai.vo.AiKnowledgeChunkDetailVO;
import org.bedrock.ai.vo.AiKnowledgeChunkListVO;
import org.bedrock.ai.vo.AiKnowledgeDetailVO;
import org.bedrock.ai.vo.AiKnowledgeDocDetailVO;
import org.bedrock.common.auth.util.AuthUtil;
import org.bedrock.common.code.util.SpringUtil;
import org.bedrock.common.code.util.StringUtil;
import org.bedrock.common.log.exception.ServiceException;
import org.bedrock.common.log.operation.support.LogRecordContext;
import org.bedrock.common.mybatisplus.base.BaseEntity;
import org.bedrock.common.mybatisplus.base.BaseServiceImpl;
import org.bedrock.common.mybatisplus.constant.BedrockDBConstant;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 知识库文档分片服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiKnowledgeChunkServiceImpl extends BaseServiceImpl<AiKnowledgeChunkMapper, AiKnowledgeChunk>
        implements IAiKnowledgeChunkService {

    /**
     * 向量库 metadata 过滤表达式构建器
     */
    private static final FilterExpressionBuilder FILTER_EXPRESSION_BUILDER = new FilterExpressionBuilder();

    private final AiKnowledgeDocMapper aiKnowledgeDocMapper;

    /**
     * 延迟注入，避免循环依赖
     */
    private IAiKnowledgeService self;

    /**
     * 手动新增分片，自动分配序号并写入向量库
     */
    @Override
    public boolean submit(AiKnowledgeChunkSubmitParam param) {
        AiKnowledgeDocDetailVO docDetailVO = aiKnowledgeDocMapper.selectDetailById(param.getDocId());
        int maxChunkNo = baseMapper.getMaxChunkNo(param.getDocId());
        Document document = new Document(param.getChunkContent());
        AiKnowledgeChunk aiKnowledgeChunk = buildAiKnowledgeChunk(document, docDetailVO.getKnowledgeId(), docDetailVO.getId(), maxChunkNo + 1);
        IAiKnowledgeService service = getSelf();
        AiKnowledgeDetailVO detail = service.detail(docDetailVO.getKnowledgeId());
        VectorStore vectorStore = service.getVectorStore(detail.getEmbeddingModelId(), detail.getVectorDbId());
        vectorStore.add(List.of(document));
        return save(aiKnowledgeChunk);
    }

    /**
     * 编辑分片，先删旧向量再写入新向量
     */
    @Override
    public boolean edit(AiKnowledgeChunkSubmitParam param) {
        AiKnowledgeChunk chunk = getById(param.getId());
        Document document = new Document(param.getChunkContent());
        AiKnowledgeChunk aiKnowledgeChunk = buildAiKnowledgeChunk(document, chunk.getKnowledgeId(), chunk.getDocId(), chunk.getChunkNo());
        aiKnowledgeChunk.setId(chunk.getId());
        document.getMetadata().put(AiConstant.VECTOR_META_KEY_CHUNK_ID, StringUtil.toStr(aiKnowledgeChunk.getId()));

        IAiKnowledgeService service = getSelf();
        AiKnowledgeDetailVO detail = service.detail(chunk.getKnowledgeId());
        VectorStore vectorStore = service.getVectorStore(detail.getEmbeddingModelId(), detail.getVectorDbId());
        vectorStore.delete(List.of(StringUtil.toStr(aiKnowledgeChunk.getId())));
        vectorStore.add(List.of(document));
        return updateById(aiKnowledgeChunk);
    }

    /**
     * 逻辑删除分片，并同步删除向量
     */
    @Override
    public boolean removeById(Long id) {
        AiKnowledgeChunk chunk = getById(id);
        if (chunk == null) {
            throw new ServiceException(AiErrorEnum.KNOWLEDGE_CHUNK_NOT_FOUND.getCode(),
                    AiErrorEnum.KNOWLEDGE_CHUNK_NOT_FOUND.getMessage());
        }
        IAiKnowledgeService service = getSelf();
        AiKnowledgeDetailVO detail = service.detail(chunk.getKnowledgeId());
        VectorStore vectorStore = service.getVectorStore(detail.getEmbeddingModelId(), detail.getVectorDbId());
        vectorStore.delete(List.of(StringUtil.toStr(id)));
        return logicRemoveById(id);
    }

    /**
     * 查询分片详情
     */
    @Override
    public AiKnowledgeChunkDetailVO detail(Long id) {
        AiKnowledgeChunkDetailVO detailVO = baseMapper.selectDetailById(id);
        if (detailVO == null) {
            throw new ServiceException(AiErrorEnum.KNOWLEDGE_CHUNK_NOT_FOUND.getCode(),
                    AiErrorEnum.KNOWLEDGE_CHUNK_NOT_FOUND.getMessage());
        }
        return detailVO;
    }

    /**
     * 按条件查询分片列表（无分页）
     */
    @Override
    public List<AiKnowledgeChunkListVO> selectAiKnowledgeChunkList(AiKnowledgeChunkListParam param) {
        return baseMapper.selectAiKnowledgeChunkList(null, param);
    }

    /**
     * 按条件查询分片分页列表
     */
    @Override
    public IPage<AiKnowledgeChunkListVO> selectAiKnowledgeChunkListPage(IPage<AiKnowledgeChunkListVO> iPage,
                                                                        AiKnowledgeChunkListParam param) {
        return iPage.setRecords(baseMapper.selectAiKnowledgeChunkList(iPage, param));
    }

    /**
     * 启用/禁用分片，禁用时仅移除向量，启用时重新写入
     */
    @Override
    public boolean enableStatus(Long id, Integer status) {
        AiKnowledgeChunk chunk = getById(id);
        if (chunk == null) {
            throw new ServiceException(AiErrorEnum.KNOWLEDGE_CHUNK_NOT_FOUND.getCode(),
                    AiErrorEnum.KNOWLEDGE_CHUNK_NOT_FOUND.getMessage());
        }
        LogRecordContext.putVariable("chunkNo", chunk.getChunkNo());
        LogRecordContext.putVariable("status", status);

        Document document = new Document(chunk.getChunkContent());
        AiKnowledgeChunk aiKnowledgeChunk = buildAiKnowledgeChunk(document, chunk.getKnowledgeId(), chunk.getDocId(), chunk.getChunkNo());
        document.getMetadata().put(AiConstant.VECTOR_META_KEY_CHUNK_ID, StringUtil.toStr(chunk.getId()));
        IAiKnowledgeService service = getSelf();
        AiKnowledgeDetailVO detail = service.detail(chunk.getKnowledgeId());
        VectorStore vectorStore = service.getVectorStore(detail.getEmbeddingModelId(), detail.getVectorDbId());
        vectorStore.delete(List.of(StringUtil.toStr(chunk.getId())));

        if (!BedrockDBConstant.DB_STATUS_DISABLE.equals(status)) {
            vectorStore.add(List.of(document));
        }
        return update(Wrappers.<AiKnowledgeChunk>lambdaUpdate()
                .eq(BaseEntity::getId, id)
                .set(AiKnowledgeChunk::getVectorId, aiKnowledgeChunk.getVectorId())
                .set(BaseEntity::getUpdateTime, LocalDateTime.now())
                .set(BaseEntity::getUpdateUserId, AuthUtil.getUserId())
                .set(AiKnowledgeChunk::getStatus, status));
    }

    /**
     * 文档上传后批量保存分片（异步）
     */
    @Override
    @Async("aiExecutor")
    public void saveDocument(AiKnowledgeDoc doc, List<Document> documentList) {
        IAiKnowledgeService service = getSelf();
        AiKnowledgeDetailVO detail = service.detail(doc.getKnowledgeId());
        VectorStore vectorStore = service.getVectorStore(detail.getEmbeddingModelId(), detail.getVectorDbId());
        List<AiKnowledgeChunk> chunkList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < documentList.size(); i++) {
            Document document = documentList.get(i);
            AiKnowledgeChunk aiKnowledgeChunk = buildAiKnowledgeChunk(document, doc.getKnowledgeId(), doc.getId(), i + 1);
            aiKnowledgeChunk.setCreateUserId(doc.getCreateUserId());
            aiKnowledgeChunk.setUpdateUserId(doc.getCreateUserId());
            aiKnowledgeChunk.setTenantId(doc.getTenantId());
            aiKnowledgeChunk.setStatus(BedrockDBConstant.DB_STATUS_NORMAL);
            aiKnowledgeChunk.setCreateTime(now);
            aiKnowledgeChunk.setUpdateTime(now);
            chunkList.add(aiKnowledgeChunk);
        }
        if (!chunkList.isEmpty()) {
            baseMapper.insertBatch(chunkList);
        }
        try {
            vectorStore.add(documentList);
        } catch (Exception e) {
            log.error("向量存储添加失败", e);
            baseMapper.update(Wrappers.<AiKnowledgeChunk>lambdaUpdate()
                    .eq(AiKnowledgeChunk::getDocId, doc.getId())
                    .set(AiKnowledgeChunk::getEmbedStatus, AiConstant.EMBED_STATUS_FAILED));
            return;
        }
        baseMapper.update(Wrappers.<AiKnowledgeChunk>lambdaUpdate()
                .eq(AiKnowledgeChunk::getDocId, doc.getId())
                .set(AiKnowledgeChunk::getEmbedStatus, AiConstant.EMBED_STATUS_SUCCESS));
    }

    /**
     * 按文档 ID 逻辑删除分片，并批量清理向量
     */
    @Override
    public void removeByKnowledgeDocId(Long docId) {
        AiKnowledgeDocDetailVO docDetailVO = aiKnowledgeDocMapper.selectDetailById(docId);
        IAiKnowledgeService service = getSelf();
        AiKnowledgeDetailVO detail = service.detail(docDetailVO.getKnowledgeId());
        VectorStore vectorStore = service.getVectorStore(detail.getEmbeddingModelId(), detail.getVectorDbId());
        this.update(Wrappers.<AiKnowledgeChunk>lambdaUpdate()
                .eq(AiKnowledgeChunk::getDocId, docId)
                .set(BaseEntity::getIsDeleted, BedrockDBConstant.DB_IS_DELETED)
                .set(BaseEntity::getUpdateTime, LocalDateTime.now())
                .set(BaseEntity::getUpdateUserId, AuthUtil.getUserId()));

        Filter.Expression build = FILTER_EXPRESSION_BUILDER.eq(AiConstant.VECTOR_META_KEY_DOC_ID, StringUtil.toStr(docId)).build();
        vectorStore.delete(build);
    }

    /**
     * 按知识库 ID 逻辑删除分片，并批量清理向量
     */
    @Override
    public void removeByKnowledgeId(Long knowledgeId) {
        IAiKnowledgeService service = getSelf();
        AiKnowledgeDetailVO detail = service.detail(knowledgeId);
        VectorStore vectorStore = service.getVectorStore(detail.getEmbeddingModelId(), detail.getVectorDbId());
        this.update(Wrappers.<AiKnowledgeChunk>lambdaUpdate()
                .eq(AiKnowledgeChunk::getKnowledgeId, knowledgeId)
                .set(BaseEntity::getIsDeleted, BedrockDBConstant.DB_IS_DELETED)
                .set(BaseEntity::getUpdateTime, LocalDateTime.now())
                .set(BaseEntity::getUpdateUserId, AuthUtil.getUserId()));
        Filter.Expression build = FILTER_EXPRESSION_BUILDER.eq(AiConstant.VECTOR_META_KEY_KNOWLEDGE_ID, StringUtil.toStr(knowledgeId)).build();
        vectorStore.delete(build);
    }

    /**
     * 构建分片实体，填充 metadata 并统计 Token 数
     */
    private AiKnowledgeChunk buildAiKnowledgeChunk(Document document, Long knowledgeId, Long docId, int chunkNo) {
        AiKnowledgeChunk chunk = new AiKnowledgeChunk();
        chunk.setId(IdWorker.getId());
        chunk.setKnowledgeId(knowledgeId);
        chunk.setDocId(docId);
        chunk.setChunkNo(chunkNo);
        chunk.setChunkContent(document.getText());
        chunk.setChunkTokenCount(AiTokenUtil.getTokenCount(document.getText()));
        chunk.setVectorId(document.getId());
        document.getMetadata().put(AiConstant.VECTOR_META_KEY_CHUNK_ID, StringUtil.toStr(chunk.getId()));
        document.getMetadata().put(AiConstant.VECTOR_META_KEY_KNOWLEDGE_ID, StringUtil.toStr(chunk.getKnowledgeId()));
        document.getMetadata().put(AiConstant.VECTOR_META_KEY_DOC_ID, StringUtil.toStr(chunk.getDocId()));
        return chunk;
    }

    @Override
    public List<Document> search(AiKnowledgeChunkSearchParam param) {
        AiChatOptions.KnowledgeParam knowledgeParam = param.getKnowledgeParam();
        AiKnowledgeDetailVO detail = getSelf().detail(Long.valueOf(knowledgeParam.knowledgeId()));
        VectorStore vectorStore = getSelf().getVectorStore(detail.getEmbeddingModelId(), detail.getVectorDbId());
        return vectorStore.similaritySearch(SearchRequest.builder()
                .query(param.getQuery())
                .topK(knowledgeParam.topK())
                .similarityThreshold(knowledgeParam.similarity())
                .filterExpression(FILTER_EXPRESSION_BUILDER.eq(
                        AiConstant.VECTOR_META_KEY_KNOWLEDGE_ID,
                        knowledgeParam.knowledgeId()).build())
                .build());
    }

    /**
     * 获取 IAiKnowledgeService 代理，避免循环依赖
     */
    public IAiKnowledgeService getSelf() {
        if (self == null) {
            self = SpringUtil.getBean(IAiKnowledgeService.class);
        }
        return self;
    }
}
