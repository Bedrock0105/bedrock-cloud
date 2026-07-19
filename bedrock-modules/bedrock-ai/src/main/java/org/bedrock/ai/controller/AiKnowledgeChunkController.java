package org.bedrock.ai.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bedrock.ai.param.AiKnowledgeChunkListParam;
import org.bedrock.ai.param.AiKnowledgeChunkSearchParam;
import org.bedrock.ai.param.AiKnowledgeChunkSubmitParam;
import org.bedrock.ai.service.IAiKnowledgeChunkService;
import org.bedrock.ai.vo.AiKnowledgeChunkDetailVO;
import org.bedrock.ai.vo.AiKnowledgeChunkListVO;
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
 * 知识库文档分片控制器
 */
@Tag(name = "知识库文档分片 控制器")
@RestController
@RequestMapping("/ai-knowledge-chunk")
@RequiredArgsConstructor
public class AiKnowledgeChunkController extends BaseController {

    private final IAiKnowledgeChunkService aiKnowledgeChunkService;

    /**
     * 手动新增分片，同步写入向量库
     */
    @Operation(summary = "添加知识库文档分片")
    @PostMapping("/submit")
    @ApiOperationSupport(order = 1)
    @OperationLog(type = "知识库分片管理", subType = "添加", success = "新增分片: 文档{{#param.docId}}-序号{{#param.chunkNo}}", extra = "{TO_JSON{#param}}", condition = "{{#_errorMsg == null}}")
    public R<Void> submit(@RequestBody AiKnowledgeChunkSubmitParam param) {
        return status(aiKnowledgeChunkService.submit(param));
    }

    /**
     * 修改分片内容，先删旧向量再写入新向量
     */
    @Operation(summary = "修改知识库文档分片")
    @PutMapping("/edit")
    @ApiOperationSupport(order = 2)
    @OperationLog(type = "知识库分片管理", subType = "修改", success = "修改分片: 文档{{#param.docId}}-序号{{#param.chunkNo}}", extra = "{TO_JSON{#param}}", condition = "{{#_errorMsg == null}}")
    public R<Void> edit(@RequestBody AiKnowledgeChunkSubmitParam param) {
        return status(aiKnowledgeChunkService.edit(param));
    }

    /**
     * 逻辑删除分片，并同步删除向量
     */
    @Operation(summary = "删除知识库文档分片")
    @DeleteMapping("/remove")
    @ApiOperationSupport(order = 3)
    @OperationLog(type = "知识库分片管理", subType = "删除", success = "删除分片 id: {{#id}}", extra = "{{#id}}", condition = "{{#_errorMsg == null}}")
    @PrePermissionCheck("hasAnyRole('" + RoleAliasConstant.ROLE_ADMIN + "','" + RoleAliasConstant.ROLE_ADMINISTRATOR + "')")
    public R<Void> remove(@Parameter(description = "分片 id", required = true, name = "id", in = ParameterIn.QUERY) Long id) {
        return status(aiKnowledgeChunkService.removeById(id));
    }

    @Operation(summary = "知识库文档分片详情")
    @GetMapping("/detail")
    @ApiOperationSupport(order = 4)
    public R<AiKnowledgeChunkDetailVO> detail(@Parameter(description = "分片 id", required = true, name = "id", in = ParameterIn.QUERY) Long id) {
        return success(aiKnowledgeChunkService.detail(id));
    }

    /**
     * 启用/禁用分片，禁用时仅移除向量
     */
    @Operation(summary = "启用禁用知识库文档分片")
    @PutMapping("/enable-status")
    @ApiOperationSupport(order = 5)
    @OperationLog(type = "知识库分片管理", subType = "启用禁用", success = "分片【序号{{#chunkNo}}】状态修改为【{{#status == 1 ? '启用':'禁用'}}】", condition = "{{#_errorMsg == null}}")
    public R<Void> enableStatus(@Parameter(description = "分片 id", required = true, name = "id", in = ParameterIn.QUERY) Long id,
                                @Parameter(description = "配置状态（1=启用，0=禁用）", required = true, name = "status", in = ParameterIn.QUERY) Integer status) {
        return status(aiKnowledgeChunkService.enableStatus(id, status));
    }

    @Operation(summary = "知识库文档分片列表")
    @GetMapping("/list")
    @ApiOperationSupport(order = 6)
    public R<List<AiKnowledgeChunkListVO>> list(AiKnowledgeChunkListParam param) {
        return success(aiKnowledgeChunkService.selectAiKnowledgeChunkList(param));
    }

    @Operation(summary = "知识库文档分片分页列表")
    @GetMapping("/list-page")
    @ApiOperationSupport(order = 7)
    public R<IPage<AiKnowledgeChunkListVO>> listPage(Query query, AiKnowledgeChunkListParam param) {
        return success(aiKnowledgeChunkService.selectAiKnowledgeChunkListPage(PageUtil.getPage(query), param));
    }

    /**
     * 召回测试 就是测试向量检索
     */
    @Operation(summary = "召回测试")
    @PostMapping("/search")
    @ApiOperationSupport(order = 8)
    public R<List<Document>> search(@RequestBody AiKnowledgeChunkSearchParam param) {
        return success(aiKnowledgeChunkService.search(param));
    }

}
