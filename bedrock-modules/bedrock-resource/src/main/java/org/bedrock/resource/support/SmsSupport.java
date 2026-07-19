package org.bedrock.resource.support;

import lombok.RequiredArgsConstructor;
import org.bedrock.common.auth.util.AuthUtil;
import org.bedrock.common.code.constant.StringPool;
import org.bedrock.common.code.util.StringUtil;
import org.bedrock.common.code.util.WebUtil;
import org.bedrock.common.log.exception.ServiceException;
import org.bedrock.common.resource.factory.SmsBeanFactory;
import org.bedrock.common.resource.model.sms.SmsProperties;
import org.bedrock.common.resource.service.sms.SmsTemplate;
import org.bedrock.resource.service.ISmsConfigService;
import org.bedrock.resource.vo.SmsConfigDetailVO;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * sms 发送核心控制类
 */
@Component
@RequiredArgsConstructor
public class SmsSupport {

    private final ISmsConfigService smsConfigService;

    private final SmsBeanFactory smsBeanFactory;

    /**
     * 默认的请求头
     */
    private static final String DEFAULT_HEADER_CONFIG_CODE = "smsConfigCode";

    /**
     * 缓存
     */
    private static final Map<Long, SmsTemplate> smsTemplateMap = new ConcurrentHashMap<>();

    /**
     * 缓存
     */
    private static final Map<String, SmsConfigDetailVO> smsConfigMap = new ConcurrentHashMap<>();

    /**
     * 获取smsTemplate
     */
    public SmsTemplate smsTemplate() {
        return smsTemplate(AuthUtil.getTenantId());
    }

    /**
     * 获取smsTemplate
     */
    public SmsTemplate smsTemplate(String tenantId) {
        return smsTemplate(tenantId, null);
    }

    /**
     * 获取smsTemplate
     */
    public SmsTemplate smsTemplate(String tenantId, String configCode) {
        SmsConfigDetailVO smsConfigDetailVO = getSmsConfigDetailVO(tenantId, configCode);
        if (smsConfigDetailVO == null) {
            throw new ServiceException("未找到对应的SMS配置");
        }
        return smsTemplateMap.computeIfAbsent(smsConfigDetailVO.getId(), k -> {
            SmsProperties smsProperties = new SmsProperties();
            smsProperties.setApiKey(smsConfigDetailVO.getApiKey());
            smsProperties.setApiSecret(smsConfigDetailVO.getApiSecret());
            smsProperties.setSignature(smsConfigDetailVO.getSignature());
            smsProperties.setTemplateId(smsConfigDetailVO.getTemplateId());
            smsProperties.setEndpoint(smsConfigDetailVO.getEndpoint());
            smsProperties.setRegion(smsConfigDetailVO.getRegion());
            smsProperties.setAppId(smsConfigDetailVO.getAppId());
            smsProperties.setSender(smsConfigDetailVO.getSender());
            return smsBeanFactory.getSmsTemplate(smsConfigDetailVO.getServiceProvider(), smsProperties);
        });
    }

    private SmsConfigDetailVO getSmsConfigDetailVO(String tenantId, String configCode) {
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
        return smsConfigMap.computeIfAbsent(key, k -> smsConfigService.getByConfigCode(configCode));
    }

    /**
     * 清空缓存
     */
    public static void clearCache(Long id) {
        smsTemplateMap.remove(id);
        smsConfigMap.clear();
    }
}
