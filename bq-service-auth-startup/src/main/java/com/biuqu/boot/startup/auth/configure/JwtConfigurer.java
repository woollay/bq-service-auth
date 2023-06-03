package com.biuqu.boot.startup.auth.configure;

import com.biuqu.boot.constants.CommonBootConst;
import com.biuqu.jwt.JwkMgr;
import com.biuqu.jwt.JwtMgr;
import com.biuqu.model.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * JwtToken配置服务
 *
 * @author BiuQu
 * @date 2023/2/4 15:56
 */
@Slf4j
@Configuration
public class JwtConfigurer
{
    @Bean(CommonBootConst.JWT_CHANNEL_CONFIG)
    @ConfigurationProperties(prefix = "bq.channels.jwt")
    public Channel jwtChannel()
    {
        return new Channel();
    }

    @Bean
    public JwkMgr jwkMgr(@Qualifier(CommonBootConst.JWT_CHANNEL_CONFIG) Channel channel)
    {
        return new JwkMgr(channel);
    }

    @Bean
    public JwtMgr jwtMgr(JwkMgr jwkMgr)
    {
        return new JwtMgr(jwkMgr);
    }
}
