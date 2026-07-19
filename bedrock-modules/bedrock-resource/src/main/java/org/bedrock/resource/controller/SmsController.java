package org.bedrock.resource.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bedrock.common.boot.base.BaseController;
import org.bedrock.common.code.api.R;
import org.bedrock.common.resource.model.sms.SmsResponse;
import org.bedrock.resource.param.SmsSendParam;
import org.bedrock.resource.support.SmsSupport;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短信发送控制器
 * <p>通过请求头 smsConfigCode 指定使用的短信配置</p>
 */
@Tag(name = "短信发送")
@Slf4j
@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
public class SmsController extends BaseController {

    private final SmsSupport smsSupport;

    /**
     * 发送短信
     * <p>请求头 smsConfigCode 为空时使用当前启用的配置</p>
     */
    @PostMapping("/send")
    @Operation(summary = "发送短信")
    @ApiOperationSupport(order = 1)
    public R<SmsResponse> send(@RequestBody SmsSendParam param) {
        SmsResponse response = smsSupport.smsTemplate().send(param.getParams(), param.getPhone());
        return R.success(response);
    }

}
