package org.bedrock.system.cache;

import org.bedrock.common.code.constant.CacheConstant;
import org.bedrock.common.code.constant.StringPool;
import org.bedrock.common.code.util.CacheUtil;
import org.bedrock.common.code.util.NumberUtil;
import org.bedrock.common.code.util.SpringUtil;
import org.bedrock.system.entity.ParamConfig;
import org.bedrock.system.feign.IAdminClient;
import org.bedrock.system.feign.ISystemClient;
import org.bedrock.system.vo.*;

import java.util.stream.Collectors;

public final class SystemCache {

    private static ISystemClient systemClient;

    private static IAdminClient adminClient;

    /**
     * 部门详情 key
     */
    public static final String DEPT_DETAIL_ID = "dept:detail:id:";

    /**
     * 角色详情 key
     */
    public static final String ROLE_DETAIL_ID = "role:detail:id:";

    /**
     * 管理员详情 key
     */
    public static final String ADMIN_DETAIL_ID = "detail:adminid:";

    /**
     * 参数配置详情 key
     */
    public static final String PARAM_CONFIG_DETAIL = "param:config:detail:";

    /**
     * 产品包详情 key
     */
    public static final String TENANT_PACKAGE_DETAIL_ID = "tenant:package:detail:id:";

    /**
     * 租户详情 key
     */
    public static final String TENANT_DETAIL_ID = "tenant:detail:id:";

    /**
     * 租户详情 key
     */
    public static final String TENANT_DETAIL_TENANT_ID = "tenant:detail:tenantid:";

    /**
     * 获取管理员详情
     */
    public static AdminDetailVO getAdminDetail(Long adminId) {
        return CacheUtil.get(CacheConstant.ADMIN_CACHE,
                ADMIN_DETAIL_ID + adminId,
                () -> getAdminClient()
                        .selectAdminById(adminId).getData());
    }

    /**
     * 获取部门详情
     */
    public static DeptDetailVO getDeptDetail(Long deptId) {
        return CacheUtil.get(CacheConstant.SYS_CACHE,
                DEPT_DETAIL_ID + deptId,
                () -> getSystemClient()
                        .deptDetail(deptId).getData());
    }

    /**
     * 获取部门名称
     */
    public static String getDeptNameByIds(String deptIds) {
        return NumberUtil.toListLong(deptIds)
                .stream()
                .map(SystemCache::getDeptDetail)
                .map(DeptDetailVO::getDeptName)
                .collect(Collectors.joining(StringPool.COMMA));
    }

    /**
     * 获取部门详情
     */
    public static RoleDetailVO getRoleDetail(Long roleId) {
        return CacheUtil.get(CacheConstant.SYS_CACHE,
                ROLE_DETAIL_ID + roleId,
                () -> getSystemClient()
                        .roleDetail(roleId).getData());
    }

    /**
     * 获取角色名字
     */
    public static String getRoleNameByIds(String roleIds) {
        return NumberUtil.toListLong(roleIds)
                .stream()
                .map(SystemCache::getRoleDetail)
                .map(RoleDetailVO::getRoleName)
                .collect(Collectors.joining(StringPool.COMMA));
    }

    /**
     * 获取参数配置
     */
    public static ParamConfig getParam(String configKey) {
        return CacheUtil.get(CacheConstant.SYS_CACHE,
                PARAM_CONFIG_DETAIL + configKey,
                () -> getSystemClient()
                        .paramConfigDetail(configKey).getData());
    }

    /**
     * 获取参数配置
     */
    public static String getParamValue(String configKey) {
        ParamConfig param = getParam(configKey);
        if (param == null) {
            return StringPool.EMPTY;
        }
        return param.getConfigValue();
    }

    /**
     * 获取产品包详情
     */
    public static TenantPackageDetailVO getTenantPackageDetail(Long id) {
        return CacheUtil.get(CacheConstant.SYS_CACHE,
                TENANT_PACKAGE_DETAIL_ID + id,
                () -> getSystemClient()
                        .tenantPackageDetail(id).getData());
    }

    /**
     * 获取租户详情
     */
    public static TenantDetailVO getTenantDetail(Long id) {
        return CacheUtil.get(CacheConstant.SYS_CACHE,
                TENANT_DETAIL_ID + id,
                () -> getSystemClient()
                        .tenantDetail(id).getData());
    }

    /**
     * 获取租户详情
     */
    public static TenantDetailVO getTenantDetail(String tenantId) {
        return CacheUtil.get(CacheConstant.SYS_CACHE,
                TENANT_DETAIL_TENANT_ID + tenantId,
                () -> getSystemClient()
                        .tenantDetail(tenantId).getData());
    }

    private static ISystemClient getSystemClient() {
        if (systemClient == null) {
            systemClient = SpringUtil.getBean(ISystemClient.class);
        }
        return systemClient;
    }

    private static IAdminClient getAdminClient() {
        if (adminClient == null) {
            adminClient = SpringUtil.getBean(IAdminClient.class);
        }
        return adminClient;
    }
}
