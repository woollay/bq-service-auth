package com.biuqu.boot.service.auth.converter.impl;

import com.biuqu.boot.common.auth.constants.AuthConst;
import com.google.common.collect.Sets;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2RefreshTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

/**
 * Jwt刷新服务实现
 *
 * @author BiuQu
 * @date 2023/2/21 08:26
 */
@Slf4j
@Component
public class JwtRefreshAuthConverterImpl implements AuthenticationConverter
{
    @Override
    public Authentication convert(HttpServletRequest request)
    {
        String uri = request.getRequestURI();
        OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.REFRESH_TOKEN, uri);

        // grant_type (REQUIRED)
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (!AuthConst.REFRESH_GRANT_TYPE.getValue().equals(grantType))
        {
            log.error("no jwt refresh type find:{}.", uri);
            return null;
        }

        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
        // refresh_token (REQUIRED)
        String refreshToken = BEARER_JWT_RESOLVER.resolve(request);
        String jwtType = AuthConst.JWT_TYPE_TOKEN;
        try
        {
            Map<String, Object> claims = SignedJWT.parse(refreshToken).getJWTClaimsSet().getClaims();
            if (claims.containsKey(AuthConst.JWT_TYPE))
            {
                jwtType = claims.get(AuthConst.JWT_TYPE).toString();
            }
        }
        catch (Exception e)
        {
            log.error("failed to parse jwt refresh.", e);
            throw new OAuth2AuthenticationException(error);
        }
        if (!AuthConst.JWT_TYPE_REFRESH.equalsIgnoreCase(jwtType))
        {
            log.error("invalid jwt refresh type");
            throw new OAuth2AuthenticationException(error);
        }

        // scope (OPTIONAL)
        String scope = request.getParameter(OAuth2ParameterNames.SCOPE);
        if (StringUtils.isEmpty(scope))
        {
            log.error("no jwt refresh scope find:{}", uri);
            throw new OAuth2AuthenticationException(error);
        }
        Set<String> requestedScopes = Sets.newHashSet(StringUtils.split(scope, " "));
        return new OAuth2RefreshTokenAuthenticationToken(refreshToken, clientPrincipal, requestedScopes, null);
    }

    /**
     * jwt token解析器
     */
    private static final DefaultBearerTokenResolver BEARER_JWT_RESOLVER = new DefaultBearerTokenResolver();
}
