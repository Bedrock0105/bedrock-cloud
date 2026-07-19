package org.bedrock.websocket.support;

import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;
import org.bedrock.common.code.util.WebUtil;
import org.bedrock.websocket.constant.WebSocketConstant;

/**
 * 添加自定义参数
 */
public class ServerEndpointConfigConfigurator extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        sec.getUserProperties().put(WebSocketConstant.CLIENT_IP, WebUtil.getIP());
    }
}
