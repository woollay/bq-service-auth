package com.biuqu.boot.service.auth.handler;

import com.biuqu.errcode.ErrCodeEnum;
import com.biuqu.model.ResultCode;
import com.biuqu.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

/**
 * 抽象的Jwt异常处理Handler
 *
 * @author BiuQu
 * @date 2023/2/19 22:58
 */
@Slf4j
public abstract class BaseJwtExceptionHandler<T extends RuntimeException>
{
    /**
     * 认证失败的异常处理
     *
     * @param response  响应对象
     * @param exception 异常对象
     */
    public void handle(HttpServletResponse response, T exception)
    {
        try
        {
            log(exception);

            response.setStatus(HttpStatus.OK.value());
            MediaType mediaType = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);
            response.setHeader(HttpHeaders.CONTENT_TYPE, mediaType.toString());

            ResultCode<?> fail = ResultCode.error(ErrCodeEnum.AUTH_ERROR.getCode());
            String json = JsonUtil.toJson(fail, snakeCase);
            response.getWriter().write(json);
            response.getWriter().flush();
        }
        catch (Exception e)
        {
            log.error("auth failed with unknown exception.", exception);
        }
    }

    /**
     * 认证失败的日志处理
     *
     * @param exception 失败的异常
     */
    protected void log(T exception)
    {
        log.error("auth failed.", exception);
    }

    /**
     * 是否驼峰式json(默认支持)
     */
    @Value("${bq.json.snake-case:true}")
    private boolean snakeCase;
}
