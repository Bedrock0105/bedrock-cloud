package org.bedrock.websocket.constant;

public interface WebSocketConstant {

    /**
     * 发送全部消息
     */
    String SENDALL = "websocket::send:all";

    /**
     * 指定人员rediskey
     */
    String SEND_BY_USER_ID = "websocket::send:byuserid";

    /**
     * 指定规则rediskey
     */
    String SEND_BY_PARAMETER = "websocket::send:byparameter";

    /**
     * 客户端ip
     */
    String CLIENT_IP = "clientIp";
}
