package com.biuqu.boot.service.auth.handler.impl;

import com.biuqu.boot.service.auth.handler.BaseJwtExceptionHandler;
import com.biuqu.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 认证失败的异常处理
 *
 * @author BiuQu
 * @date 2023/2/19 23:28
 */
@Slf4j
@Component
public class JwtAuthFailureHandlerImpl extends BaseJwtExceptionHandler<AuthenticationException>
    implements AuthenticationFailureHandler
{
    @Override
    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse resp, AuthenticationException e)
    {
        handle(resp, e);
    }

    @Override
    protected void log(AuthenticationException exception)
    {
        OAuth2Error error = ((OAuth2AuthenticationException)exception).getError();
        log.error("jwt auth failed:{}", JsonUtil.toJson(error));
        super.log(exception);
    }
}
