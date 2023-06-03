package com.biuqu.boot.startup.auth.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.web.OAuth2TokenEndpointFilter;
import org.springframework.security.oauth2.server.authorization.web.authentication.DelegatingAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2ClientCredentialsAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2RefreshTokenAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import java.util.Arrays;
import java.util.List;

/**
 * 安全过滤器的管理器
 *
 * @author BiuQu
 * @date 2023/2/19 23:58
 */
@Slf4j
@Component
public final class SecurityFilterMgr
{
    /**
     * 构建定制的过滤器链
     *
     * @param http        基于请求的安全对象
     * @param authManager 安全认证管理对象
     * @return 过滤器链(Filter模式)
     * @throws Exception 初始化过滤器时的异常
     */
    public SecurityFilterChain custom(HttpSecurity http, AuthenticationManager authManager) throws Exception
    {
        SecurityFilterChain filterChain = http.build();
        for (Filter filter : filterChain.getFilters())
        {
            if (filter instanceof BearerTokenAuthenticationFilter)
            {
                ((BearerTokenAuthenticationFilter)filter).setAuthenticationFailureHandler(authFailureHandler);
            }
            else if (filter instanceof OAuth2TokenEndpointFilter)
            {
                //1.加自定义属性
                OAuth2TokenEndpointFilter tokenFilter = (OAuth2TokenEndpointFilter)filter;
                tokenFilter.setAuthenticationSuccessHandler(authSuccessHandler);

                //2.新增刷新转换器
                AuthenticationConverter authConverter = new DelegatingAuthenticationConverter(
                    Arrays.asList(new OAuth2AuthorizationCodeAuthenticationConverter(),
                        new OAuth2RefreshTokenAuthenticationConverter(),
                        new OAuth2ClientCredentialsAuthenticationConverter(), refreshAuthConverter));
                tokenFilter.setAuthenticationConverter(authConverter);

                //3.新增刷新认证器
                if (authManager instanceof ProviderManager)
                {
                    ProviderManager providerManager = (ProviderManager)authManager;
                    List<AuthenticationProvider> providers = providerManager.getProviders();
                    providers.add(refreshAuthProvider);
                }
            }
        }
        return filterChain;
    }

    /**
     * 新增的刷新token认证器
     */
    @Autowired
    private AuthenticationProvider refreshAuthProvider;

    /**
     * 新增的刷新token转换器
     */
    @Autowired
    private AuthenticationConverter refreshAuthConverter;

    /**
     * 认证成功的处理器
     */
    @Autowired
    private AuthenticationSuccessHandler authSuccessHandler;

    /**
     * 认证失败的异常处理器
     */
    @Autowired
    private AuthenticationFailureHandler authFailureHandler;
}
