package org.bedrock.websocket.handle.impl;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import org.bedrock.common.auth.constant.TokenConstant;
import org.bedrock.common.auth.entity.AuthUser;
import org.bedrock.common.auth.util.AuthUtil;
import org.bedrock.common.code.util.CollectionUtil;
import org.bedrock.common.code.util.StringUtil;
import org.bedrock.system.feign.IAdminOnlineClient;
import org.bedrock.system.param.AdminOnlineSubmitParam;
import org.bedrock.websocket.constant.WebSocketConstant;
import org.bedrock.websocket.endpoint.WebSocketServer;
import org.bedrock.websocket.handle.IModelOperateHandle;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 在线状态
 */
@Component
@RequiredArgsConstructor
public class AdminOnlineModelOperateHandle implements IModelOperateHandle {

    private final IAdminOnlineClient adminOnlineClient;

    @Override
    public void onOpen(WebSocketServer wss) {
        adminOnlineClient.submit(getParam(wss));
    }

    @Override
    public void onPing(WebSocketServer wss, JsonNode data) {
        adminOnlineClient.heartbeat(getParam(wss));
    }

    @Override
    public void closeSocket(Long onlyId, Long userId) {
        AdminOnlineSubmitParam param = new AdminOnlineSubmitParam();
        param.setAdminId(userId);
        param.setWsOnlyId(onlyId);
        adminOnlineClient.close(param);
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }

    /**
     * 获取参数
     */
    private AdminOnlineSubmitParam getParam(WebSocketServer wss) {
        AdminOnlineSubmitParam param = new AdminOnlineSubmitParam();
        param.setAdminId(wss.getUserId());
        param.setWsOnlyId(wss.getOnlyId());

        Session session = wss.getSession();
        /**
         * 获取token
         */
        List<String> list = session.getRequestParameterMap().get(TokenConstant.AUTH_TOKEN);
        if (CollectionUtil.isNotEmpty(list)) {
            param.setToken(list.get(0));
        }
        /**
         * 获取客户端IP
         */
        Map<String, Object> userProperties = session.getUserProperties();
        if (userProperties != null) {
            param.setClientIp(StringUtil.toStr(userProperties.get(WebSocketConstant.CLIENT_IP), "127.0.0.1"));
            Object tokenid = userProperties.get("TOKENID");
            if (tokenid != null) {
                param.setTokenId(tokenid.toString());
            } else {
                String token = AuthUtil.getToken(param.getToken());
                AuthUser authUser = AuthUtil.getAuthUser(token);
                param.setTokenId(authUser.getTokenId());
                userProperties.put("TOKENID", authUser.getTokenId());
            }
        }
        return param;
    }
}
