package org.bedrock.websocket.support;

import jakarta.websocket.Decoder;
import lombok.extern.slf4j.Slf4j;
import org.bedrock.common.code.util.JsonUtil;
import org.bedrock.websocket.dto.ResultDto;

@Slf4j
public class ResultDtoDecoder implements Decoder.Text<ResultDto> {

    @Override
    public ResultDto decode(String json) {
        return JsonUtil.parse(json, ResultDto.class);
    }

    @Override
    public boolean willDecode(String json) {
        return json != null && !json.trim().isEmpty()
                && json.startsWith("{") && json.endsWith("}");
    }
}
