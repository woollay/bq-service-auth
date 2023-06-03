package com.biuqu.boot.service.auth.impl;

import com.biuqu.boot.common.auth.model.UrlResource;
import com.biuqu.boot.dao.BizDao;
import com.biuqu.boot.service.AssemblyConfService;
import com.biuqu.service.BaseBizService;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 客户的访问资源查询服务
 *
 * @author BiuQu
 * @date 2023/2/6 22:26
 */
@Service
public class UrlResourceServiceImpl extends BaseBizService<UrlResource>
{
    @Override
    public UrlResource get(UrlResource model)
    {
        UrlResource result = new UrlResource();
        result.setAppId(model.getAppId());
        result.setUrls(Sets.newHashSet());

        List<UrlResource> batch = super.getBatch(model);
        if (!CollectionUtils.isEmpty(batch))
        {
            Map<String, String> urls = assemblyConfService.getClientUrl();
            if (!CollectionUtils.isEmpty(urls))
            {
                for (UrlResource resource : batch)
                {
                    String url = urls.get(resource.getUrlId());
                    if (!StringUtils.isEmpty(url))
                    {
                        result.getUrls().add(url);
                    }
                }
            }
        }
        return result;
    }

    @Override
    protected List<UrlResource> queryBatchByKey(String key)
    {
        UrlResource param = UrlResource.toBean(key);
        return dao.getBatch(param);
    }

    /**
     * 配置的聚合服务
     */
    @Autowired
    private AssemblyConfService assemblyConfService;

    /**
     * 注入dao
     */
    @Autowired
    private BizDao<UrlResource> dao;
}
