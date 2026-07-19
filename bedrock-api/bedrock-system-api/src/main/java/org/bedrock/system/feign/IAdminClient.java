package org.bedrock.system.feign;

import org.bedrock.common.code.api.R;
import org.bedrock.common.constant.ApplicationConstant;
import org.bedrock.system.dto.LoginInfo;
import org.bedrock.system.vo.AdminDetailVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ApplicationConstant.APPLICATION_SYSTEM_NAME)
public interface IAdminClient {

    String SELECT_ADMIN_LOGIN_INFO_BY_USERNAME = "/feign/admin/login-info/username";

    String SELECT_ADMIN_BY_ID = "/feign/admin/detail/id";

    /**
     * 根据用户名查询管理员登录信息
     *
     * @param username
     * @return
     */
    @GetMapping(SELECT_ADMIN_LOGIN_INFO_BY_USERNAME)
    R<LoginInfo> selectAdminLoginInfoByUsername(@RequestParam("username") String username,
                                                @RequestParam("tenantId") String tenantId);

    /**
     * 根据用户名查询管理员登录信息
     *
     * @param id id
     */
    @GetMapping(SELECT_ADMIN_BY_ID)
    R<AdminDetailVO> selectAdminById(@RequestParam("id") Long id);

}
