package com.biuqu.boot.service.auth.converter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponseMapConverter;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Jwt消息转换器
 * <p>
 * 参见{org.springframework.security.oauth2.server.authorization.web.OAuth2AccessTokenResponseHttpMessageConverter}
 *
 * @author BiuQu
 * @date 2023/2/20 09:24
 */
@Component
public class JwtRespConverterImpl extends OAuth2AccessTokenResponseHttpMessageConverter
{
    @Override
    protected void writeInternal(OAuth2AccessTokenResponse tokenResponse, HttpOutputMessage outputMessage)
    throws HttpMessageNotWritableException
    {
        try
        {
            Map<String, Object> tokenResponseParameters;
            // Only use deprecated converter if it has been set directly
            if (this.tokenResponseParametersConverter.getClass() != OAuth2AccessTokenResponseMapConverter.class)
            {
                tokenResponseParameters =
                    new LinkedHashMap<>(this.tokenResponseParametersConverter.convert(tokenResponse));
            }
            else
            {
                tokenResponseParameters = this.jwtRespMapConverter.convert(tokenResponse);
            }
            CONVERTER.write(tokenResponseParameters, STRING_OBJECT_MAP.getType(), MediaType.APPLICATION_JSON,
                outputMessage);
        }
        catch (Exception ex)
        {
            throw new HttpMessageNotWritableException(
                "An error occurred writing the OAuth 2.0 Access Token Response: " + ex.getMessage(), ex);
        }
    }

    private static final ParameterizedTypeReference<Map<String, Object>> STRING_OBJECT_MAP =
        new ParameterizedTypeReference<Map<String, Object>>()
        {
        };

    /**
     * 默认使用Jackson
     */
    private static final GenericHttpMessageConverter<Object> CONVERTER = new MappingJackson2HttpMessageConverter();

    /**
     * 注入转成我们标准的格式的自定义map转换器
     */
    @Autowired
    private Converter<OAuth2AccessTokenResponse, Map<String, Object>> jwtRespMapConverter;
}
