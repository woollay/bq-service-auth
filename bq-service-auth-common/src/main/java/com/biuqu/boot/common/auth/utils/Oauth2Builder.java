package com.biuqu.boot.common.auth.utils;

import com.biuqu.boot.common.auth.constants.AuthConst;
import com.biuqu.utils.IdUtil;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2TokenFormat;
import org.springframework.security.oauth2.core.OAuth2TokenType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;

import java.time.Duration;

/**
 * 认证构建的工具类
 *
 * @author BiuQu
 * @date 2023/2/21 10:01
 */
public final class Oauth2Builder
{
    /**
     * 构建注册好的客户端对象builder
     *
     * @param clientId 客户id
     * @param expire   过期时间
     * @return 注册好的客户端对象builder
     */
    public static RegisteredClient.Builder build(String clientId, long expire)
    {
        RegisteredClient.Builder clientBuilder = RegisteredClient.withId(IdUtil.uuid());
        clientBuilder.clientId(clientId).scope(AuthConst.JWT_SCOPE);
        clientBuilder.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
        clientBuilder.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS);
        clientBuilder.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN);
        clientBuilder.authorizationGrantType(AuthConst.REFRESH_GRANT_TYPE);

        TokenSettings.Builder tokenBuilder = TokenSettings.builder();
        tokenBuilder.accessTokenTimeToLive(Duration.ofSeconds(expire));
        tokenBuilder.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED);
        clientBuilder.tokenSettings(tokenBuilder.build());

        clientBuilder.clientSettings(ClientSettings.builder().build());
        return clientBuilder;
    }

    /**
     * 构建provider认证时使用的对象builder
     *
     * @param client 认证通过的客户端对象
     * @return provider认证时使用的对象builder
     */
    public static OAuth2Authorization.Builder build(RegisteredClient client)
    {
        OAuth2Authorization.Builder builder = OAuth2Authorization.withRegisteredClient(client);
        String id = client.getClientId();
        String principalName = client.getClientId();

        builder.id(id).principalName(principalName);
        builder.attribute(OAuth2ParameterNames.STATE, OAuth2TokenType.REFRESH_TOKEN);
        builder.attribute(OAuth2Authorization.AUTHORIZED_SCOPE_ATTRIBUTE_NAME, client.getScopes());
        return builder;
    }

    private Oauth2Builder()
    {
    }
}
