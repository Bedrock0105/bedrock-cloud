package org.bedrock.websocket.feign;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.bedrock.common.code.api.R;
import org.bedrock.common.redis.stream.template.RSMessageTemplate;
import org.bedrock.websocket.constant.WebSocketConstant;
import org.bedrock.websocket.dto.SendDto;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequiredArgsConstructor
public class WebSocketClient implements IWebSocketClient {

    private final RSMessageTemplate rs;

    @Override
    public R<String> sendAll(SendDto sendDto) {
        RecordId send = rs.send(WebSocketConstant.SENDALL, sendDto);
        return R.success(send.getValue());
    }

    @Override
    public R<String> sendByUserId(SendDto sendDto) {
        RecordId send = rs.send(WebSocketConstant.SEND_BY_USER_ID, sendDto);
        return R.success(send.getValue());
    }

    @Override
    public R<String> sendByParameter(SendDto sendDto) {
        RecordId send = rs.send(WebSocketConstant.SEND_BY_PARAMETER, sendDto);
        return R.success(send.getValue());
    }
}
