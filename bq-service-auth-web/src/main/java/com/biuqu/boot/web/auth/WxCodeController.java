package com.biuqu.boot.web.auth;

import com.biuqu.boot.common.auth.model.WxCode;
import com.biuqu.boot.common.auth.model.WxInner;
import com.biuqu.boot.common.auth.model.WxResult;
import com.biuqu.boot.service.RestService;
import com.biuqu.boot.web.BaseBizController;
import com.biuqu.log.annotation.ClientLogAnn;
import com.biuqu.model.ResultCode;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 微信Code Rest服务
 *
 * @author BiuQu
 * @date 2023/2/12 11:52
 */
@RestController
public class WxCodeController extends BaseBizController<WxResult, WxCode, WxInner>
{
    @ClientLogAnn
    @PostMapping("/auth/wx")
    @Override
    public ResultCode<WxResult> execute(@RequestBody WxInner inner)
    {
        return super.execute(inner);
    }

    @Override
    protected RestService<WxResult, WxCode> getService()
    {
        return restService;
    }

    /**
     * 替换掉默认的rest实现
     */
    @Resource(name = "wxRestSvc")
    private RestService<WxResult, WxCode> restService;
}
