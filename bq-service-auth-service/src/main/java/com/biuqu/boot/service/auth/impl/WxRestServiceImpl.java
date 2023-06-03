package com.biuqu.boot.service.auth.impl;

import com.biuqu.boot.common.auth.model.WxCode;
import com.biuqu.boot.common.auth.model.WxResult;
import com.biuqu.boot.remote.RemoteService;
import com.biuqu.boot.service.BaseRestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 微信Rest服务实现
 *
 * @author BiuQu
 * @date 2023/2/12 14:04
 */
@Service("wxRestSvc")
public class WxRestServiceImpl extends BaseRestService<WxResult, WxCode>
{
    @Override
    protected RemoteService<WxResult, WxCode> getRemoteService()
    {
        return remoteService;
    }

    /**
     * 替换掉默认的remote实现
     */
    @Resource(name = "wxRemoteSvc")
    private RemoteService<WxResult, WxCode> remoteService;
}
