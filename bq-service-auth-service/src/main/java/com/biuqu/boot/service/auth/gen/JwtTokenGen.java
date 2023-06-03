package com.biuqu.boot.service.auth.gen;

import com.biuqu.boot.common.auth.constants.AuthConst;
import com.biuqu.boot.common.auth.utils.JwtGenUtil;
import com.biuqu.boot.constants.CommonBootConst;
import com.biuqu.jwt.JwkMgr;
import com.biuqu.model.Channel;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * JwtToken生成器
 *
 * @author BiuQu
 * @date 2023/2/20 14:04
 */
@Slf4j
@Component
public class JwtTokenGen
{
    /**
     * 生成新的jwt
     *
     * @return 新Jwt
     */
    public String genJwt()
    {
        Map<String, Object> claims = JwtGenUtil.buildClaims(jwtChannel.getConnTimeout(), AuthConst.JWT_TYPE_TOKEN);
        return JwtGenUtil.genJwt(claims, jwkMgr.getJwk());
    }

    /**
     * 生成新的jwt
     *
     * @return 新Jwt
     */
    public String genRefreshJwt()
    {
        Map<String, Object> claims = JwtGenUtil.buildClaims(jwtChannel.getTimeout(), AuthConst.JWT_TYPE_REFRESH);
        claims.remove(AuthConst.JWT_RESOURCES);
        return JwtGenUtil.genJwt(claims, jwkMgr.getJwk());
    }

    /**
     * 基于当前的jwt对象，生成新的属性Jwt
     *
     * @param jwt 当前的Jwt
     * @return 新Jwt
     */
    public String genRefreshJwt(String jwt)
    {
        try
        {
            return genRefreshJwt(SignedJWT.parse(jwt));
        }
        catch (Exception e)
        {
            log.error("failed to gen refresh token.", e);
        }
        return null;
    }

    /**
     * 基于当前的jwt对象，生成新的属性Jwt
     *
     * @param jwt 当前的Jwt
     * @return 新Jwt
     */
    public String genRefreshJwt(SignedJWT jwt)
    {
        Map<String, Object> claims = JwtGenUtil.buildClaims(jwt, jwtChannel.getTimeout(), AuthConst.JWT_TYPE_REFRESH);
        claims.remove(AuthConst.JWT_RESOURCES);
        return JwtGenUtil.genJwt(claims, jwkMgr.getJwk());
    }

    /**
     * jwt配置
     */
    @Resource(name = CommonBootConst.JWT_CHANNEL_CONFIG)
    private Channel jwtChannel;

    /**
     * jwk管理器
     */
    @Autowired
    private JwkMgr jwkMgr;
}
