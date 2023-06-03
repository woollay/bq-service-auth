package com.biuqu.boot.service.auth.handler.impl;

import com.biuqu.boot.service.auth.handler.BaseJwtExceptionHandler;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jwt鉴权过程中出现的异常处理
 *
 * @author BiuQu
 * @date 2023/2/19 23:14
 */
@Component
public class JwtExceptionHandlerImpl extends BaseJwtExceptionHandler<AccessDeniedException>
    implements AccessDeniedHandler
{
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception)
    {
        handle(response, exception);
    }
}
