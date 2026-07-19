package org.bedrock.ai.advisor.knowledge;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.bedrock.ai.constant.AiConstant;
import org.bedrock.ai.dto.AiChatOptions;
import org.bedrock.ai.entity.AiKnowledgeDoc;
import org.bedrock.ai.mapper.AiKnowledgeChunkMapper;
import org.bedrock.ai.mapper.AiKnowledgeDocMapper;
import org.bedrock.ai.service.IAiKnowledgeService;
import org.bedrock.ai.vo.AiChatRecordDetailVO;
import org.bedrock.ai.vo.AiKnowledgeDetailVO;
import org.bedrock.common.ai.advisor.knowledge.KnowledgeRetriever;
import org.bedrock.common.code.util.CollectionUtil;
import org.bedrock.common.code.util.StringUtil;
import org.bedrock.common.log.exception.ServiceException;
import org.bedrock.common.mybatisplus.base.BaseEntity;
import org.bedrock.common.mybatisplus.constant.BedrockDBConstant;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 知识库向量检索业务实现，供 {@link org.bedrock.common.ai.advisor.KnowledgeRetrievalAdvisor} 调用。
 * <p>
 * 会话 {@link AiChatRecordDetailVO#getChatOptions()} 中可配置多个知识库及其检索参数
 * （相似度阈值、TopK），本类按配置逐个知识库召回并合并结果。
 * <p>
 * <b>召回统计口径：</b>
 * <ul>
 *   <li><b>分片 {@code recall_count}</b>：向量检索命中的每个分片（vector）各 +1，
 *       同一文档命中 3 个分片则分片表合计 +3</li>
 *   <li><b>文档 {@code recall_count}</b>：同一轮用户提问中，同一文档无论命中几个分片，
 *       文档维度只 +1（按 {@code docId} 去重）；表示「该文档被引用过多少次」，
 *       而非分片命中次数之和</li>
 * </ul>
 * {@link #transformRetrievedDocuments} 会同时更新分片与文档召回次数。
 */
@Component
@RequiredArgsConstructor
public class AiKnowledgeRetrieval implements KnowledgeRetriever {

    /**
     * 向量库 metadata 过滤表达式构建器，用于按 knowledgeId 限定检索范围
     */
    private static final FilterExpressionBuilder FILTER_EXPRESSION_BUILDER = new FilterExpressionBuilder();

    private final IAiKnowledgeService aiKnowledgeService;

    private final AiKnowledgeChunkMapper aiKnowledgeChunkMapper;

    private final AiKnowledgeDocMapper aiKnowledgeDocMapper;

    /**
     * 构建基础检索请求，具体 topK / 相似度 / 过滤条件在 {@link #retrieve} 中按知识库参数覆盖。
     */
    @Override
    public @Nullable SearchRequest getSearchRequest(String text, List<Message> history, Map<String, Object> context) {
        if (StringUtil.isBlank(text)) {
            return null;
        }
        return SearchRequest.builder()
                .query(text)
                .build();
    }

    /**
     * 按会话配置遍历知识库，分别执行向量相似度检索并合并结果。
     * <p>
     * 每个知识库使用独立的 {@link VectorStore}（由 embedding 模型 + 向量库配置决定），
     * 并通过 metadata 过滤（knowledgeId + 排除禁用文档）避免跨库污染与禁用内容召回。
     */
    @Override
    public List<Document> retrieve(SearchRequest request, String text, List<Message> history, Map<String, Object> context) {
        List<Document> documents = new ArrayList<>();
        if (!(context.get(AiConstant.CTX_CHAT_RECORD) instanceof AiChatRecordDetailVO recordDetailVO)) {
            return documents;
        }
        AiChatOptions chatOptions = recordDetailVO.getChatOptions();
        if (chatOptions == null || CollectionUtil.isEmpty(chatOptions.getKnowledgeParams())) {
            return documents;
        }
        for (AiChatOptions.KnowledgeParam knowledgeParam : chatOptions.getKnowledgeParams()) {
            documents.addAll(retrieveFromKnowledge(request, knowledgeParam));
        }
        return documents;
    }

    /**
     * 检索结果后置处理：累加分片与文档召回次数。
     * <p>
     * 分片：按 {@link Document#getId()}（{@code vector_id}）各 +1；
     * 文档：从 metadata {@link AiConstant#VECTOR_META_KEY_DOC_ID} 取 docId 去重后各 +1。
     */
    @Override
    public List<Document> transformRetrievedDocuments(String text, List<Message> history, List<Document> documents, Map<String, Object> context) {
        if (CollectionUtil.isEmpty(documents)) {
            return documents;
        }
        List<String> vectorIds = documents.stream().map(Document::getId).toList();
        aiKnowledgeChunkMapper.updateRetrievalCountIncr(vectorIds);

        List<Long> docIds = documents.stream()
                .map(document -> document.getMetadata().get(AiConstant.VECTOR_META_KEY_DOC_ID))
                .filter(Objects::nonNull)
                .map(Object::toString)
                .filter(StringUtil::isNotBlank)
                .map(Long::valueOf)
                .distinct()
                .toList();
        if (CollectionUtil.isNotEmpty(docIds)) {
            aiKnowledgeDocMapper.updateRecallCountIncr(docIds);
        }
        return documents;
    }

    /**
     * 本实现按会话内多知识库分别获取 VectorStore，不在此提供单一实例。
     */
    @Override
    public @NonNull VectorStore getVectorStore(Map<String, Object> context) {
        throw new ServiceException("Not implemented");
    }

    /**
     * 对单个知识库执行向量检索；知识库不存在或已禁用时返回空列表。
     */
    private List<Document> retrieveFromKnowledge(SearchRequest request, AiChatOptions.KnowledgeParam knowledgeParam) {
        Long knowledgeId = Long.valueOf(knowledgeParam.knowledgeId());
        AiKnowledgeDetailVO detail = aiKnowledgeService.detail(knowledgeId);
        if (detail == null || detail.getStatus() == BedrockDBConstant.DB_STATUS_DISABLE) {
            return List.of();
        }
        SearchRequest searchRequest = SearchRequest.from(request)
                .similarityThreshold(knowledgeParam.similarity())
                .topK(knowledgeParam.topK())
                .filterExpression(buildFilterExpression(knowledgeParam.knowledgeId(), knowledgeId))
                .build();
        VectorStore vectorStore = aiKnowledgeService.getVectorStore(
                detail.getEmbeddingModelId(), detail.getVectorDbId());
        return vectorStore.similaritySearch(searchRequest);
    }

    /**
     * 构建向量库过滤条件：限定 knowledgeId；若存在禁用文档则排除其 docId。
     * <p>metadata 中 docId / knowledgeId 存为字符串，过滤条件同步使用字符串。
     */
    private Filter.Expression buildFilterExpression(String knowledgeIdStr, Long knowledgeId) {
        FilterExpressionBuilder.Op knowledgeEq = FILTER_EXPRESSION_BUILDER
                .eq(AiConstant.VECTOR_META_KEY_KNOWLEDGE_ID, knowledgeIdStr);
        List<Object> disabledDocIds = listDisabledDocIds(knowledgeId);
        if (CollectionUtil.isEmpty(disabledDocIds)) {
            return knowledgeEq.build();
        }
        return FILTER_EXPRESSION_BUILDER.and(
                knowledgeEq,
                FILTER_EXPRESSION_BUILDER.nin(AiConstant.VECTOR_META_KEY_DOC_ID, disabledDocIds)
        ).build();
    }

    /**
     * 查询知识库下已禁用文档的 id（字符串形式，与向量 metadata 一致）。
     */
    private List<Object> listDisabledDocIds(Long knowledgeId) {
        return aiKnowledgeDocMapper.selectList(Wrappers.<AiKnowledgeDoc>lambdaQuery()
                        .select(BaseEntity::getId)
                        .eq(AiKnowledgeDoc::getKnowledgeId, knowledgeId)
                        .eq(AiKnowledgeDoc::getStatus, BedrockDBConstant.DB_STATUS_DISABLE))
                .stream()
                .map(BaseEntity::getId)
                .<Object>map(String::valueOf)
                .toList();
    }
}
