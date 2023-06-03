package com.biuqu.boot.service.auth.impl;

import com.biuqu.boot.common.auth.model.ClientResource;
import com.biuqu.boot.common.auth.model.UrlResource;
import com.biuqu.boot.dao.BizDao;
import com.biuqu.service.BaseBizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 客户资源查询服务
 *
 * @author BiuQu
 * @date 2023/2/6 22:26
 */
@Service
public class ClientResourceServiceImpl extends BaseBizService<ClientResource>
{
    @Override
    public ClientResource get(ClientResource model)
    {
        ClientResource client = super.get(model);
        if (!client.isEmpty())
        {
            UrlResource urlParam = new UrlResource();
            urlParam.setAppId(model.getAppId());
            UrlResource urlResource = urlService.get(urlParam);
            client.setResources(urlResource.getUrls());
        }
        return client;
    }

    @Override
    protected ClientResource queryByKey(String key)
    {
        ClientResource client = ClientResource.toBean(key);
        return dao.get(client);
    }

    /**
     * 注入url服务
     */
    @Autowired
    private BaseBizService<UrlResource> urlService;

    /**
     * 注入dao
     */
    @Autowired
    private BizDao<ClientResource> dao;
}
