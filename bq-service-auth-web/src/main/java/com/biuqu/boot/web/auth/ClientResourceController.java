package com.biuqu.boot.web.auth;

import com.biuqu.boot.common.auth.model.ClientResource;
import com.biuqu.boot.dao.BizDao;
import com.biuqu.model.ResultCode;
import com.biuqu.model.StatusType;
import com.biuqu.utils.IdUtil;
import com.biuqu.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 客户资源Rest服务(仅测试使用)
 *
 * @author BiuQu
 * @date 2023/2/12 11:52
 */
@Slf4j
@RestController
public class ClientResourceController
{
    @PostMapping("/auth/user/add")
    public ResultCode<ClientResource> execute(@RequestBody ClientResource client)
    {
        client.setId(IdUtil.uuid());
        client.setCreateTime(System.currentTimeMillis());
        client.setExpireTime(client.getCreateTime() + TimeUnit.DAYS.toMillis(365));
        client.setStatus(StatusType.ENABLE.getStatus());

        log.info("current user:{}", JsonUtil.toJson(client));
        int code = dao.add(client);
        log.info("add user result:{}", code);

        ClientResource result = dao.get(client);
        log.info("from db user:{}", JsonUtil.toJson(result));
        result.setAppKey(null);
        return ResultCode.ok(result);
    }

    @PostMapping("/auth/user/get")
    public ResultCode<ClientResource> get(@RequestBody ClientResource client)
    {
        log.info("current user:{}", JsonUtil.toJson(client));
        ClientResource result = dao.get(client);
        log.info("from db user:{}", JsonUtil.toJson(result));
        return ResultCode.ok(result);
    }

    /**
     * dao操作
     */
    @Autowired
    private BizDao<ClientResource> dao;
}
