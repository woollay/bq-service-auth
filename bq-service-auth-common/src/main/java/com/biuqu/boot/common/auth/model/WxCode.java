package com.biuqu.boot.common.auth.model;

import com.biuqu.model.BaseBiz;
import com.biuqu.model.ResultCode;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;

/**
 * 微信认证码
 *
 * @author BiuQu
 * @date 2023/2/12 11:30
 */
@Data
public class WxCode extends BaseBiz<WxResult>
{
    @Override
    public Object toRemote()
    {
        WxRemote model = new WxRemote();
        model.setCode(this.code);
        model.setAppId("app001");
        model.setAppKey("pwd001");
        model.setAuthType("oauth2");
        return model;
    }

    @Override
    public TypeReference<ResultCode<WxResult>> toTypeRef()
    {
        return new TypeReference<ResultCode<WxResult>>()
        {
        };
    }

    /**
     * 微信认证码
     */
    private String code;
}
