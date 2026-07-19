package org.bedrock.system.feign;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.bedrock.common.code.api.R;
import org.bedrock.common.log.annotation.ExcludeRequestLog;
import org.bedrock.system.param.AdminOnlineSubmitParam;
import org.bedrock.system.service.IAdminOnlineService;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequiredArgsConstructor
public class AdminOnlineClient implements IAdminOnlineClient {

    private final IAdminOnlineService adminOnlineService;

    @Override
    public R<Void> submit(AdminOnlineSubmitParam param) {
        adminOnlineService.submit(param);
        return R.success();
    }

    @Override
    @ExcludeRequestLog
    public R<Void> heartbeat(AdminOnlineSubmitParam param) {
        adminOnlineService.heartbeat(param);
        return R.success();
    }

    @Override
    public R<Void> close(AdminOnlineSubmitParam param) {
        adminOnlineService.close(param);
        return R.success();
    }
}
