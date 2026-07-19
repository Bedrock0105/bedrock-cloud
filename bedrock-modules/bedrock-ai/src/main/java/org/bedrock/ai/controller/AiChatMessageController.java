package org.bedrock.ai.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bedrock.ai.service.IAiChatMessageService;
import org.bedrock.ai.vo.AiChatMessageListVO;
import org.bedrock.common.boot.base.BaseController;
import org.bedrock.common.code.api.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * AI 聊天消息明细控制器（bedrock_ai_chat_message 表）
 */
@Tag(name = "AI聊天消息 控制器")
@RestController
@RequestMapping("/ai-chat-message")
@RequiredArgsConstructor
public class AiChatMessageController extends BaseController {

    private final IAiChatMessageService aiChatMessageService;

    @Operation(summary = "会话历史消息列表")
    @GetMapping("/list")
    @ApiOperationSupport(order = 1)
    public R<List<AiChatMessageListVO>> list(
            @Parameter(description = "会话 id", required = true, name = "recordId", in = ParameterIn.QUERY) Long recordId) {
        return success(aiChatMessageService.selectMessageListByRecordId(recordId));
    }
}
