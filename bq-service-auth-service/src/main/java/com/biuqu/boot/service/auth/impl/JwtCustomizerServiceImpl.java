package com.biuqu.boot.service.auth.impl;

import com.biuqu.boot.common.auth.constants.AuthConst;
import com.biuqu.boot.common.auth.model.ClientResource;
import com.biuqu.model.JwtSourceType;
import com.biuqu.service.BaseBizService;
import com.biuqu.utils.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.authorization.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenCustomizer;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 注入jwt定制服务
 *
 * @author BiuQu
 * @date 2023/2/7 08:12
 */
@Slf4j
@Service
public class JwtCustomizerServiceImpl implements OAuth2TokenCustomizer<JwtEncodingContext>
{
    @Override
    public void customize(JwtEncodingContext context)
    {
        String clientId = context.getRegisteredClient().getClientId();
        ClientResource param = new ClientResource();
        param.setAppId(clientId);

        ClientResource clientResource = clientService.get(param);
        context.getClaims().claim(AuthConst.JWT_RESOURCES, clientResource.getResources());
        context.getClaims().claim(JwtClaimNames.JTI, IdUtil.uuid());
        context.getClaims().claim(AuthConst.JWT_TYPE, AuthConst.JWT_TYPE_TOKEN);
        context.getClaims().claim(OAuth2ParameterNames.TOKEN_TYPE, OAuth2AccessToken.TokenType.BEARER.getValue());

        //获取复制出来的jwt body键值对
        Map<String, Object> claims = context.getClaims().build().getClaims();
        Object sourceType = JwtSourceType.SDK.name();
        if (claims.containsKey(AuthConst.JWT_SOURCE_TYPE))
        {
            sourceType = claims.get(AuthConst.JWT_SOURCE_TYPE);
        }
        context.getClaims().claim(AuthConst.JWT_SOURCE_TYPE, sourceType);
    }

    /**
     * 注入client信息查询服务
     */
    @Autowired
    private BaseBizService<ClientResource> clientService;
}
