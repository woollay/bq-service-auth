package com.biuqu.boot.startup.auth.configure;

import com.biuqu.boot.constants.CommonBootConst;
import com.biuqu.boot.service.auth.handler.impl.JwtAuthFailureHandlerImpl;
import com.biuqu.boot.service.auth.handler.impl.JwtExceptionHandlerImpl;
import com.biuqu.boot.startup.auth.filter.SecurityFilterMgr;
import com.biuqu.jwt.JwkMgr;
import com.biuqu.model.Channel;
import com.google.common.collect.Sets;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwsEncoder;
import org.springframework.security.oauth2.server.authorization.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 认证配置服务
 *
 * @author BiuQu
 * @date 2023/2/6 21:58
 */
@Slf4j
@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class ServerConfigurer
{
    /**
     * 注入认证管理器
     *
     * @param authConf 认证配置
     * @return 认证管理器
     * @throws Exception 初始化异常
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConf) throws Exception
    {
        return authConf.getAuthenticationManager();
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain serverChain(HttpSecurity http, AuthenticationManager authManager,
        JWKSource<SecurityContext> jwkSource, JwtGenerator jwtGen) throws Exception
    {
        //1.前后端分离，禁用会话管理和csrf(跨站攻击)
        http.sessionManagement().disable();
        http.csrf().disable();

        //2.添加匿名访问的url(应该包括jwk)
        Set<String> anonymous = Sets.newHashSet();
        if (!CollectionUtils.isEmpty(ignoreUrls))
        {
            anonymous.addAll(ignoreUrls);
        }
        String[] anonUrls = anonymous.toArray(new String[] {});
        http.authorizeRequests(registry -> registry.antMatchers(anonUrls).permitAll().anyRequest().authenticated());

        //3.设置服务端配置(指定jwt生成器等)
        OAuth2AuthorizationServerConfigurer<HttpSecurity> serverConf = new OAuth2AuthorizationServerConfigurer<>();
        http.apply(serverConf);

        serverConf.tokenGenerator(jwtGen);
        //设置认证信息匹配失败的异常
        serverConf.clientAuthentication(clientConf -> clientConf.errorResponseHandler(this.failureHandler));
        //设置token生成失败的异常
        serverConf.tokenEndpoint(tokenConf -> tokenConf.errorResponseHandler(this.failureHandler));
        //设置全局处理异常
        http.exceptionHandling(exceptionHandler -> exceptionHandler.accessDeniedHandler(this.exceptionHandler));

        //4.设置业务请求的jwt解析配置
        http.oauth2ResourceServer(resourceConf ->
        {
            resourceConf.bearerTokenResolver(new DefaultBearerTokenResolver());
            OAuth2ResourceServerConfigurer<HttpSecurity>.JwtConfigurer resourceJwtConf = resourceConf.jwt();
            resourceJwtConf.decoder(OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource));
            //设置资源解析失败的异常(主要是资源带的token解析/认证失败)
            resourceConf.accessDeniedHandler(this.exceptionHandler);
        });

        return filterMgr.custom(http, authManager);
    }

    @Bean
    public PasswordEncoder pwdEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    /**
     * 注入秘钥管理服务
     *
     * @param jwkMgr 秘钥管理服务({@link com.biuqu.boot.startup.auth.configure.JwtConfigurer#jwkMgr(Channel)})
     * @return 秘钥管理服务
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource(JwkMgr jwkMgr)
    {
        JWKSet jwkSet = new JWKSet(jwkMgr.getJwk());
        return (jwkSelector, context) -> jwkSelector.select(jwkSet);
    }

    @Bean
    public ProviderSettings providerSettings()
    {
        ProviderSettings.Builder builder = ProviderSettings.builder();
        return builder.jwkSetEndpoint(jwtChannel.getAuthUrl()).tokenEndpoint(jwtChannel.getUrl()).build();
    }

    /**
     * 注入 jwt token生成器
     *
     * @param jwkSource 秘钥上下文
     * @return jwt token生成器
     */
    @Bean
    public JwtGenerator jwtGenerator(JWKSource<SecurityContext> jwkSource)
    {
        //1.oauth2-server0.2.3匹配的springboot和springboot-security是2.5.12,无法使用NimbusJwtEncoder
        JwtGenerator jwtGenerator = new JwtGenerator(new NimbusJwsEncoder(jwkSource));
        jwtGenerator.setJwtCustomizer(tokenCustomizer);
        return jwtGenerator;
    }

    /**
     * 定制的过滤器管理器
     */
    @Autowired
    private SecurityFilterMgr filterMgr;

    /**
     * 鉴权拒绝(出现了非失败的异常)时的异常处理(参见{@link JwtExceptionHandlerImpl}实现)
     */
    @Autowired
    private AccessDeniedHandler exceptionHandler;

    /**
     * 鉴权不通过的异常处理(参见{@link JwtAuthFailureHandlerImpl}实现)
     */
    @Autowired
    private AuthenticationFailureHandler failureHandler;

    /**
     * 注入定制的token字典处理服务
     */
    @Autowired
    private OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer;

    /**
     * jwt配置
     */
    @Resource(name = CommonBootConst.JWT_CHANNEL_CONFIG)
    private Channel jwtChannel;

    /**
     * 注入忽略的url列表
     */
    @Value("${bq.auth.ignoreUrls}")
    private Set<String> ignoreUrls;
}
