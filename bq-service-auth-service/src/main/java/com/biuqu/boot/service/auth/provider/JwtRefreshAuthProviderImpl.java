package com.biuqu.boot.service.auth.provider;

import com.biuqu.boot.common.auth.constants.AuthConst;
import com.biuqu.boot.common.auth.utils.Oauth2Builder;
import com.biuqu.boot.constants.CommonBootConst;
import com.biuqu.boot.service.auth.gen.JwtTokenGen;
import com.biuqu.context.ApplicationContextHolder;
import com.biuqu.model.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2RefreshTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.ProviderContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.Map;
import java.util.Set;

/**
 * 自定义的刷新认证器
 *
 * @author BiuQu
 * @date 2023/2/21 09:12
 */
@Slf4j
@Component
public class JwtRefreshAuthProviderImpl implements AuthenticationProvider
{
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        OAuth2RefreshTokenAuthenticationToken refreshTokenAuth = (OAuth2RefreshTokenAuthenticationToken)authentication;
        JwtAuthenticationToken principal = getAuthenticatedClient(refreshTokenAuth);
        RegisteredClient client = Oauth2Builder.build(principal.getName(), jwtChannel.getConnTimeout()).build();
        if (!client.getAuthorizationGrantTypes().contains(AuthConst.REFRESH_GRANT_TYPE))
        {
            log.error("invalid grant in configs:{}", refreshTokenAuth.getGrantType());
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
        }

        Map<String, Object> addParameters = refreshTokenAuth.getAdditionalParameters();
        OAuth2Authorization.Builder authBuilder = Oauth2Builder.build(client);
        authBuilder.authorizationGrantType(new AuthorizationGrantType(refreshTokenAuth.getGrantType().getValue()));
        authBuilder.attributes(parameters -> parameters.putAll(addParameters));
        OAuth2Authorization authorization = authBuilder.build();

        Set<String> authorizedScopes = authorization.getAttribute(OAuth2Authorization.AUTHORIZED_SCOPE_ATTRIBUTE_NAME);
        if (!CollectionUtils.containsAny(refreshTokenAuth.getScopes(), authorizedScopes))
        {
            log.error("invalid scope:{}", refreshTokenAuth.getScopes());
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
        }

        DefaultOAuth2TokenContext.Builder tokenContextBuilder =
            DefaultOAuth2TokenContext.builder().registeredClient(client).principal(principal)
                .providerContext(ProviderContextHolder.getProviderContext()).authorization(authorization)
                .authorizedScopes(authorizedScopes).authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrant(refreshTokenAuth);

        OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
        JwtGenerator tokenGenerator = ApplicationContextHolder.getBean(JwtGenerator.class);
        OAuth2Token auth2Token = tokenGenerator.generate(tokenContext);
        if (auth2Token == null)
        {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.SERVER_ERROR);
        }

        String jwt = auth2Token.getTokenValue();
        Instant issuedAt = auth2Token.getIssuedAt();
        Instant expiresAt = auth2Token.getExpiresAt();
        OAuth2AccessToken.TokenType tokenType = OAuth2AccessToken.TokenType.BEARER;
        OAuth2AccessToken accessToken = new OAuth2AccessToken(tokenType, jwt, issuedAt, expiresAt, authorizedScopes);

        String refreshJwt = tokenGen.genRefreshJwt(jwt);
        Instant refreshExpiresAt = Instant.ofEpochSecond(issuedAt.getEpochSecond() + jwtChannel.getTimeout());
        OAuth2RefreshToken refreshToken = new OAuth2RefreshToken(refreshJwt, issuedAt, refreshExpiresAt);
        return new OAuth2AccessTokenAuthenticationToken(client, principal, accessToken, refreshToken, addParameters);
    }

    @Override
    public boolean supports(Class<?> authentication)
    {
        return OAuth2RefreshTokenAuthenticationToken.class.isAssignableFrom(authentication);
    }

    /**
     * 获取认证通过的token对象
     *
     * @param authentication 认证对象
     * @return 认证后的token对象
     * @throws OAuth2AuthenticationException 认证失败异常
     */
    private static JwtAuthenticationToken getAuthenticatedClient(OAuth2RefreshTokenAuthenticationToken authentication)
    throws OAuth2AuthenticationException
    {
        JwtAuthenticationToken clientPrincipal = null;
        Object principal = authentication.getPrincipal();
        if (principal instanceof JwtAuthenticationToken)
        {
            clientPrincipal = (JwtAuthenticationToken)principal;
        }

        if (clientPrincipal != null && clientPrincipal.isAuthenticated())
        {
            return clientPrincipal;
        }
        throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
    }

    @Autowired
    private JwtTokenGen tokenGen;

    /**
     * jwt配置
     */
    @Resource(name = CommonBootConst.JWT_CHANNEL_CONFIG)
    private Channel jwtChannel;
}
