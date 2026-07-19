package org.bedrock.resource.support;

import lombok.RequiredArgsConstructor;
import org.bedrock.common.auth.util.AuthUtil;
import org.bedrock.common.code.constant.StringPool;
import org.bedrock.common.code.util.StringUtil;
import org.bedrock.common.code.util.WebUtil;
import org.bedrock.common.log.exception.ServiceException;
import org.bedrock.common.resource.factory.OssBeanFactory;
import org.bedrock.common.resource.model.oss.OssProperties;
import org.bedrock.common.resource.service.oss.OssTemplate;
import org.bedrock.resource.service.IOssConfigService;
import org.bedrock.resource.vo.OssConfigDetailVO;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * oss 上传核心控制类
 */
@Component
@RequiredArgsConstructor
public class OssSupport {

    private final IOssConfigService ossConfigService;

    private final OssBeanFactory ossBeanFactory;

    /**
     * 默认的请求头
     */
    private static final String DEFAULT_HEADER_CONFIG_CODE = "ossConfigCode";

    /**
     * 缓存
     */
    private static final Map<Long, OssTemplate> ossTemplateMap = new ConcurrentHashMap<>();

    /**
     * 缓存
     */
    private static final Map<String, OssConfigDetailVO> ossConfigMap = new ConcurrentHashMap<>();

    /**
     * 获取ossTemplate
     */
    public OssTemplate ossTemplate() {
        return ossTemplate(AuthUtil.getTenantId());
    }

    /**
     * 获取ossTemplate
     */
    public OssTemplate ossTemplate(String tenantId) {
        return ossTemplate(tenantId, null);
    }

    /**
     * 获取ossTemplate
     */
    public OssTemplate ossTemplate(String tenantId, String configCode) {
        OssConfigDetailVO ossConfigDetailVO = getOssConfigDetailVO(tenantId, configCode);
        if (ossConfigDetailVO == null) {
            throw new ServiceException("未找到对应的OSS配置");
        }
        return ossTemplateMap.computeIfAbsent(ossConfigDetailVO.getId(), k -> {
            OssProperties ossProperties = new OssProperties();
            ossProperties.setEndpoint(ossConfigDetailVO.getEndpoint());
            ossProperties.setAccessKey(ossConfigDetailVO.getAccessKey());
            ossProperties.setSecretKey(ossConfigDetailVO.getSecretKey());
            ossProperties.setBucketName(ossConfigDetailVO.getBucketName());
            ossProperties.setPrefixPath(ossConfigDetailVO.getPrefixPath());
            ossProperties.setPublicUrl(ossConfigDetailVO.getPublicUrl());
            ossProperties.setBucketRegion(ossConfigDetailVO.getBucketRegion());
            return ossBeanFactory.getOssTemplate(ossConfigDetailVO.getServiceProvider(), ossProperties);
        });
    }

    private OssConfigDetailVO getOssConfigDetailVO(String tenantId, String configCode) {
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
        return ossConfigMap.computeIfAbsent(key, k -> ossConfigService.getByConfigCode(configCode));
    }

    /**
     * 清空缓存
     */
    public static void clearCache(Long id) {
        ossTemplateMap.remove(id);
        ossConfigMap.clear();
    }
}
