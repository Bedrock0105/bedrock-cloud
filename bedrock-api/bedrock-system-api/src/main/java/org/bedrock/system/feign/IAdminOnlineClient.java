package org.bedrock.system.feign;

import org.bedrock.common.code.api.R;
import org.bedrock.common.constant.ApplicationConstant;
import org.bedrock.system.param.AdminOnlineSubmitParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = ApplicationConstant.APPLICATION_SYSTEM_NAME)
public interface IAdminOnlineClient {

    String SUBMIT_ADMIN_ONLINE = "/feign/admin-online/submit";

    String HEARTBEAT_ADMIN_ONLINE = "/feign/admin-online/heartbeat";

    String CLOSE_ADMIN_ONLINE = "/feign/admin-online/close";

    /**
     * 提交在线用户信息
     *
     * @param param
     */
    @PostMapping(SUBMIT_ADMIN_ONLINE)
    R<Void> submit(@RequestBody AdminOnlineSubmitParam param);

    /**
     * 心跳
     *
     * @param param
     */
    @PostMapping(HEARTBEAT_ADMIN_ONLINE)
    R<Void> heartbeat(@RequestBody AdminOnlineSubmitParam param);

    /**
     * 断开连接
     *
     * @param param
     */
    @PostMapping(CLOSE_ADMIN_ONLINE)
    R<Void> close(@RequestBody AdminOnlineSubmitParam param);
}
