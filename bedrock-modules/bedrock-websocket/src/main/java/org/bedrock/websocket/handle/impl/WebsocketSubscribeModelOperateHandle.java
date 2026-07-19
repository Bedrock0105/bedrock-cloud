package org.bedrock.websocket.handle.impl;

import org.bedrock.websocket.endpoint.WebSocketServer;
import org.bedrock.websocket.enums.ModelEnum;
import org.bedrock.websocket.handle.IModelOperateHandle;
import org.springframework.stereotype.Component;

/**
 * 订阅消息处理类
 */
@Component
public class WebsocketSubscribeModelOperateHandle implements IModelOperateHandle {

    @Override
    public void subscribe(WebSocketServer wss, String msg) {
        wss.getSubscribeSet().add(msg);
    }

    @Override
    public void unsubscribe(WebSocketServer wss, String msg) {
        wss.getSubscribeSet().remove(msg);
    }

    @Override
    public boolean isModel(ModelEnum modelEnum) {
        return true;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
