package org.bedrock.websocket.endpoint;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bedrock.websocket.dto.ResultDto;
import org.bedrock.websocket.manage.WebSocketManage;
import org.bedrock.websocket.support.ResultDtoDecoder;
import org.bedrock.websocket.support.ServerEndpointConfigConfigurator;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Getter
@Component
@ServerEndpoint(value = "/web-socket/{userId}", decoders = {ResultDtoDecoder.class}, configurator = ServerEndpointConfigConfigurator.class)
public class WebSocketServer {

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 接收userId
     */
    private Long userId = 0L;

    /**
     * 一个用户可以建立多个连接
     * 次字段代表唯一标识
     */
    private Long onlyId;

    /**
     * 订阅的信息，实际发生订阅的时候进行初始化
     */
    @Getter
    private final Set<String> subscribeSet = new CopyOnWriteArraySet<>();

    /**
     * 连接是否关闭
     */
    private final AtomicBoolean closed = new AtomicBoolean(false);

    /**
     * 连接闲置超时 60秒
     */
    private static final long SESSION_IDLE_TIMEOUT = 60 * 1000;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Long userId) {
        this.session = session;
        this.userId = userId;
        this.onlyId = IdWorker.getId();
        /**
         * 设置连接信息
         */
        session.setMaxIdleTimeout(SESSION_IDLE_TIMEOUT);
        WebSocketManage.getWebSocketManage().onOpen(userId, this);

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(CloseReason closeReason) {
        if (!closed.compareAndSet(false, true)) {
            return;
        }
        log.info("连接断开断开原因--->{}", closeReason);
        /**
         * 处理关闭连接核心逻辑
         */
        WebSocketManage.getWebSocketManage().onClose(userId, onlyId, this);
        this.userId = null;
        this.session = null;
        this.onlyId = null;
        this.subscribeSet.clear();

    }

    /**
     * 收到客户端消息后调用的方法
     *
     */
    @OnMessage
    public void onMessage(ResultDto resultDto) {
        /**
         * 调用消息处理
         */
        WebSocketManage.getWebSocketManage().onMessage(resultDto, this);
    }

    /**
     * @param error
     */
    @OnError
    public void onError(Throwable error) {
        if (!closed.compareAndSet(false, true)) {
            return;
        }
        log.error("用户错误:{},原因:{}", this.userId, error.getMessage(), error);
        /**
         * 处理关闭连接核心逻辑
         */
        WebSocketManage.getWebSocketManage().onClose(userId, onlyId, this);
        this.userId = null;
        this.session = null;
        this.onlyId = null;
        this.subscribeSet.clear();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebSocketServer that = (WebSocketServer) o;
        if (this.onlyId == null || that.onlyId == null) {
            return false;
        }
        return Objects.equals(onlyId, that.onlyId); //  onlyId 是唯一标识
    }

    @Override
    public int hashCode() {
        return onlyId != null ? Objects.hash(onlyId) : super.hashCode();
    }
}
