package org.bedrock.resource.support;

import lombok.RequiredArgsConstructor;
import org.bedrock.common.auth.util.AuthUtil;
import org.bedrock.common.code.constant.StringPool;
import org.bedrock.common.code.util.StringUtil;
import org.bedrock.common.code.util.WebUtil;
import org.bedrock.common.log.exception.ServiceException;
import org.bedrock.common.resource.factory.EmailBeanFactory;
import org.bedrock.common.resource.model.email.EmailProperties;
import org.bedrock.common.resource.service.email.EmailTemplate;
import org.bedrock.resource.service.IEmailConfigService;
import org.bedrock.resource.vo.EmailConfigDetailVO;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * email 发送核心控制类
 */
@Component
@RequiredArgsConstructor
public class EmailSupport {

    private final IEmailConfigService emailConfigService;

    private final EmailBeanFactory emailBeanFactory;

    /**
     * 默认的请求头
     */
    private static final String DEFAULT_HEADER_CONFIG_CODE = "emailConfigCode";

    /**
     * 缓存
     */
    private static final Map<Long, EmailTemplate> emailTemplateMap = new ConcurrentHashMap<>();

    /**
     * 缓存
     */
    private static final Map<String, EmailConfigDetailVO> emailConfigMap = new ConcurrentHashMap<>();

    /**
     * 获取emailTemplate
     */
    public EmailTemplate emailTemplate() {
        return emailTemplate(AuthUtil.getTenantId());
    }

    /**
     * 获取emailTemplate
     */
    public EmailTemplate emailTemplate(String tenantId) {
        return emailTemplate(tenantId, null);
    }

    /**
     * 获取emailTemplate
     */
    public EmailTemplate emailTemplate(String tenantId, String configCode) {
        EmailConfigDetailVO emailConfigDetailVO = getEmailConfigDetailVO(tenantId, configCode);
        if (emailConfigDetailVO == null) {
            throw new ServiceException("未找到对应的Email配置");
        }
        return emailTemplateMap.computeIfAbsent(emailConfigDetailVO.getId(), k -> {
            EmailProperties emailProperties = new EmailProperties();
            emailProperties.setProtocol(emailConfigDetailVO.getProtocol());
            emailProperties.setSmtpServer(emailConfigDetailVO.getSmtpServer());
            emailProperties.setSmtpPort(emailConfigDetailVO.getSmtpPort());
            emailProperties.setEncyType(emailConfigDetailVO.getEncyType());
            emailProperties.setAccountAuth(emailConfigDetailVO.getAccountAuth());
            emailProperties.setUsername(emailConfigDetailVO.getUsername());
            emailProperties.setPassword(emailConfigDetailVO.getPassword());
            return emailBeanFactory.getEmailTemplate(emailConfigDetailVO.getServiceProvider(), emailProperties);
        });
    }

    private EmailConfigDetailVO getEmailConfigDetailVO(String tenantId, String configCode) {
        /**
         * 拼接 key
         * 如果configCode为空，则从请求头中获取
         */
        String key = "";
        if (StringUtil.isNotBlank(tenantId)) {
            key = tenantId + StringPool.COLON;
        }
        // 只有当configCode为空时才读取header
        String code = configCode;
        if (StringUtil.isBlank(code)) {
            String headerCode = WebUtil.getHeader(DEFAULT_HEADER_CONFIG_CODE);
            code = StringUtil.isBlank(headerCode) ? "defaultStatus" : headerCode;
        }
        key += code;
        return emailConfigMap.computeIfAbsent(key, k -> emailConfigService.getByConfigCode(configCode));
    }

    /**
     * 清空缓存
     */
    public static void clearCache(Long id) {
        emailTemplateMap.remove(id);
        emailConfigMap.clear();
    }
}
