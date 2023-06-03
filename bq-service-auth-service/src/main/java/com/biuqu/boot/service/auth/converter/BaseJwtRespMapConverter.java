package com.biuqu.boot.service.auth.converter;

import com.biuqu.model.JwtResult;
import com.biuqu.model.ResultCode;
import com.biuqu.utils.JsonUtil;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * response响应的map映射对象
 * <p>
 * 抽象类，只是改了返回的结构
 *
 * @author BiuQu
 * @date 2023/2/20 09:43
 */
public abstract class BaseJwtRespMapConverter implements Converter<OAuth2AccessTokenResponse, Map<String, Object>>
{
    @Override
    public Map<String, Object> convert(OAuth2AccessTokenResponse tokenResponse)
    {
        Map<String, Object> parameters = Maps.newHashMap();
        parameters.put(OAuth2ParameterNames.ACCESS_TOKEN, tokenResponse.getAccessToken().getTokenValue());
        parameters.put(OAuth2ParameterNames.TOKEN_TYPE, tokenResponse.getAccessToken().getTokenType().getValue());
        parameters.put(OAuth2ParameterNames.EXPIRES_IN, getExpiresIn(tokenResponse));
        if (!CollectionUtils.isEmpty(tokenResponse.getAccessToken().getScopes()))
        {
            parameters.put(OAuth2ParameterNames.SCOPE,
                StringUtils.collectionToDelimitedString(tokenResponse.getAccessToken().getScopes(), " "));
        }
        if (tokenResponse.getRefreshToken() != null)
        {
            parameters.put(OAuth2ParameterNames.REFRESH_TOKEN, tokenResponse.getRefreshToken().getTokenValue());
        }
        if (!CollectionUtils.isEmpty(tokenResponse.getAdditionalParameters()))
        {
            for (Map.Entry<String, Object> entry : tokenResponse.getAdditionalParameters().entrySet())
            {
                parameters.put(entry.getKey(), entry.getValue());
            }
        }

        //把带下划线的标准Jwt参数转换成json
        ResultCode<JwtResult> jwtResult = toJwtResult(parameters);
        return JsonUtil.toMap(JsonUtil.toJson(jwtResult, snakeCase), String.class, Object.class);
    }

    /**
     * 把map转成标准的对象
     *
     * @param parameters map参数
     * @return 标准的结果对象
     */
    protected ResultCode<JwtResult> toJwtResult(Map<String, Object> parameters)
    {
        //把带下划线的标准Jwt参数转换成json
        String json = JsonUtil.toJson(parameters, true);
        JwtResult jwtResult = JsonUtil.toObject(json, JwtResult.class, true);

        ResultCode<JwtResult> resultCode = ResultCode.ok(jwtResult);
        return resultCode;
    }

    private static long getExpiresIn(OAuth2AccessTokenResponse tokenResponse)
    {
        if (tokenResponse.getAccessToken().getExpiresAt() != null)
        {
            return ChronoUnit.SECONDS.between(Instant.now(), tokenResponse.getAccessToken().getExpiresAt());
        }
        return -1;
    }

    /**
     * 是否驼峰式json(默认支持)
     */
    @Value("${bq.json.snake-case:true}")
    private boolean snakeCase;
}
