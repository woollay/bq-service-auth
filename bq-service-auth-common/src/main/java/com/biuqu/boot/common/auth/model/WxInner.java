package com.biuqu.boot.common.auth.model;

import com.biuqu.model.BaseBizInner;
import lombok.Data;

/**
 * 微信入参
 *
 * @author BiuQu
 * @date 2023/2/12 11:53
 */
@Data
public class WxInner extends BaseBizInner<WxCode>
{
    @Override
    protected WxCode genModel()
    {
        WxCode wxCode = new WxCode();
        wxCode.setCode(this.code);
        return wxCode;
    }

    private String code;
}
