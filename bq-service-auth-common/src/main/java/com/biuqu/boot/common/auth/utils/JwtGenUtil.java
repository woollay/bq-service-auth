package com.biuqu.boot.common.auth.utils;

import com.biuqu.boot.common.auth.constants.AuthConst;
import com.biuqu.model.JwtToken;
import com.biuqu.utils.IdUtil;
import com.google.common.collect.Maps;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.factories.DefaultJWSSignerFactory;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.produce.JWSSignerFactory;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.jwt.JwtClaimNames;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Jwt构建器
 *
 * @author BiuQu
 * @date 2023/2/20 14:15
 */
@Slf4j
public final class JwtGenUtil
{
    /**
     * 生成JwtToken base64字符串
     *
     * @param claims jwt body集合
     * @param jwk    秘钥
     * @return JwtToken base64字符串
     */
    public static String genJwt(Map<String, Object> claims, JWK jwk)
    {
        try
        {
            JWTClaimsSet jwtClaimsSet = JWTClaimsSet.parse(claims);
            return genJwt(jwtClaimsSet, jwk);
        }
        catch (Exception e)
        {
            log.error("failed to gen jwt token.", e);
        }
        return null;
    }

    /**
     * 生成JwtToken base64字符串
     *
     * @param jwtClaimsSet jwt body集合
     * @param jwk          秘钥
     * @return JwtToken base64字符串
     */
    public static String genJwt(JWTClaimsSet jwtClaimsSet, JWK jwk)
    {
        try
        {
            JWSSigner jwsSigner = SIGNER_FACTORY.createJWSSigner(jwk);
            SignedJWT signedJwt = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), jwtClaimsSet);
            signedJwt.sign(jwsSigner);
            return signedJwt.serialize();
        }
        catch (Exception e)
        {
            log.error("failed to gen jwt token.", e);
        }
        return null;
    }

    /**
     * 基于现有的Jwt构建新的Jwt claims
     *
     * @param jwtToken jwt参数
     * @return 新的Jwt claims
     */
    public static Map<String, Object> buildClaims(JwtToken jwtToken)
    {
        Map<String, Object> claims = buildClaims(jwtToken.getExp(), jwtToken.getJwtType());
        claims.put(JwtClaimNames.SUB, jwtToken.getSub());
        claims.put(JwtClaimNames.AUD, jwtToken.getAud());
        claims.put(JwtClaimNames.ISS, StringUtils.EMPTY);
        claims.put(AuthConst.JWT_RESOURCES, jwtToken.getResources());
        return claims;
    }

    /**
     * 基于现有的Jwt构建新的Jwt claims
     *
     * @param signedJwt 签名后的jwt
     * @param expire    有效时长(s)
     * @param jwtType   jwt类型(token/refresh)
     * @return 新的Jwt claims
     */
    public static Map<String, Object> buildClaims(SignedJWT signedJwt, long expire, String jwtType)
    {
        Map<String, Object> claims = Maps.newHashMap();
        try
        {
            claims.putAll(signedJwt.getJWTClaimsSet().getClaims());
            Map<String, Object> newClaims = buildClaims(expire, jwtType);
            claims.putAll(newClaims);
        }
        catch (Exception e)
        {
            log.error("failed to gen jwt token.", e);
        }
        return claims;
    }

    /**
     * 基于现有的Jwt构建新的Jwt claims
     *
     * @param expire  有效时长(s)
     * @param jwtType jwt类型(token/refresh)
     * @return 新的Jwt claims
     */
    public static Map<String, Object> buildClaims(long expire, String jwtType)
    {
        Map<String, Object> claims = Maps.newHashMap();
        long validTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        long expireTime = validTime + expire;
        claims.put(JwtClaimNames.IAT, validTime);
        claims.put(JwtClaimNames.NBF, validTime);
        claims.put(JwtClaimNames.EXP, expireTime);
        claims.put(JwtClaimNames.JTI, IdUtil.uuid());
        claims.put(OAuth2ParameterNames.TOKEN_TYPE, OAuth2AccessToken.TokenType.BEARER.getValue());
        claims.put(AuthConst.JWT_TYPE, jwtType);
        return claims;
    }

    private JwtGenUtil()
    {
    }

    /**
     * 默认的签名工厂
     */
    private static final JWSSignerFactory SIGNER_FACTORY = new DefaultJWSSignerFactory();
}
