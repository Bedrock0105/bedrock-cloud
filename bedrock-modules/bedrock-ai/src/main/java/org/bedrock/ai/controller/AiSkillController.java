package org.bedrock.ai.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bedrock.ai.param.AiSkillListParam;
import org.bedrock.ai.param.AiSkillSubmitParam;
import org.bedrock.ai.service.IAiSkillService;
import org.bedrock.ai.vo.AiSkillDetailVO;
import org.bedrock.ai.vo.AiSkillListVO;
import org.bedrock.common.auth.constant.RoleAliasConstant;
import org.bedrock.common.boot.base.BaseController;
import org.bedrock.common.code.api.R;
import org.bedrock.common.crypto.annotation.ApiCrypto;
import org.bedrock.common.log.annotation.OperationLog;
import org.bedrock.common.mybatisplus.base.Query;
import org.bedrock.common.mybatisplus.util.PageUtil;
import org.bedrock.common.security.annotation.PrePermissionCheck;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * AI Skill 管理接口。
 * <p>
 * 解析类接口仅预览不落库；新增默认禁用，启用时同步本地目录并注册到 SkillManager；
 * 下载为流式 zip 响应。
 * </p>
 */
@Tag(name = "AI Skill 控制器")
@ApiCrypto
@RestController
@RequestMapping("/ai-skill")
@RequiredArgsConstructor
public class AiSkillController extends BaseController {

    private final IAiSkillService aiSkillService;

    @Operation(summary = "解析上传文件为 Skill 文件节点",
            description = "不落库。文本后缀写入 content，其它上传 OSS 写 fileUrl。")
    @PostMapping("/parse-file")
    @ApiOperationSupport(order = 1)
    public R<AiSkillSubmitParam.AiSkillFileItem> parseFile(
            @Parameter(description = "待解析文件（文本写 content，其它上传 OSS 写 fileUrl）", required = true)
            @RequestPart("file") MultipartFile file) throws IOException {
        return success(aiSkillService.parseFile(file));
    }

    @Operation(summary = "解析 Skill 压缩包为详情预览（含文件树）",
            description = "不落库。解析 zip 为文件树，并从 SKILL.md frontmatter 提取 name/description。")
    @PostMapping("/parse-zip")
    @ApiOperationSupport(order = 2)
    public R<AiSkillDetailVO> parseZip(
            @Parameter(description = "Skill zip 压缩包", required = true)
            @RequestPart("file") MultipartFile file) throws IOException {
        return success(aiSkillService.parseZip(file));
    }

    @Operation(summary = "下载 Skill 压缩包",
            description = "按详情扁平文件列表流式打包 zip（含包根目录），非 JSON 响应。")
    @GetMapping("/download-skill-zip")
    @ApiOperationSupport(order = 3)
    public void downloadSkillZip(
            @Parameter(description = "Skill id", required = true, name = "id", in = ParameterIn.QUERY) Long id) throws IOException {
        FileDownload fileDownload = fileDownload();
        aiSkillService.downloadSkillZip(id, fileDownload);
    }

    @Operation(summary = "添加 Skill",
            description = "默认禁用；name/description 由根下 SKILL.md 解析；本地目录延后到启用时同步。")
    @PostMapping("/submit")
    @ApiOperationSupport(order = 4)
    @OperationLog(type = "AI Skill管理", subType = "添加", success = "新增 Skill: {{#param.label}}",
            extra = "{TO_JSON{#param}}", condition = "{{#_errorMsg == null}}")
    public R<Void> submit(@RequestBody AiSkillSubmitParam param) {
        return status(aiSkillService.submit(param));
    }

    @Operation(summary = "修改 Skill",
            description = "name 可随 SKILL.md 变更；全量替换文件树。原启用态会同步本地目录并重新注册 SkillManager。")
    @PutMapping("/edit")
    @ApiOperationSupport(order = 5)
    @OperationLog(type = "AI Skill管理", subType = "修改", success = "修改 Skill: {{#param.label}}",
            extra = "{TO_JSON{#param}}", condition = "{{#_errorMsg == null}}")
    public R<Void> edit(@RequestBody AiSkillSubmitParam param) {
        return status(aiSkillService.edit(param));
    }

    @Operation(summary = "删除 Skill",
            description = "逻辑删除主表，并清理文件树、本地目录与 SkillManager 注册。")
    @DeleteMapping("/remove")
    @ApiOperationSupport(order = 6)
    @OperationLog(type = "AI Skill管理", subType = "删除", success = "删除 Skill id: {{#name}}",
            extra = "{{#id}}", condition = "{{#_errorMsg == null}}")
    @PrePermissionCheck("hasAnyRole('" + RoleAliasConstant.ROLE_ADMIN + "','"
            + RoleAliasConstant.ROLE_ADMINISTRATOR + "')")
    public R<Void> remove(
            @Parameter(description = "Skill id", required = true, name = "id", in = ParameterIn.QUERY) Long id) {
        return status(aiSkillService.removeById(id));
    }

    @Operation(summary = "Skill 详情",
            description = "返回主表信息与组装后的文件树。")
    @GetMapping("/detail")
    @ApiOperationSupport(order = 7)
    public R<AiSkillDetailVO> detail(
            @Parameter(description = "Skill id", required = true, name = "id", in = ParameterIn.QUERY) Long id) {
        return success(aiSkillService.detail(id));
    }

    @Operation(summary = "启用禁用 Skill",
            description = "启用：先同步本地目录再 register；禁用：unregister。")
    @PutMapping("/enable-status")
    @ApiOperationSupport(order = 8)
    @OperationLog(type = "AI Skill管理", subType = "启用禁用",
            success = "Skill 【{{#name}}】状态修改为【{{#status == 1 ? '启用':'禁用'}}】",
            condition = "{{#_errorMsg == null}}")
    public R<Void> enableStatus(
            @Parameter(description = "Skill id", required = true, name = "id", in = ParameterIn.QUERY) Long id,
            @Parameter(description = "配置状态（1=启用，0=禁用）", required = true, name = "status",
                    in = ParameterIn.QUERY) Integer status) {
        return status(aiSkillService.enableStatus(id, status));
    }

    @Operation(summary = "Skill 列表")
    @GetMapping("/list")
    @ApiOperationSupport(order = 9)
    public R<List<AiSkillListVO>> list(AiSkillListParam param) {
        return success(aiSkillService.selectAiSkillList(param));
    }

    @Operation(summary = "Skill 分页列表")
    @GetMapping("/list-page")
    @ApiOperationSupport(order = 10)
    public R<IPage<AiSkillListVO>> listPage(Query query, AiSkillListParam param) {
        return success(aiSkillService.selectAiSkillListPage(PageUtil.getPage(query), param));
    }

}
