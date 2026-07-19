package org.bedrock.websocket.listener;

import lombok.RequiredArgsConstructor;
import org.bedrock.common.code.util.CollectionUtil;
import org.bedrock.common.redis.stream.annotation.RSMessageListener;
import org.bedrock.websocket.constant.WebSocketConstant;
import org.bedrock.websocket.dto.SendDto;
import org.bedrock.websocket.manage.ModelOperateManage;
import org.bedrock.websocket.manage.WebSocketManage;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 *
 */
@Component
@RequiredArgsConstructor
public class RedisStreamListener {

    private final WebSocketManage webSocketManage;

    private final ModelOperateManage modelOperateManage;

    /**
     * 监听发送全部的消息
     */
    @RSMessageListener(topic = WebSocketConstant.SENDALL)
    public void sendAll(ObjectRecord<String, SendDto> objectRecord) {
        webSocketManage.sendMessage(objectRecord.getValue());
    }

    /**
     * 监听发送指定人员的消息
     */
    @RSMessageListener(topic = WebSocketConstant.SEND_BY_USER_ID)
    public void sendByUserId(ObjectRecord<String, SendDto> objectRecord) {
        if (CollectionUtil.isEmpty(objectRecord.getValue().getUserIdCollection())) {
            return;
        }
        webSocketManage.sendMessage(objectRecord.getValue());
    }

    /**
     * 监听发送规则计算出人员的消息
     */
    @RSMessageListener(topic = WebSocketConstant.SEND_BY_PARAMETER)
    public void sendByParameter(ObjectRecord<String, SendDto> objectRecord) {
        SendDto value = objectRecord.getValue();
        Collection<Long> userIds = modelOperateManage.processParameters(value.getModel(), value.getParameters());
        if (CollectionUtil.isEmpty(userIds)) {
            return;
        }
        value.setUserIdCollection(userIds);
        webSocketManage.sendMessage(value);
    }
}
