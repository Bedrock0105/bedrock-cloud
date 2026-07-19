package org.bedrock.websocket.enums;

import lombok.Getter;

@Getter
public enum OperateTypeEnum {
	/**
	 *
	 * 用于心跳监测
	 */
	PING,
	/**
	 * 订阅
	 * 订阅的时候 ，订阅定义的功能不能和其他的功能一样否则会冲突
	 */
	SUBSCRIBE,
	/**
	 * 取消订阅
	 */
	UNSUBSCRIBE,
	/**
	 * 消息
	 */
	ONMESSAGE,
	;



	OperateTypeEnum() {

	}
}
