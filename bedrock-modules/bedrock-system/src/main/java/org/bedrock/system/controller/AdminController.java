package org.bedrock.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bedrock.common.auth.constant.RoleAliasConstant;
import org.bedrock.common.auth.util.AuthUtil;
import org.bedrock.common.boot.base.BaseController;
import org.bedrock.common.code.api.R;
import org.bedrock.common.code.util.NumberUtil;
import org.bedrock.common.excel.annotation.RequestExcel;
import org.bedrock.common.excel.annotation.ResponseExcel;
import org.bedrock.common.log.annotation.OperationLog;
import org.bedrock.common.mybatisplus.base.Query;
import org.bedrock.common.mybatisplus.util.PageUtil;
import org.bedrock.common.security.annotation.PrePermissionCheck;
import org.bedrock.system.excel.AdminExportExcel;
import org.bedrock.system.excel.AdminImportExcel;
import org.bedrock.system.param.AdminPageParam;
import org.bedrock.system.param.AdminProfileUpdateParam;
import org.bedrock.system.param.AdminSubmitParam;
import org.bedrock.system.service.IAdminService;
import org.bedrock.system.vo.AdminDetailVO;
import org.bedrock.system.vo.AdminListVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "管理员 控制器")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController extends BaseController {

    private final IAdminService adminService;

    @GetMapping("/info")
    @Operation(summary = "当前登录人信息")
    @ApiOperationSupport(order = -1)
    public R<AdminDetailVO> info() {
        return R.success(adminService.detail(AuthUtil.getUserId()));
    }

    @PostMapping("/submit")
    @Operation(summary = "添加管理员")
    @ApiOperationSupport(order = 1)
    @OperationLog(type = "管理员管理", subType = "添加管理员", success = "新增管理员 username: {{#param.username}}", extra = "{TO_JSON{#param}}", condition = "{{#_errorMsg == null}}")
    public R<Void> submit(@RequestBody AdminSubmitParam param) {
        return R.status(adminService.submit(param));
    }

    @PutMapping("/edit")
    @Operation(summary = "修改管理员")
    @ApiOperationSupport(order = 2)
    @OperationLog(type = "管理员管理", subType = "修改管理员", success = "修改管理员 username: {{#param.username}}", extra = "{TO_JSON{#param}}", condition = "{{#_errorMsg == null}}")
    public R<Void> edit(@RequestBody AdminSubmitParam param) {
        return R.status(adminService.edit(param));
    }

    @GetMapping("/detail")
    @Operation(summary = "管理员详情")
    @ApiOperationSupport(order = 3)
    public R<AdminDetailVO> detail(Long id) {
        return R.success(adminService.detail(id));
    }

    @GetMapping("/page")
    @Operation(summary = "管理员 分页查询")
    @ApiOperationSupport(order = 4)
    public R<IPage<AdminListVO>> page(Query query, AdminPageParam param) {
        return R.success(adminService.pageAdmin(PageUtil.getPage(query), param));
    }

    @GetMapping("/list")
    @Operation(summary = "管理员 无分页")
    @ApiOperationSupport(order = 5)
    public R<List<AdminListVO>> list(AdminPageParam param) {
        return R.success(adminService.listAdmin(param));
    }

    @PutMapping("/enable-status")
    @Operation(summary = "管理员 修改密码")
    @ApiOperationSupport(order = 6)
    @OperationLog(type = "管理员管理", subType = "管理员状态", success = "管理员【{{#username}}】状态修改为【{{#status == 1 ? '启用':'禁用'}}】", condition = "{{#_errorMsg == null}}")
    public R<Void> enableStatus(@Parameter(description = "管理员ID") Long id,
                                @Parameter(description = "账号状态（1正常 0停用）") Integer status) {
        return R.status(adminService.enableStatus(id, status));
    }

    @PutMapping("/update-password")
    @Operation(summary = "管理员 修改密码")
    @ApiOperationSupport(order = 6)
    @OperationLog(type = "管理员管理", subType = "修改管理员密码", success = "修改管理员密码 username: {{#username}}", extra = "{{#id}}", condition = "{{#_errorMsg == null}}")
    public R<Void> updatePassword(@Parameter(description = "管理员ID", required = false) Long id,
                                  @Parameter(description = "旧密码") String oldPassword,
                                  @Parameter(description = "新密码") String newPassword) {
        return R.status(adminService.updatePassword(id == null ? AuthUtil.getUserId() : id, oldPassword, newPassword));
    }

    @PutMapping("/update-profile")
    @Operation(summary = "修改个人信息")
    @ApiOperationSupport(order = 6)
    @OperationLog(type = "管理员管理", subType = "修改个人信息", success = "修改个人信息 username: {{#username}}", condition = "{{#_errorMsg == null}}")
    public R<Void> updateProfile(@RequestBody AdminProfileUpdateParam param) {
        return R.status(adminService.updateProfile(param));
    }

    @PutMapping("/reset-password")
    @Operation(summary = "管理员 重置密码")
    @ApiOperationSupport(order = 7)
    @OperationLog(type = "管理员管理", subType = "重置管理员密码", success = "重置管理员密码 ids: {{#ids}}", extra = "{{#ids}}", condition = "{{#_errorMsg == null}}")
    @PrePermissionCheck("hasAnyRole('" + RoleAliasConstant.ROLE_ADMIN + "','" + RoleAliasConstant.ROLE_ADMINISTRATOR + "')")
    public R<Void> updatePassword(@Parameter(description = "管理员ID 多个逗号分隔") String ids,
                                  @Parameter(description = "新密码") String newPassword) {
        return R.status(adminService.resetPassword(NumberUtil.toListLong(ids), newPassword));
    }

    @DeleteMapping("/remove")
    @Operation(summary = "管理员 删除")
    @OperationLog(type = "管理员管理", subType = "删除管理员", success = "删除管理员 username: {{#username}}", extra = "{{#id}}", condition = "{{#_errorMsg == null}}")
    @ApiOperationSupport(order = 8)
    public R<Void> remove(Long id) {
        return R.status(adminService.removeById(id));
    }

    @PostMapping("/import-excel")
    @Operation(summary = "管理员 导入")
    @ApiOperationSupport(order = 9)
    @OperationLog(type = "管理员管理", subType = "导入管理员", success = "操作了导入管理员功能", condition = "{{#_errorMsg == null}}")
    @Parameters({
            @Parameter(description = "excel", required = true, name = "file", in = ParameterIn.QUERY, schema = @Schema(type = "string", format = "binary")),
    })
    public R<Void> importExcel(@Parameter(hidden = true) @RequestExcel(value = "file", head = AdminImportExcel.class, headRowNumber = 2) List<AdminImportExcel> list) {
        return R.status(adminService.importExcel(list));
    }

    @Operation(summary = "管理员 导入模版下载")
    @ApiOperationSupport(order = 10)
    @GetMapping("/download-excel")
    @ResponseExcel(fileName = "用户导入模版", sheets = @ResponseExcel.Sheet(name = "管理员列表", head = AdminImportExcel.class), inMemory = true)
    public R<List<AdminImportExcel>> downloadExcel() {
        return R.success(List.of());
    }

    @Operation(summary = "管理员 导出数据下载")
    @ApiOperationSupport(order = 11)
    @GetMapping("/export-excel")
    @ResponseExcel(fileName = "管理员数据", sheets = @ResponseExcel.Sheet(name = "管理员列表", head = AdminExportExcel.class))
    public R<List<AdminExportExcel>> exportExcel(AdminPageParam param) {
        return R.success(adminService.exportExcel(param));
    }
}
