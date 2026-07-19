package org.bedrock.auth.handler;

import org.bedrock.auth.model.UserDetailsModel;
import org.bedrock.common.auth.constant.TokenConstant;
import org.bedrock.common.authentication.handler.TokenEnhancerHandler;
import org.bedrock.common.authentication.model.OauthTokenParams;
import org.bedrock.common.authentication.model.OauthTokenResult;
import org.bedrock.common.authentication.support.ClientDetails;
import org.bedrock.common.authentication.support.UserDetails;
import org.bedrock.common.code.util.WebUtil;

public class TokenParamTokenEnhancerHandler implements TokenEnhancerHandler {

    @Override
    public OauthTokenResult enhance(OauthTokenResult tokenResult,
                                    ClientDetails client,
                                    UserDetails userDetails,
                                    OauthTokenParams params) {
        if (!(userDetails instanceof UserDetailsModel userDetailsModel)) {
            return tokenResult;
        }
        tokenResult
                .additionalInformation(TokenConstant.USERNAME, userDetailsModel.getUsername())
                .additionalInformation(TokenConstant.USERID, userDetailsModel.userId())
                .additionalInformation(TokenConstant.TENANT_ID, userDetailsModel.tenantId())
                .additionalInformation(TokenConstant.PHONE, userDetailsModel.phone())
                .additionalInformation(TokenConstant.NICKNAME, userDetailsModel.nickname())
                .additionalInformation(TokenConstant.DEPT_IDS, userDetailsModel.deptIds())
                .additionalInformation(TokenConstant.ROLE_IDS, userDetailsModel.roleIds())
                .additionalInformation(TokenConstant.ROLE_ALIAS, userDetailsModel.roleAlias())
                .additionalInformation(TokenConstant.PARAMS, userDetailsModel.params())
                .additionalInformation(TokenConstant.IPADDR, WebUtil.getIP());
        return tokenResult;
    }
}
