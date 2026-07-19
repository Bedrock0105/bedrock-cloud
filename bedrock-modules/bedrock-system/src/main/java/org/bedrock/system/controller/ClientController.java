package org.bedrock.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bedrock.common.code.api.R;
import org.bedrock.common.log.annotation.OperationLog;
import org.bedrock.common.mybatisplus.base.Query;
import org.bedrock.common.mybatisplus.util.PageUtil;
import org.bedrock.system.entity.Client;
import org.bedrock.system.service.IClientService;
import org.springframework.web.bind.annotation.*;

@Tag(name = "客户端 控制器")
@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private final IClientService clientService;

    @PostMapping("/submit")
    @Operation(summary = "添加客户端")
    @ApiOperationSupport(order = 1)
    @OperationLog(type = "客户端管理", subType = "添加客户端", success = "添加客户端 clientId: {{#param.clientId}}", extra = "{TO_JSON{#param}}", condition = "{{#_errorMsg == null}}")
    public R<Void> submit(@RequestBody Client param) {
        return R.status(clientService.submit(param));
    }

    @PutMapping("/edit")
    @Operation(summary = "修改客户端")
    @ApiOperationSupport(order = 2)
    @OperationLog(type = "客户端管理", subType = "修改客户端", success = "修改客户端 clientId: {{#param.clientId}}", extra = "{TO_JSON{#param}}", condition = "{{#_errorMsg == null}}")
    public R<Void> edit(@RequestBody Client param) {
        return R.status(clientService.edit(param));
    }

    @GetMapping("/detail")
    @Operation(summary = "客户端详情")
    @ApiOperationSupport(order = 3)
    public R<Client> detail(Long id) {
        return R.success(clientService.detail(id));
    }

    @GetMapping("/page")
    @Operation(summary = "客户端 分页查询")
    @ApiOperationSupport(order = 4)
    @Parameter(name = "clientId", description = "客户端ID", in = ParameterIn.QUERY)
    public R<IPage<Client>> page(Query query, @Parameter(hidden = true) Client param) {
        return R.success(clientService.pageClient(PageUtil.getPage(query), param));
    }

    @DeleteMapping("/remove")
    @Operation(summary = "删除客户端")
    @ApiOperationSupport(order = 5)
    @OperationLog(type = "客户端管理", subType = "删除客户端", success = "删除客户端 clientId: {{#clientId}}", extra = "{{#id}}", condition = "{{#_errorMsg == null}}")
    public R<Void> edit(Long id) {
        return R.status(clientService.removeById(id));
    }
}
