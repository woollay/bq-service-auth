package com.biuqu.boot.remote.auth.impl;

import com.biuqu.boot.common.auth.model.WxCode;
import com.biuqu.boot.common.auth.model.WxResult;
import com.biuqu.boot.dao.BizDao;
import com.biuqu.boot.remote.BaseRemoteService;
import com.biuqu.model.ResultCode;
import com.biuqu.utils.IdUtil;
import com.biuqu.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 微信远程调用服务
 *
 * @author BiuQu
 * @date 2023/2/12 12:05
 */
@Slf4j
@Service("wxRemoteSvc")
public class WxRemoteServiceImpl extends BaseRemoteService<WxResult, WxCode>
{
    @Override
    protected String call(WxCode model, boolean snake)
    {
        String channelUrl = this.getChannelUrl(model);
        if (StringUtils.isEmpty(channelUrl))
        {
            log.error("[{}]no channel[{}] url found.", model.getUrlId(), model.getChannelId());
            return null;
        }

        String paramJson = JsonUtil.toJson(model.toRemote(), snake);
        log.info("remote json:{}", paramJson);

        WxResult wxResult = new WxResult();
        wxResult.setOpenId(IdUtil.uuid());
        //TODO
        wxResult.setPwd("hao123");
        wxResult.setName("okBro");
        wxResult.setPath("/Users/yoyo-studio/logs/image_" + IdUtil.uuid() + ".enc");
        wxResult.setImg("Hello java with me.");
        log.info("before wx result:{}", JsonUtil.toJson(wxResult));
        wxResultDao.add(wxResult);
        log.info("after wx result:{}", JsonUtil.toJson(wxResult));

        WxResult newResult = wxResultDao.get(wxResult);
        log.info("after query wx result:{}", JsonUtil.toJson(newResult));

        ResultCode<WxResult> resultCode = ResultCode.ok(newResult);
        return JsonUtil.toJson(resultCode, snake);
    }

    @Autowired
    private BizDao<WxResult> wxResultDao;
}
