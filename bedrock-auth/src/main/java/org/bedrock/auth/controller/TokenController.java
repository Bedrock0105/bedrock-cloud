package org.bedrock.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bedrock.common.auth.entity.AuthUser;
import org.bedrock.common.auth.util.AuthUtil;
import org.bedrock.common.code.api.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "认证相关")
@Slf4j
@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class TokenController {

    @PostMapping("/logout")
    @Operation(summary = "登出接口")
    public R<Void> logOut() {
        /**
         * 可以出来退出登录的操作
         */
        AuthUser authUser = AuthUtil.getAuthUser();
        if (authUser != null) {
            log.info("登出接口:{}/{}/{}", authUser.getUsername(), authUser.getUserId(), authUser.getTokenId());
        }
        return R.success();
    }
}
