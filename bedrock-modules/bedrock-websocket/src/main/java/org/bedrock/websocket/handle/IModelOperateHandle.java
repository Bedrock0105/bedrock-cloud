package org.bedrock.websocket.handle;

import com.fasterxml.jackson.databind.JsonNode;
import org.bedrock.websocket.dto.ResultDto;
import org.bedrock.websocket.dto.SendDto;
import org.bedrock.websocket.endpoint.WebSocketServer;
import org.bedrock.websocket.enums.ModelEnum;
import org.springframework.core.Ordered;

/**
 * 收到消息处理类  ,需要什么时候触发自行实现即可
 * 实现ordered 方法就行排序，然后按照顺序执行
 */
public interface IModelOperateHandle extends Ordered {

    /**
     * 用户端打开连接的时候触发方法
     *
     * @param wss 连接信息可以从中获取session会话;
     *            userid 用户id;
     *            onlyId 一个用户可以多个连接,此字段加上用户id表示唯一
     */
    default void onOpen(WebSocketServer wss) {

    }

    /**
     * 心跳调用此方法
     *
     * @param wss  链接信息
     * @param data 心跳数据
     */
    default void onPing(WebSocketServer wss, JsonNode data) {

    }

    /**
     * 订阅调用此方法
     *
     * @param wss 连接信息可以从中获取session会话;
     *            userid 用户id;
     *            onlyId 一个用户可以多个连接,此字段加上用户id表示唯一
     * @param msg 订阅的类型
     */
    default void subscribe(WebSocketServer wss, String msg) {
    }

    /**
     * 取消订阅调用此方法
     *
     * @param wss 连接信息可以从中获取session会话;
     *            userid 用户id;
     *            onlyId 一个用户可以多个连接,此字段加上用户id表示唯一
     * @param msg 订阅的类型
     */
    default void unsubscribe(WebSocketServer wss, String msg) {
    }

    /**
     * 断开socket连接的时候调用方法
     *
     * @param onlyId 一个用户可以多个连接,此字段加上用户id表示唯一
     * @param userId 用户id
     */
    default void closeSocket(Long onlyId, Long userId) {
    }

    /**
     * 收到消息调用次方法
     *
     * @param wss  连接信息可以从中获取session会话;
     *             userid 用户id;
     *             onlyId 一个用户可以多个连接,此字段加上用户id表示唯一
     * @param data 消息体
     */
    default void onMessage(WebSocketServer wss, JsonNode data) {
    }

    /**
     * @param wss   连接信息可以从中获取session会话;
     *              userid 用户id;
     *              onlyId 一个用户可以多个连接,此字段加上用户id表示唯一
     * @param error 错误日志
     * @param obj   消息描述
     * @Discription 发送消息失败的回调
     */
    default void sendMessageError(WebSocketServer wss, Throwable error, SendDto obj) {
    }

    /**
     * 判断是否，是本模块 实现类要自己实现次方法
     *
     * @param modelEnum 当前模块
     */
    default boolean isModel(ModelEnum modelEnum) {
        return false;
    }

    /**
     * 参数加工，传入参数就行加工返回要发送的人员id，
     * 例如可以存储redis的key然后通过此方法获取redis的值
     *
     * @param parameters 条件或者是发送的人员id。
     *                   每个模块可以自行实现。
     *                   要是一个模块有多个实现类，次方法会按照Ordered
     *                   进行排序然后依次执行第二次执行是第一次的返回参数
     * @return 要收到消息的人员id
     */
    default Object processParameters(Object parameters) {
        return parameters;
    }

    /**
     * 默认顺序是1
     */
    @Override
    default int getOrder() {
        return 1;
    }
}
