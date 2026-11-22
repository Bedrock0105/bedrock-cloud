package org.bedrock.system.controller;

import org.bedrock.common.crypto.annotation.ApiCrypto;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bedrock.common.auth.constant.RoleAliasConstant;
import org.bedrock.common.code.api.R;
import org.bedrock.common.code.util.CacheUtil;
import org.bedrock.common.code.util.StringUtil;
import org.bedrock.common.log.annotation.OperationLog;
import org.bedrock.common.log.exception.ServiceException;
import org.bedrock.common.security.annotation.PrePermissionCheck;
import org.bedrock.system.cache.CacheWhitelist;
import org.bedrock.system.enums.SystemErrorEnum;
import org.bedrock.system.vo.CacheItemVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统缓存运维清理（白名单兜底，不替代业务 {@code @CacheEvict}）。
 */
@Tag(name = "缓存管理 控制器")
@ApiCrypto
@RestController
@RequestMapping("/cache")
public class CacheController {

    @GetMapping("/list")
    @Operation(summary = "可清理缓存列表")
    @ApiOperationSupport(order = 1)
    @PrePermissionCheck("hasAnyRole('" + RoleAliasConstant.ROLE_ADMIN + "','" + RoleAliasConstant.ROLE_ADMINISTRATOR + "')")
    public R<List<CacheItemVO>> list() {
        return R.success(CacheWhitelist.list());
    }

    @PostMapping("/clear")
    @Operation(summary = "按缓存名称清理（仅白名单）")
    @ApiOperationSupport(order = 2)
    @PrePermissionCheck("hasAnyRole('" + RoleAliasConstant.ROLE_ADMIN + "','" + RoleAliasConstant.ROLE_ADMINISTRATOR + "')")
    @OperationLog(type = "缓存管理", subType = "清理缓存", success = "清理缓存：{{#cacheNames}}", extra = "{TO_JSON{#cacheNames}}", condition = "{{#_errorMsg == null}}")
    public R<Void> clear(@RequestBody List<String> cacheNames) {
        for (String cacheName : cacheNames) {
            if (StringUtil.isBlank(cacheName) || !CacheWhitelist.contains(cacheName)) {
                throw new ServiceException(SystemErrorEnum.CACHE_NAME_NOT_ALLOWED);
            }
            clearIfPresent(cacheName);
        }
        return R.success();
    }

    @PostMapping("/clear-all")
    @Operation(summary = "清空白名单内全部缓存")
    @ApiOperationSupport(order = 3)
    @PrePermissionCheck("hasAnyRole('" + RoleAliasConstant.ROLE_ADMIN + "','" + RoleAliasConstant.ROLE_ADMINISTRATOR + "')")
    @OperationLog(type = "缓存管理", subType = "清空全部缓存", success = "清空白名单内全部缓存", condition = "{{#_errorMsg == null}}")
    public R<Void> clearAll() {
        for (String cacheName : CacheWhitelist.names()) {
            clearIfPresent(cacheName);
        }
        return R.status(true);
    }

    private static void clearIfPresent(String cacheName) {
        if (CacheUtil.exists(cacheName)) {
            CacheUtil.clear(cacheName);
        }
    }
}
