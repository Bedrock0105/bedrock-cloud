package org.bedrock.ai.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.bedrock.ai.component.AiChatKit;
import org.bedrock.ai.entity.AiKnowledgeDoc;
import org.bedrock.ai.enums.AiErrorEnum;
import org.bedrock.ai.enums.DocSourceType;
import org.bedrock.ai.mapper.AiKnowledgeDocMapper;
import org.bedrock.ai.param.*;
import org.bedrock.ai.service.IAiKnowledgeChunkService;
import org.bedrock.ai.service.IAiKnowledgeDocService;
import org.bedrock.ai.vo.AiKnowledgeDocDetailVO;
import org.bedrock.ai.vo.AiKnowledgeDocListVO;
import org.bedrock.common.auth.util.AuthUtil;
import org.bedrock.common.code.util.CollectionUtil;
import org.bedrock.common.code.util.FileUtil;
import org.bedrock.common.log.exception.ServiceException;
import org.bedrock.common.log.operation.support.LogRecordContext;
import org.bedrock.common.mybatisplus.base.BaseEntity;
import org.bedrock.common.mybatisplus.base.BaseServiceImpl;
import org.bedrock.common.mybatisplus.constant.BedrockDBConstant;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 知识库文档服务实现
 * <p>负责文档 CRUD、文件上传入库、分片预览拆分等</p>
 */
@Service
@RequiredArgsConstructor
public class AiKnowledgeDocServiceImpl extends BaseServiceImpl<AiKnowledgeDocMapper, AiKnowledgeDoc>
        implements IAiKnowledgeDocService {

    private final IAiKnowledgeChunkService aiKnowledgeChunkService;

    private final AiChatKit aiChatKit;

    /**
     * 手动创建空文档（手动数据集），默认禁用状态
     */
    @Override
    public boolean create(AiKnowledgeDocCreateParam param) {
        AiKnowledgeDoc doc = new AiKnowledgeDoc();
        doc.setDocTitle(param.getDocTitle());
        doc.setDocSourceType(DocSourceType.MANUAL_TEXT);
        doc.setStatus(BedrockDBConstant.DB_STATUS_DISABLE);
        doc.setKnowledgeId(param.getKnowledgeId());
        return save(doc);
    }

    /**
     * 文件数据集上传入库：先拆分文档，再逐文件保存文档记录及分片
     */
    @Override
    @Transactional
    public boolean submit(AiKnowledgeDocUploadParam param) {
        List<List<Document>> separateDocs = separateDocs(param.getSeparateParam());
        for (int i = 0; i < param.getSeparateParam().fileItems().size(); i++) {
            AiKnowledgeDocSeparateParam.DocFileItem item = param.getSeparateParam().fileItems().get(i);
            AiKnowledgeDoc doc = new AiKnowledgeDoc();
            doc.setDocTitle(item.fileName());
            doc.setKnowledgeId(param.getKnowledgeId());
            doc.setDocSourceType(DocSourceType.UPLOAD_FILE);
            doc.setFileUrl(item.fileUrl());
            doc.setFileSize(item.size());
            doc.setFileSuffix(FileUtil.getExtension(item.fileName()));
            doc.setSliceMode(param.getSeparateParam().mode());
            doc.setStatus(BedrockDBConstant.DB_STATUS_NORMAL);
            doc.setTenantId(AuthUtil.getTenantId());
            doc.setCreateUserId(AuthUtil.getUserId());
            doc.setUpdateUserId(AuthUtil.getUserId());
            save(doc);
            aiKnowledgeChunkService.saveDocument(doc, separateDocs.get(i));
        }
        return true;
    }

    /**
     * 重新拆分文档
     */
    @Override
    public boolean againSplit(AiKnowledgeDocAgainSplitParam param) {
        List<Document> documents = separateDoc(param.getSeparateParam());
        AiKnowledgeDoc knowledgeDoc = this.getById(param.getDocId());
        logicRemoveChunksByDocId(knowledgeDoc.getId());
        aiKnowledgeChunkService.saveDocument(knowledgeDoc, documents);
        return true;
    }

    /**
     * 修改文档标题
     */
    @Override
    public boolean edit(AiKnowledgeDocEditParam param) {
        return update(Wrappers.<AiKnowledgeDoc>lambdaUpdate()
                .set(AiKnowledgeDoc::getDocTitle, param.getDocTitle())
                .set(BaseEntity::getUpdateTime, LocalDateTime.now())
                .set(BaseEntity::getUpdateUserId, AuthUtil.getUserId())
                .eq(BaseEntity::getId, param.getId()));
    }

    /**
     * 逻辑删除文档，并级联删除其下分片及向量
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Long id) {
        logicRemoveChunksByDocId(id);
        return logicRemoveById(id);
    }

    /**
     * 查询文档详情
     */
    @Override
    public AiKnowledgeDocDetailVO detail(Long id) {
        AiKnowledgeDocDetailVO detailVO = baseMapper.selectDetailById(id);
        if (detailVO == null) {
            throw new ServiceException(AiErrorEnum.KNOWLEDGE_DOC_NOT_FOUND.getCode(),
                    AiErrorEnum.KNOWLEDGE_DOC_NOT_FOUND.getMessage());
        }
        return detailVO;
    }

    /**
     * 按条件查询文档列表（无分页）
     */
    @Override
    public List<AiKnowledgeDocListVO> selectAiKnowledgeDocList(AiKnowledgeDocListParam param) {
        return baseMapper.selectAiKnowledgeDocList(null, param);
    }

    /**
     * 按条件查询文档分页列表
     */
    @Override
    public IPage<AiKnowledgeDocListVO> selectAiKnowledgeDocListPage(IPage<AiKnowledgeDocListVO> iPage,
                                                                    AiKnowledgeDocListParam param) {
        return iPage.setRecords(baseMapper.selectAiKnowledgeDocList(iPage, param));
    }

    /**
     * 启用/禁用文档
     */
    @Override
    public boolean enableStatus(Long id, Integer status) {
        AiKnowledgeDoc doc = getById(id);
        if (doc == null) {
            throw new ServiceException(AiErrorEnum.KNOWLEDGE_DOC_NOT_FOUND.getCode(),
                    AiErrorEnum.KNOWLEDGE_DOC_NOT_FOUND.getMessage());
        }
        LogRecordContext.putVariable("docTitle", doc.getDocTitle());
        LogRecordContext.putVariable("status", status);
        return update(Wrappers.<AiKnowledgeDoc>lambdaUpdate()
                .eq(BaseEntity::getId, id)
                .set(AiKnowledgeDoc::getStatus, status));
    }

    /**
     * 拆分单个文档（上传向导预览用）
     * <p>读取 OSS 文件 URL，按指定分片模式返回 Document 列表</p>
     */
    @Override
    public List<Document> separateDoc(AiKnowledgeDocSeparateParam param) {
        if (CollectionUtil.isEmpty(param.fileItems())) {
            return List.of();
        }
        List<Document> documents = aiChatKit.documentReader().readDocuments(param.fileItems().get(0).fileUrl());
        return aiChatKit.transformer().transformer(documents, param.mode(), param.params());
    }

    /**
     * 批量拆分多个文档（上传入库前预处理）
     */
    @Override
    public List<List<Document>> separateDocs(AiKnowledgeDocSeparateParam param) {
        if (CollectionUtil.isEmpty(param.fileItems())) {
            return List.of();
        }
        List<List<Document>> documents = new ArrayList<>();

        for (AiKnowledgeDocSeparateParam.DocFileItem item : param.fileItems()) {
            documents.add(aiChatKit.transformer()
                    .transformer(aiChatKit.documentReader()
                                    .readDocuments(item.fileUrl()),
                            param.mode(),
                            param.params()));
        }
        return documents;
    }

    /**
     * 按文档 ID 删除其下全部分片
     */
    private void logicRemoveChunksByDocId(Long docId) {
        aiKnowledgeChunkService.removeByKnowledgeDocId(docId);
    }
}
