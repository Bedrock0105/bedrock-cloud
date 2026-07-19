package org.bedrock.resource.feign;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.bedrock.common.code.api.R;
import org.bedrock.common.resource.model.sms.SmsResponse;
import org.bedrock.resource.param.SmsSendParam;
import org.bedrock.resource.support.SmsSupport;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短信 Feign 服务端实现
 */
@Hidden
@RestController
@RequiredArgsConstructor
public class SmsClient implements ISmsClient {

    private final SmsSupport smsSupport;

    @Override
    public R<SmsResponse> send(SmsSendParam param) {
        SmsResponse response = smsSupport.smsTemplate().send(param.getParams(), param.getPhone());
        return R.success(response);
    }

}
