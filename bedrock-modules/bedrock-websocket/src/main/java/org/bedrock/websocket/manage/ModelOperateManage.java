package org.bedrock.websocket.manage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.bedrock.common.code.util.ObjectUtil;
import org.bedrock.common.code.util.StringUtil;
import org.bedrock.websocket.dto.ResultDto;
import org.bedrock.websocket.dto.SendDto;
import org.bedrock.websocket.endpoint.WebSocketServer;
import org.bedrock.websocket.enums.ModelEnum;
import org.bedrock.websocket.enums.OperateTypeEnum;
import org.bedrock.websocket.handle.IModelOperateHandle;
import org.springframework.scheduling.annotation.Async;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @Classname ModelOperateConfiguration
 * @Description IModelOperateInterface 管理类处理核心逻辑
 */
public class ModelOperateManage {

    /**
     * 所有的收到消息处理类
     */
    private final List<IModelOperateHandle> modelOperateList;

    /**
     * 用户连接的时候触发
     */
    @Async("webSocketExecutor")
    public void onOpen(WebSocketServer ws) {
        modelOperateList.forEach(e -> e.onOpen(ws));
    }

    /**
     * 用户ping的时候触发
     */
    @Async("webSocketExecutor")
    public void onPing(ResultDto resultDto, WebSocketServer ws) {
        modelOperateList.forEach(e -> e.onPing(ws, resultDto.getData()));
    }

    /**
     * @param resultDto 消息内容
     * @param ws        连接信息
     * @Discription 收到消息处理方法
     */
    @Async("webSocketExecutor")
    public void receivedMessage(ResultDto resultDto, WebSocketServer ws) {
        modelOperateList.forEach(modelOperate -> {
            /**
             * 是否是此模块
             */
            if (!modelOperate.isModel(resultDto.getModel())) {
                return;
            }
            /**
             * 获取操作类型
             */
            OperateTypeEnum operateType = resultDto.getOperateType();

            /**
             * 类型是收到消息
             */
            if (operateType == OperateTypeEnum.ONMESSAGE) {
                modelOperate.onMessage(ws, resultDto.getData());
                return;
            }
            /**
             * 类型是订阅
             */
            if (operateType == OperateTypeEnum.SUBSCRIBE) {
                JsonNode data = resultDto.getData();
                if ((data instanceof TextNode) && StringUtil.isNotBlank(data.textValue())) {
                    modelOperate.subscribe(ws, data.textValue());
                }
                return;

            }

            /**
             * 类型是取消订阅
             */
            if (operateType == OperateTypeEnum.UNSUBSCRIBE) {
                JsonNode data = resultDto.getData();
                if ((data instanceof TextNode) && StringUtil.isNotBlank(data.textValue())) {
                    modelOperate.unsubscribe(ws, data.textValue());
                }
            }

        });
    }

    /**
     * @param onlyId 连接的唯一标识
     * @param userId 用户id
     * @Discription 端口socket连接调用的回调
     */
    @Async("webSocketExecutor")
    public void closeWebSocket(Long onlyId, Long userId) {
        /**
         * 断开连接不就行类型判断所有的模块，都有通知到
         */
        this.modelOperateList.forEach(modelOperate -> modelOperate.closeSocket(onlyId, userId));
    }

    /**
     * 发送消息发送异常的时候回调
     */
    @Async("webSocketExecutor")
    public void sendMessageError(WebSocketServer wss, Throwable error, SendDto obj) {
        if (ObjectUtil.isEmpty(obj.getModel())) {
            return;
        }
        modelOperateList.forEach(modelOperate -> {
            if (modelOperate.isModel(obj.getModel())) {
                modelOperate.sendMessageError(wss, error, obj);
            }
        });
    }

    /**
     * @param modelEnum  模块
     * @param parameters 参数
     * @return java.util.Collection<java.lang.Long>
     * @Discription 进行参数加工，返回要发送的人员id
     * 最好返回的集合是去重的
     */
    public Collection<Long> processParameters(ModelEnum modelEnum, Object parameters) {
        if (ObjectUtil.isEmpty(modelEnum)) {
            return Collections.emptyList();
        }
        for (IModelOperateHandle iModelOperateInterface : this.modelOperateList) {
            if (!iModelOperateInterface.isModel(modelEnum)) {
                continue;
            }
            parameters = iModelOperateInterface.processParameters(parameters);
        }
        return (Collection<Long>) parameters;
    }

    public ModelOperateManage(List<IModelOperateHandle> modelOperateList) {
        /**
         * 进行order排序
         */
        modelOperateList.sort(Comparator.comparing(IModelOperateHandle::getOrder));
        this.modelOperateList = modelOperateList;
    }
}
