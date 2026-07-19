package org.bedrock.resource.feign;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bedrock.common.code.api.R;
import org.bedrock.common.resource.model.email.AttachmentDataSource;
import org.bedrock.common.resource.model.email.EmailSend;
import org.bedrock.common.resource.model.email.EmailSendAttachment;
import org.bedrock.common.resource.model.email.EmailSendCCAndBCC;
import org.bedrock.resource.support.EmailSupport;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;

/**
 * 邮件 Feign 服务端实现
 */
@Hidden
@RestController
@RequiredArgsConstructor
public class EmailClient implements IEmailClient {

    private final EmailSupport emailSupport;

    @Override
    public R sendMail(EmailSend emailSend) {
        return R.status(emailSupport.emailTemplate().send(emailSend).success());
    }

    @Override
    public R sendMail(EmailSendCCAndBCC emailSend) {
        return R.status(emailSupport.emailTemplate().send(emailSend).success());
    }

    @Override
    @SneakyThrows
    public R sendMail(EmailSendAttachment emailSend, MultipartFile file) {
        emailSend.setAttachments(Collections.singletonList(new AttachmentDataSource(file.getOriginalFilename(),
                file.getInputStream(),
                file.getContentType())));
        return R.status(emailSupport.emailTemplate().send(emailSend).success());
    }

}
