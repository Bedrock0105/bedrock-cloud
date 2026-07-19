package org.bedrock.system.feign;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.bedrock.common.code.api.R;
import org.bedrock.system.dto.LoginInfo;
import org.bedrock.system.service.IAdminService;
import org.bedrock.system.vo.AdminDetailVO;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequiredArgsConstructor
public class AdminClient implements IAdminClient {

    private final IAdminService adminService;

    @Override
    public R<LoginInfo> selectAdminLoginInfoByUsername(String username, String tenantId) {
        return R.success(adminService.loginInfo(username, tenantId));
    }

    @Override
    public R<AdminDetailVO> selectAdminById(Long id) {
        return R.success(adminService.detail(id));
    }
}
