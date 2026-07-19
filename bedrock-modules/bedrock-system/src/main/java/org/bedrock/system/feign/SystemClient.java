package org.bedrock.system.feign;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.bedrock.common.code.api.R;
import org.bedrock.system.entity.ParamConfig;
import org.bedrock.system.service.*;
import org.bedrock.system.vo.DeptDetailVO;
import org.bedrock.system.vo.RoleDetailVO;
import org.bedrock.system.vo.TenantDetailVO;
import org.bedrock.system.vo.TenantPackageDetailVO;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequiredArgsConstructor
public class SystemClient implements ISystemClient {

    private final IDeptService deptService;

    private final IParamConfigService paramConfigService;

    private final IRoleService roleService;

    private final ITenantService tenantService;

    private final ITenantPackageService tenantPackageService;

    @Override
    public R<DeptDetailVO> deptDetail(Long id) {
        return R.success(deptService.detail(id));
    }

    @Override
    public R<RoleDetailVO> roleDetail(Long id) {
        return R.success(roleService.detail(id));
    }

    @Override
    public R<TenantPackageDetailVO> tenantPackageDetail(Long id) {
        return R.success(tenantPackageService.detail(id));
    }

    @Override
    public R<TenantDetailVO> tenantDetail(Long id) {
        return R.success(tenantService.detail(id));
    }

    @Override
    public R<TenantDetailVO> tenantDetail(String tenantId) {
        return R.success(tenantService.info(tenantId));
    }

    @Override
    public R<ParamConfig> paramConfigDetail(String configKey) {
        return R.success(paramConfigService.detail(configKey));
    }
}
