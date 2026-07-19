package org.bedrock.ai.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bedrock.ai.param.*;
import org.bedrock.ai.service.IAiKnowledgeDocService;
import org.bedrock.ai.vo.AiKnowledgeDocDetailVO;
import org.bedrock.ai.vo.AiKnowledgeDocListVO;
import org.bedrock.common.auth.constant.RoleAliasConstant;
import org.bedrock.common.boot.base.BaseController;
import org.bedrock.common.code.api.R;
import org.bedrock.common.log.annotation.OperationLog;
import org.bedrock.common.mybatisplus.base.Query;
import org.bedrock.common.mybatisplus.util.PageUtil;
import org.bedrock.common.security.annotation.PrePermissionCheck;
import org.springframework.ai.document.Document;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 知识库文档控制器
 */
@Tag(name = "知识库文档 控制器")
@RestController
@RequestMapping("/ai-knowledge-doc")
@RequiredArgsConstructor
public class AiKnowledgeDocController extends BaseController {

    private final IAiKnowledgeDocService aiKnowledgeDocService;

    /**
     * 手动创建空文档（手动录入数据集）
     */
    @Operation(summary = "手动添加知识库文档")
    @PostMapping("/create")
    @ApiOperationSupport(order = 1)
    public R<Void> create(@RequestBody AiKnowledgeDocCreateParam param) {
        return status(aiKnowledgeDocService.create(param));
    }

    /**
     * 文件上传入库，含分片处理与向量写入
     */
    @Operation(summary = "提交知识库文档")
    @PostMapping("/submit")
    @ApiOperationSupport(order = 2)
    public R<Void> submit(@RequestBody AiKnowledgeDocUploadParam param) {
        return status(aiKnowledgeDocService.submit(param));
    }

    /**
     * 文件上传入库，含分片处理与向量写入
     */
    @Operation(summary = "重新拆分文档")
    @PostMapping("/again-split")
    @ApiOperationSupport(order = 3)
    public R<Void> againSplit(@RequestBody AiKnowledgeDocAgainSplitParam param) {
        return status(aiKnowledgeDocService.againSplit(param));
    }

    @Operation(summary = "修改知识库文档")
    @PutMapping("/edit")
    @ApiOperationSupport(order = 4)
    @OperationLog(type = "知识库文档管理", subType = "修改", success = "修改文档: {{#param.docTitle}}", extra = "{TO_JSON{#param}}", condition = "{{#_errorMsg == null}}")
    public R<Void> edit(@RequestBody AiKnowledgeDocEditParam param) {
        return status(aiKnowledgeDocService.edit(param));
    }

    /**
     * 逻辑删除文档，并级联删除其下分片
     */
    @Operation(summary = "删除知识库文档")
    @DeleteMapping("/remove")
    @ApiOperationSupport(order = 5)
    @OperationLog(type = "知识库文档管理", subType = "删除", success = "删除文档 id: {{#id}}", extra = "{{#id}}", condition = "{{#_errorMsg == null}}")
    @PrePermissionCheck("hasAnyRole('" + RoleAliasConstant.ROLE_ADMIN + "','" + RoleAliasConstant.ROLE_ADMINISTRATOR + "')")
    public R<Void> remove(@Parameter(description = "文档 id", required = true, name = "id", in = ParameterIn.QUERY) Long id) {
        return status(aiKnowledgeDocService.removeById(id));
    }

    @Operation(summary = "知识库文档详情")
    @GetMapping("/detail")
    @ApiOperationSupport(order = 6)
    public R<AiKnowledgeDocDetailVO> detail(@Parameter(description = "文档 id", required = true, name = "id", in = ParameterIn.QUERY) Long id) {
        return success(aiKnowledgeDocService.detail(id));
    }

    @Operation(summary = "启用禁用知识库文档")
    @PutMapping("/enable-status")
    @ApiOperationSupport(order = 7)
    @OperationLog(type = "知识库文档管理", subType = "启用禁用", success = "文档【{{#docTitle}}】状态修改为【{{#status == 1 ? '启用':'禁用'}}】", condition = "{{#_errorMsg == null}}")
    public R<Void> enableStatus(@Parameter(description = "文档 id", required = true, name = "id", in = ParameterIn.QUERY) Long id,
                                @Parameter(description = "配置状态（1=启用，0=禁用）", required = true, name = "status", in = ParameterIn.QUERY) Integer status) {
        return status(aiKnowledgeDocService.enableStatus(id, status));
    }

    @Operation(summary = "知识库文档列表")
    @GetMapping("/list")
    @ApiOperationSupport(order = 8)
    public R<List<AiKnowledgeDocListVO>> list(AiKnowledgeDocListParam param) {
        return success(aiKnowledgeDocService.selectAiKnowledgeDocList(param));
    }

    @Operation(summary = "知识库文档分页列表")
    @GetMapping("/list-page")
    @ApiOperationSupport(order = 9)
    public R<IPage<AiKnowledgeDocListVO>> listPage(Query query, AiKnowledgeDocListParam param) {
        return success(aiKnowledgeDocService.selectAiKnowledgeDocListPage(PageUtil.getPage(query), param));
    }

    /**
     * 拆分单个文档，用于上传向导分片预览
     */
    @Operation(summary = "拆分文档")
    @PostMapping("/separate-doc")
    @ApiOperationSupport(order = 10)
    public R<List<Document>> separateDoc(@RequestBody AiKnowledgeDocSeparateParam param) {
        return success(aiKnowledgeDocService.separateDoc(param));
    }

    /**
     * 批量拆分多个文档，用于文件上传入库前预处理
     */
    @Operation(summary = "拆分文档列表")
    @PostMapping("/separate-docs")
    @ApiOperationSupport(order = 11)
    public R<List<List<Document>>> separateDocs(@RequestBody AiKnowledgeDocSeparateParam param) {
        return success(aiKnowledgeDocService.separateDocs(param));
    }

}
