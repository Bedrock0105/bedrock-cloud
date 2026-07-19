package org.bedrock.websocket.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.bedrock.websocket.enums.ModelEnum;
import org.bedrock.websocket.enums.OperateTypeEnum;

import java.util.Collection;


@Data
public class SendDto {

	/**
	 * 标识 操作类型不能为空
	 */
	private OperateTypeEnum operateType = OperateTypeEnum.ONMESSAGE;

	/**
	 * 操作模块 不能为空
	 */
	private ModelEnum model;

	/**
	 * 订阅的功能
	 * 如果为空则是条件内在线的用户都发送，
	 * 如果不为空则是条件内并且订阅的在线用户进行发送
	 * 条件内是指发送的人员范围
	 */
	private String messageType;

	/**
	 * 用户id集合,
	 * 如果数据太多不建议使用结合发送
	 * 可以采取参数的形式
	 *  注意这两个参数要调用不同的feign接口
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private Collection<Long> userIdCollection;

	/**
	 * 参数的形式进行决定发送的用户
	 * 会进行二次加工
	 * 如果要发送多个用户建议使用此参数
	 * 注意这两个参数要调用不同的feign接口
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Object parameters;

	/**
	 * 消息体
	 */
	private Object data;


}
