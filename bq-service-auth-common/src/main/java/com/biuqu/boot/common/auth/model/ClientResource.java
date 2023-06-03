package com.biuqu.boot.common.auth.model;

import com.biuqu.annotation.EncryptionSecurityAnn;
import com.biuqu.annotation.IntegritySecurityAnn;
import com.biuqu.constants.Const;
import com.biuqu.model.BaseSecurity;
import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;

/**
 * 客户资源
 *
 * @author BiuQu
 * @date 2023/2/6 22:18
 */
@Data
public class ClientResource extends BaseSecurity
{
    /**
     * 根据guava key构造缓存对象
     *
     * @param key 查询key
     * @return 配置对象
     */
    public static ClientResource toBean(String key)
    {
        ClientResource client = new ClientResource();
        client.setAppId(key);
        return client;
    }

    @Override
    public String toKey()
    {
        return this.appId;
    }

    @IntegritySecurityAnn
    @Override
    public String toIntegrity()
    {
        List<Object> keys = Lists.newArrayList();
        keys.add(appId);
        keys.add(appKey);
        keys.add(appName);
        keys.add(expireTime);
        return StringUtils.join(keys, Const.SECURITY_LINK);
    }

    @Override
    public boolean isEmpty()
    {
        return StringUtils.isEmpty(appId) || StringUtils.isEmpty(appKey);
    }

    /**
     * 客户的唯一标识
     */
    private String appId;

    /**
     * 客户key
     */
    @EncryptionSecurityAnn
    private String appKey;

    /**
     * 客户名称
     */
    @EncryptionSecurityAnn
    private String appName;

    /**
     * 账号过期时间
     */
    private long expireTime;

    /**
     * 账号创建时间
     */
    private long createTime;

    /**
     * 账号状态(状态类型参见{@link com.biuqu.model.StatusType})
     */
    private int status;

    /**
     * 客户能够访问的资源列表
     */
    private Set<String> resources;
}
