package com.biuqu.boot.service.auth.impl;

import com.biuqu.boot.common.auth.model.ClientResource;
import com.biuqu.boot.common.auth.utils.Oauth2Builder;
import com.biuqu.boot.constants.CommonBootConst;
import com.biuqu.model.Channel;
import com.biuqu.service.BaseBizService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 客户信息查询服务(注册至oauth2-authorization-server中)
 *
 * @author BiuQu
 * @date 2023/2/6 22:07
 */
@Slf4j
@Service
public class ClientRepositoryServiceImpl implements RegisteredClientRepository
{
    @Override
    public void save(RegisteredClient registeredClient)
    {
    }

    @Override
    public RegisteredClient findById(String id)
    {
        return null;
    }

    @Override
    public RegisteredClient findByClientId(String clientId)
    {
        if (StringUtils.isEmpty(clientId))
        {
            log.error("invalid client id.");
            return null;
        }

        ClientResource clientParam = new ClientResource();
        clientParam.setAppId(clientId);
        ClientResource clientResource = clientService.get(clientParam);
        if (clientResource.isEmpty())
        {
            log.error("invalid client.");
            return null;
        }

        RegisteredClient.Builder clientBuilder = Oauth2Builder.build(clientId, jwtChannel.getConnTimeout());
        clientBuilder.clientSecret(pwdEncoder.encode(clientResource.getAppKey()));

        return clientBuilder.build();
    }

    /**
     * jwt配置
     */
    @Resource(name = CommonBootConst.JWT_CHANNEL_CONFIG)
    private Channel jwtChannel;

    /**
     * 注入带缓存的业务查询服务
     */
    @Autowired
    private BaseBizService<ClientResource> clientService;

    /**
     * 注入密码编码服务
     */
    @Autowired
    private PasswordEncoder pwdEncoder;
}
