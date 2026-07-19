package org.bedrock.websocket.manage;

import jakarta.websocket.CloseReason;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.bedrock.common.code.util.*;
import org.bedrock.websocket.dto.ResultDto;
import org.bedrock.websocket.dto.SendDto;
import org.bedrock.websocket.endpoint.WebSocketServer;
import org.bedrock.websocket.enums.OperateTypeEnum;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
public class WebSocketManage {

    /**
     * 信息处理类
     */
    private final ModelOperateManage modelOperateManage;

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static final ConcurrentHashMap<Long, ConcurrentLinkedQueue<WebSocketServer>> webSocketMap = new ConcurrentHashMap<>();

    /**
     * 发送消息
     */
    @Async("webSocketExecutor")
    public void sendMessage(SendDto sendDto) {
        Collection<Long> userIdCollection = sendDto.getUserIdCollection();
        /**
         * 设置个别属性为空，不进行序列化
         */
        sendDto.setParameters(null);
        sendDto.setUserIdCollection(null);
        String message = JsonUtil.toJson(sendDto);
        if (CollectionUtil.isEmpty(userIdCollection)) {
            /**
             * 循环发送消息全部人员
             */
//            webSocketMap.forEach((key, value) -> value.forEach(ws -> sendMessage(ws, sendDto, message)));
            webSocketMap.entrySet()
                    .parallelStream()
                    .forEach(entry -> entry
                            .getValue()
                            .forEach(ws -> sendMessage(ws, sendDto, message)));
            return;
        }
        /**
         * 循环发送消息指定人员
         */
        userIdCollection.forEach(userId -> {
            ConcurrentLinkedQueue<WebSocketServer> webSocketServers = webSocketMap.get(userId);
            if (CollectionUtil.isEmpty(webSocketServers)) {
                return;
            }
            webSocketServers.forEach(ws -> sendMessage(ws, sendDto, message));
        });
    }

    /**
     * 发送消息
     */
    private void sendMessage(WebSocketServer ws, SendDto sendDto, String message) {
        /**
         * 判断是否订阅消息
         */
        if (StringUtil.isNotBlank(sendDto.getMessageType())) {
            Set<String> subscribeSet = ws.getSubscribeSet();
            if (!subscribeSet.contains(sendDto.getMessageType())) {
                return;
            }
        }
        Session session = ws.getSession();
        if (session == null) {
            return;
        }
        /**
         * 发送消息
         */
        session.getAsyncRemote().sendText(message, result -> {
            if (result.isOK()) {
                return;
            }
            Throwable e = result.getException();
            modelOperateManage.sendMessageError(ws, e, sendDto);
            log.error("发送消息失败-->{}", e.getMessage(), e);
        });

    }

    /**
     * 收到消息 调用处理消息方法
     */
    public void onMessage(ResultDto resultDto,
                          WebSocketServer ws) {
        log.info("用户消息: {},报文: {}", ws.getUserId(), resultDto);
        if (ObjectUtil.isEmpty(resultDto) || ObjectUtil.isEmpty(resultDto.getModel())) {
            return;
        }
        /**
         * 如果是空或者操作是ping
         * 则不进行处理
         */
        if (resultDto.getOperateType() == null ||
                resultDto.getOperateType() == OperateTypeEnum.PING) {
            modelOperateManage.onPing(resultDto, ws);
            return;
        }
        /**
         * 开始处理收到的消息
         */
        modelOperateManage.receivedMessage(resultDto, ws);
    }

    /**
     * 处理打开连接
     */
    public void onOpen(Long userId, WebSocketServer ws) {
        /**
         * 判断当前用户是否加入过连接
         */
        webSocketMap.compute(userId, (key, value) -> {
            if (value == null) {
                value = new ConcurrentLinkedQueue<>();
            }
            value.add(ws);
            return value;
        });
        modelOperateManage.onOpen(ws);
        log.info("用户连接:{}/{}", userId, ws.getOnlyId());
    }

    /**
     * 处理关闭连接
     */
    public void onClose(Long userId, Long onlyId, WebSocketServer ws) {
        if (userId == null || onlyId == null) {
            return;
        }
        webSocketMap.computeIfPresent(userId, (key, value) -> {
            if (CollectionUtil.isEmpty(value)) {
                return null;
            }
            value.remove(ws);
            if (value.isEmpty()){
                return null;
            }
            return value;
        });
        /**
         * 关闭session
         */
        Session session = ws.getSession();
        if (session != null && session.isOpen()) {
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "主动关闭"));
            } catch (IOException e) {
                log.error("关闭 Session 失败，userId:{}", userId, e);
            }
        }
        log.info("用户退出:{}", userId);
        modelOperateManage.closeWebSocket(onlyId, userId);
    }

    /**
     * 清空空值
     */
    public void cleanEmptyEntries() {
        List<WebSocketServer> webSocketServers = new ArrayList<>();
        /**
         * 删除空值和无效的连接
         */
        webSocketMap.entrySet().removeIf(entry -> {
            boolean empty = entry.getValue().isEmpty();
            if (empty) {
                return true;
            }
            entry.getValue().forEach(ws -> {
                Session session = ws.getSession();
                if (session == null || !session.isOpen()) {
                    log.warn("用户{}的连接已关闭或 Session 为空，跳过发送", ws.getUserId());
                    webSocketServers.add(ws);
                }
            });
            return false;
        });
        webSocketServers.forEach(ws -> onClose(ws.getUserId(), ws.getOnlyId(), ws));
    }

    /**
     * 单例对象
     */
    private static final class WebSocketManageHolder {

        private static final WebSocketManage webSocketManage = SpringUtil.getBean(WebSocketManage.class);
    }

    /**
     * 自己本身
     * 可以用get方法获取本对象，避免循环依赖
     */
    public static WebSocketManage getWebSocketManage() {
        return WebSocketManageHolder.webSocketManage;
    }

    public WebSocketManage(ModelOperateManage modelOperateManage) {
        this.modelOperateManage = modelOperateManage;
    }
}
