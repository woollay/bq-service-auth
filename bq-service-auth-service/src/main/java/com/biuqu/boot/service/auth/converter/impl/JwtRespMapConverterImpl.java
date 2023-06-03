package com.biuqu.boot.service.auth.converter.impl;

import com.biuqu.boot.common.auth.constants.AuthConst;
import com.biuqu.boot.service.auth.converter.BaseJwtRespMapConverter;
import com.biuqu.boot.service.auth.gen.JwtTokenGen;
import com.biuqu.model.JwtResult;
import com.biuqu.model.JwtToken;
import com.biuqu.model.ResultCode;
import com.biuqu.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * response响应的map映射对象
 *
 * @author BiuQu
 * @date 2023/2/20 09:43
 */
@Slf4j
@Component
public class JwtRespMapConverterImpl extends BaseJwtRespMapConverter
{
    @Override
    protected ResultCode<JwtResult> toJwtResult(Map<String, Object> parameters)
    {
        String jwt = parameters.get(OAuth2ParameterNames.ACCESS_TOKEN).toString();

        //1.添加扩展字段
        JwtToken jwtToken = JwtUtil.getJwtToken(jwt);
        parameters.put(AuthConst.JWT_RESOURCES, jwtToken.getResources());
        parameters.put(AuthConst.CLIENT_ID, jwtToken.toClientId());
        parameters.put(JwtClaimNames.JTI, jwtToken.getJti());

        //2.生成刷新token
        String refreshJwt = jwtTokenGen.genRefreshJwt(jwt);
        parameters.put(OAuth2ParameterNames.REFRESH_TOKEN, refreshJwt);

        return super.toJwtResult(parameters);
    }

    /**
     * jwt token生成器
     */
    @Autowired
    private JwtTokenGen jwtTokenGen;
}
