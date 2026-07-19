package org.bedrock.ai.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bedrock.common.ai.tool.ToolManager;
import org.bedrock.common.boot.base.BaseController;
import org.bedrock.common.code.api.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "AI 工具 控制器")
@RestController
@RequestMapping("/ai-tool")
@RequiredArgsConstructor
public class AiToolController extends BaseController {

    private final ToolManager toolManager;

    @Operation(summary = "工具组下拉列表")
    @GetMapping("/options")
    @ApiOperationSupport(order = 1)
    public R<List<ToolManager.ToolOption>> options() {
        return success(toolManager.listOptions());
    }

}
