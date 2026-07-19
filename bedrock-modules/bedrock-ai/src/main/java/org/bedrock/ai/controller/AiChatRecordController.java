package org.bedrock.ai.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bedrock.ai.enums.AiChatTypeEnum;
import org.bedrock.ai.param.send.AiArticleParam;
import org.bedrock.ai.param.AiChatRecordAdminListParam;
import org.bedrock.ai.param.AiChatRecordEditParam;
import org.bedrock.ai.param.send.AiChatSendParam;
import org.bedrock.ai.param.send.AiImageParam;
import org.bedrock.ai.param.send.AiMindmapParam;
import org.bedrock.ai.service.IAiChatRecordService;
import org.bedrock.ai.service.IAiChatService;
import org.bedrock.ai.vo.*;
import org.bedrock.common.boot.base.BaseController;
import org.bedrock.common.code.api.R;
import org.bedrock.common.mybatisplus.base.Query;
import org.bedrock.common.mybatisplus.util.PageUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@Tag(name = "AI聊天记录 控制器")
@RestController
@RequestMapping("/ai-chat-record")
@RequiredArgsConstructor
public class AiChatRecordController extends BaseController {

    private final IAiChatRecordService aiChatRecordService;

    private final IAiChatService aiChatService;

    @Operation(summary = "会话列表（当前用户）")
    @GetMapping("/list")
    @ApiOperationSupport(order = 1)
    public R<List<AiChatRecordListVO>> list(
            @Parameter(description = "会话类型：CHAT / IMAGE / MINDMAP / ARTICLE，默认 CHAT", name = "chatType",
                    in = ParameterIn.QUERY) AiChatTypeEnum chatType) {
        return success(aiChatRecordService.selectChatRecordList(chatType));
    }

    @Operation(summary = "会话分页列表（管理端，租户内全员）")
    @GetMapping("/admin/list-page")
    @ApiOperationSupport(order = 2)
    public R<IPage<AiChatRecordAdminListVO>> adminListPage(Query query, AiChatRecordAdminListParam param) {
        return success(aiChatRecordService.selectAdminChatRecordListPage(PageUtil.getPage(query), param));
    }

    @Operation(summary = "会话详情（管理端）")
    @GetMapping("/admin/detail")
    @ApiOperationSupport(order = 3)
    public R<AiChatRecordAdminDetailVO> adminDetail(
            @Parameter(description = "会话 id", required = true, name = "id", in = ParameterIn.QUERY) Long id) {
        return success(aiChatRecordService.adminDetail(id));
    }

    @Operation(summary = "会话详情")
    @GetMapping("/detail")
    @ApiOperationSupport(order = 4)
    public R<AiChatRecordDetailVO> detail(
            @Parameter(description = "会话 id", required = true, name = "id", in = ParameterIn.QUERY) Long id) {
        return success(aiChatRecordService.detail(id));
    }

    @Operation(summary = "编辑会话")
    @PutMapping("/edit")
    @ApiOperationSupport(order = 5)
    public R<Void> edit(@RequestBody AiChatRecordEditParam param) {
        return status(aiChatRecordService.edit(param));
    }

    @Operation(summary = "删除会话")
    @DeleteMapping("/remove")
    @ApiOperationSupport(order = 6)
    public R<Void> remove(
            @Parameter(description = "会话 id", required = true, name = "id", in = ParameterIn.QUERY) Long id) {
        return status(aiChatRecordService.removeChatRecord(id));
    }

    @Operation(summary = "置顶/取消置顶")
    @PutMapping("/top")
    @ApiOperationSupport(order = 7)
    public R<Void> top(
            @Parameter(description = "会话 id", required = true, name = "id", in = ParameterIn.QUERY) Long id,
            @Parameter(description = "是否置顶：0否 1是", required = true, name = "isTop",
                    in = ParameterIn.QUERY) Integer isTop) {
        return status(aiChatRecordService.top(id, isTop));
    }

    @Operation(summary = "基于角色创建会话")
    @PostMapping("/create-from-role")
    @ApiOperationSupport(order = 8)
    public R<AiChatRecordDetailVO> createFromRole(
            @Parameter(description = "角色 id", required = true, name = "roleId", in = ParameterIn.QUERY) Long roleId) {
        return success(aiChatRecordService.createFromRole(roleId));
    }

    @Operation(summary = "聊天")
    @PostMapping("/send-char")
    @ApiOperationSupport(order = 9)
    public R<AiChatSendVO> sendChar(@RequestBody AiChatSendParam param) {
        return success(aiChatService.sendChar(param));
    }

    @Operation(summary = "流式聊天")
    @PostMapping(value = "/send-char-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ApiOperationSupport(order = 10)
    public Flux<R<AiChatSendVO>> sendCharStream(@RequestBody AiChatSendParam param) {
        return aiChatService.sendCharStream(param);
    }

    @Operation(summary = "图片生成")
    @PostMapping("/generate-image")
    @ApiOperationSupport(order = 11)
    public R<AiImageResultVO> generateImage(@RequestBody AiImageParam param) {
        return success(aiChatService.generateImage(param));
    }

    @Operation(summary = "思维导图生成（流式）")
    @PostMapping(value = "/generate-mindmap-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ApiOperationSupport(order = 12)
    public Flux<R<AiChatSendVO>> generateMindmapStream(@RequestBody AiMindmapParam param) {
        return aiChatService.generateMindmapStream(param);
    }

    @Operation(summary = "文章写作（流式）")
    @PostMapping(value = "/generate-article-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ApiOperationSupport(order = 13)
    public Flux<R<AiChatSendVO>> generateArticleStream(@RequestBody AiArticleParam param) {
        return aiChatService.generateArticleStream(param);
    }
}
