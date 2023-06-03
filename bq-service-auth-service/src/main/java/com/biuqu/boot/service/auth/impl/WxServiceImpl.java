package com.biuqu.boot.service.auth.impl;

import com.biuqu.boot.common.auth.model.WxCode;
import com.biuqu.service.BaseBizService;
import org.springframework.stereotype.Service;

/**
 * 微信认证服务
 *
 * @author BiuQu
 * @date 2023/2/12 11:37
 */
@Service
public class WxServiceImpl extends BaseBizService<WxCode>
{
    @Override
    public WxCode get(WxCode model)
    {
        return model;
    }
}
