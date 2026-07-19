package org.bedrock.resource.feign;

import org.bedrock.common.code.api.R;
import org.bedrock.common.constant.ApplicationConstant;
import org.bedrock.common.resource.model.email.EmailSend;
import org.bedrock.common.resource.model.email.EmailSendAttachment;
import org.bedrock.common.resource.model.email.EmailSendCCAndBCC;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * 邮件 Feign 客户端
 */
@FeignClient(value = ApplicationConstant.APPLICATION_RESOURCE_NAME)
public interface IEmailClient {

    String SEND_MAIL = "/feign/email/send-mail";
    String SEND_MAIL_COPY = "/feign/email/send-mail-copy";
    String SEND_MAIL_ATTACHMENT = "/feign/email/send-mail-attachment";

    /**
     * 发送邮件
     */
    @PostMapping(SEND_MAIL)
    R sendMail(@RequestBody EmailSend emailSend);

    /**
     * 发送邮件（带抄送和密送）
     */
    @PostMapping(SEND_MAIL_COPY)
    R sendMail(@RequestBody EmailSendCCAndBCC emailSend);

    /**
     * 发送邮件（带抄送、密送和附件）
     */
    @PostMapping(value = SEND_MAIL_ATTACHMENT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R sendMail(@RequestPart("emailSend") EmailSendAttachment emailSend, @RequestPart("file") MultipartFile file);

}
