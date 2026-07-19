package org.bedrock.websocket.feign;

import org.bedrock.common.code.api.R;
import org.bedrock.common.constant.ApplicationConstant;
import org.bedrock.websocket.dto.SendDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        value = ApplicationConstant.APPLICATION_WEBSOCKET_NAME
)
public interface IWebSocketClient {

    /**
     * 发送所有的用户 不知道人员范围
     */
    String SEND_ALL_USER = "/feign/send-all-user";

    /**
     * 指定用户id发送消息
     */
    String SEND_BY_USER_ID = "/feign/send-by-user-id";

    /**
     * 更具条件发送用户
     */
    String SEND_BY_PARAMETERS = "/feign/send-by-parameter";

    /**
     * 发送所有在线的用户
     * 不指定人员
     */
    @PostMapping(SEND_ALL_USER)
    R<String> sendAll(@RequestBody SendDto sendDto);

    /**
     * 指定用户id发送消息
     */
    @PostMapping(SEND_BY_USER_ID)
    R<String> sendByUserId(@RequestBody SendDto sendDto);

    /**
     * 指定参数进行发送用户
     */
    @PostMapping(SEND_BY_PARAMETERS)
    R<String> sendByParameter(@RequestBody SendDto sendDto);
}
