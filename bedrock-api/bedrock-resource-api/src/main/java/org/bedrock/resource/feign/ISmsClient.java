package org.bedrock.resource.feign;

import org.bedrock.common.code.api.R;
import org.bedrock.common.constant.ApplicationConstant;
import org.bedrock.common.resource.model.sms.SmsResponse;
import org.bedrock.resource.param.SmsSendParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 短信 Feign 客户端
 */
@FeignClient(value = ApplicationConstant.APPLICATION_RESOURCE_NAME)
public interface ISmsClient {

    String SEND_SMS = "/feign/sms/send";

    /**
     * 发送短信
     */
    @PostMapping(SEND_SMS)
    R<SmsResponse> send(@RequestBody SmsSendParam param);

}
