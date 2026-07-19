package org.bedrock.system.feign;

import org.bedrock.common.code.api.R;
import org.bedrock.common.constant.ApplicationConstant;
import org.bedrock.system.entity.ParamConfig;
import org.bedrock.system.vo.DeptDetailVO;
import org.bedrock.system.vo.RoleDetailVO;
import org.bedrock.system.vo.TenantDetailVO;
import org.bedrock.system.vo.TenantPackageDetailVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ApplicationConstant.APPLICATION_SYSTEM_NAME)
public interface ISystemClient {

    String SELECT_DEPT_DETAIL_BY_ID = "/feign/dept/detail/id";
    String SELECT_ROLE_DETAIL_BY_ID = "/feign/role/detail/id";
    String SELECT_TENANT_PACKAGE_DETAIL_BY_ID = "/feign/tenant/package/detail/id";
    String SELECT_TENANT_DETAIL_BY_ID = "/feign/tenant/detail/id";
    String SELECT_TENANT_DETAIL_BY_TENANTID = "/feign/tenant/detail/tenant";
    String SELECT_PARAM_CONFIG_DETAIL_BY_CONFIG_KEY = "/feign/param-config/detail/config-key";

    /**
     * 根据部门ID查询部门详情
     */
    @GetMapping(SELECT_DEPT_DETAIL_BY_ID)
    R<DeptDetailVO> deptDetail(@RequestParam("id") Long id);

    /**
     * 根据角色ID查询角色详情
     */
    @GetMapping(SELECT_ROLE_DETAIL_BY_ID)
    R<RoleDetailVO> roleDetail(@RequestParam("id") Long id);

    /**
     * 根据id查询产品包详情
     */
    @GetMapping(SELECT_TENANT_PACKAGE_DETAIL_BY_ID)
    R<TenantPackageDetailVO> tenantPackageDetail(@RequestParam("id") Long id);

    /**
     * 根据租户id查询租户详情
     */
    @GetMapping(SELECT_TENANT_DETAIL_BY_ID)
    R<TenantDetailVO> tenantDetail(@RequestParam("id") Long id);

    /**
     * 根据租户id查询租户详情
     */
    @GetMapping(SELECT_TENANT_DETAIL_BY_TENANTID)
    R<TenantDetailVO> tenantDetail(@RequestParam("tenantId") String tenantId);

    /**
     * 根据参数配置键查询参数配置详情
     */
    @GetMapping(SELECT_PARAM_CONFIG_DETAIL_BY_CONFIG_KEY)
    R<ParamConfig> paramConfigDetail(@RequestParam("configKey") String configKey);
}
