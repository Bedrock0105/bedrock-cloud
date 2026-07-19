package org.bedrock.auth.util;

import org.bedrock.auth.enums.OauthErrorEnums;
import org.bedrock.common.authentication.exception.OAuth2Exception;
import org.bedrock.common.mybatisplus.constant.BedrockDBConstant;
import org.bedrock.common.tenant.constant.TenantConstant;
import org.bedrock.system.vo.TenantDetailVO;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 工具方法
 */
public abstract class TokenUtil {

    /**
     * 校验租户信息
     *
     * @param tenantDetail 租户信息
     * @return true:校验通过
     */
    public static void checkTenant(TenantDetailVO tenantDetail) {
        if (tenantDetail == null) {
            throw new OAuth2Exception(OauthErrorEnums.TENANT_INFORMATION_IS_EMPTY);
        }
        /**
         * 默认租户ID 不进行校验
         */
        if (TenantConstant.DEFAULT_TENANT_ID.equals(tenantDetail.getTenantId())) {
            return;
        }
        if (Objects.equals(tenantDetail.getStatus(), BedrockDBConstant.DB_STATUS_DISABLE)) {
            throw new OAuth2Exception(OauthErrorEnums.TENANT_IS_DISABLED);
        }
        if (tenantDetail.getExpireTime() != null && tenantDetail.getExpireTime().isBefore(LocalDateTime.now())) {
            throw new OAuth2Exception(OauthErrorEnums.TENANT_IS_EXPIRED);
        }
    }
}
