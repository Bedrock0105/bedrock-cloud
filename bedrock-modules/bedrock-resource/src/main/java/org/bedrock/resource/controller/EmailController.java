package org.bedrock.resource.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bedrock.common.boot.base.BaseController;
import org.bedrock.common.code.api.R;
import org.bedrock.common.resource.model.email.*;
import org.bedrock.resource.support.EmailSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;

/**
 * 邮件发送控制器
 * <p>通过请求头 emailConfigCode 指定使用的邮件配置</p>
 */
@Tag(name = "邮件发送")
@Slf4j
@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController extends BaseController {

    private final EmailSupport emailSupport;

    /**
     * 发送邮件
     * <p>请求头 emailConfigCode 为空时使用当前启用的配置</p>
     */
    @PostMapping("/send-mail")
    public R sendMail(@RequestBody EmailSend emailSend) {
        return R.status(emailSupport.emailTemplate().send(emailSend).success());
    }

    /**
     * 发送邮件
     * 带抄送和密信
     */
    @PostMapping("/send-mail-copy")
    public R sendMail(@RequestBody EmailSendCCAndBCC emailSend) {
        return R.status(emailSupport.emailTemplate().send(emailSend).success());
    }

    /**
     * 发送邮件
     * 带抄送和密信
     * 带附件
     */
    @SneakyThrows
    @PostMapping("/send-mail-attachment")
    public R sendMail(@RequestPart("emailSend") EmailSendAttachment emailSend, @RequestPart("file") MultipartFile file) {
        emailSend.setAttachments(Collections.singletonList(new AttachmentDataSource(file.getOriginalFilename(),
                file.getInputStream(),
                file.getContentType())));
        return R.status(emailSupport.emailTemplate().send(emailSend).success());
    }

}
