package org.bedrock.ai.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bedrock.ai.param.AiKnowledgeListParam;
import org.bedrock.ai.param.AiKnowledgeSubmitParam;
import org.bedrock.ai.service.IAiKnowledgeService;
import org.bedrock.ai.vo.AiKnowledgeDetailVO;
import org.bedrock.ai.vo.AiKnowledgeListVO;
import org.bedrock.common.auth.constant.RoleAliasConstant;
import org.bedrock.common.boot.base.BaseController;
import org.bedrock.common.code.api.R;
import org.bedrock.common.log.annotation.OperationLog;
import org.bedrock.common.mybatisplus.base.Query;
import org.bedrock.common.mybatisplus.util.PageUtil;
import org.bedrock.common.security.annotation.PrePermissionCheck;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AI 知识库控制器
 * <p>提供知识库的增删改查与启停管理</p>
 */
@Tag(name = "AI 知识库 控制器")
@RestController
@RequestMapping("/ai-knowledge")
@RequiredArgsConstructor
public class AiKnowledgeController extends BaseController {

    private final IAiKnowledgeService aiKnowledgeService;

    @Operation(summary = "添加知识库")
    @PostMapping("/submit")
    @ApiOperationSupport(order = 1)
    @OperationLog(type = "知识库管理", subType = "添加", success = "新增知识库: {{#param.knowledgeName}}", extra = "{TO_JSON{#param}}", condition = "{{#_errorMsg == null}}")
    public R<Void> submit(@RequestBody AiKnowledgeSubmitParam param) {
        return status(aiKnowledgeService.submit(param));
    }

    @Operation(summary = "修改知识库")
    @PutMapping("/edit")
    @ApiOperationSupport(order = 2)
    @OperationLog(type = "知识库管理", subType = "修改", success = "修改知识库: {{#param.knowledgeName}}", extra = "{TO_JSON{#param}}", condition = "{{#_errorMsg == null}}")
    public R<Void> edit(@RequestBody AiKnowledgeSubmitParam param) {
        return status(aiKnowledgeService.edit(param));
    }

    @Operation(summary = "删除知识库")
    @DeleteMapping("/remove")
    @ApiOperationSupport(order = 3)
    @OperationLog(type = "知识库管理", subType = "删除", success = "删除知识库 id: {{#id}}", extra = "{{#id}}", condition = "{{#_errorMsg == null}}")
    @PrePermissionCheck("hasAnyRole('" + RoleAliasConstant.ROLE_ADMIN + "','" + RoleAliasConstant.ROLE_ADMINISTRATOR + "')")
    public R<Void> remove(@Parameter(description = "知识库 id", required = true, name = "id", in = ParameterIn.QUERY) Long id) {
        return status(aiKnowledgeService.removeById(id));
    }

    @Operation(summary = "知识库详情")
    @GetMapping("/detail")
    @ApiOperationSupport(order = 4)
    public R<AiKnowledgeDetailVO> detail(@Parameter(description = "知识库 id", required = true, name = "id", in = ParameterIn.QUERY) Long id) {
        return success(aiKnowledgeService.detail(id));
    }

    @Operation(summary = "启用禁用知识库")
    @PutMapping("/enable-status")
    @ApiOperationSupport(order = 5)
    @OperationLog(type = "知识库管理", subType = "启用禁用", success = "知识库【{{#knowledgeName}}】状态修改为【{{#status == 1 ? '启用':'禁用'}}】", condition = "{{#_errorMsg == null}}")
    public R<Void> enableStatus(@Parameter(description = "知识库 id", required = true, name = "id", in = ParameterIn.QUERY) Long id,
                                @Parameter(description = "配置状态（1=启用，0=禁用）", required = true, name = "status", in = ParameterIn.QUERY) Integer status) {
        return status(aiKnowledgeService.enableStatus(id, status));
    }

    @Operation(summary = "知识库列表")
    @GetMapping("/list")
    @ApiOperationSupport(order = 6)
    public R<List<AiKnowledgeListVO>> list(AiKnowledgeListParam param) {
        return success(aiKnowledgeService.selectAiKnowledgeList(param));
    }

    @Operation(summary = "知识库分页列表")
    @GetMapping("/list-page")
    @ApiOperationSupport(order = 7)
    public R<IPage<AiKnowledgeListVO>> listPage(Query query, AiKnowledgeListParam param) {
        return success(aiKnowledgeService.selectAiKnowledgeListPage(PageUtil.getPage(query), param));
    }

    @Operation(summary = "测试向量数据库")
    @GetMapping("/test-vector-store")
    @ApiOperationSupport(order = 8)
    public R testVectorDb(@Parameter(description = "模型 id", required = true, name = "modelId", in = ParameterIn.QUERY) Long modelId,
                          @Parameter(description = "向量数据库 id", required = true, name = "vectorDbId", in = ParameterIn.QUERY) Long vectorDbId) {
        VectorStore vectorStore = aiKnowledgeService.getVectorStore(modelId, vectorDbId);
        List<Document> test = vectorStore.similaritySearch("test");
        return success(test);
    }
}
