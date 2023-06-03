package com.biuqu.boot.common.auth.model;

import com.biuqu.model.BaseSecurity;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

/**
 * 客户可以访问的资源列表
 *
 * @author BiuQu
 * @date 2023/2/6 22:29
 */
@Data
public class UrlResource extends BaseSecurity
{
    /**
     * 根据guava key构造缓存对象
     *
     * @param key 查询key
     * @return 配置对象
     */
    public static UrlResource toBean(String key)
    {
        UrlResource resource = new UrlResource();
        resource.setAppId(key);
        return resource;
    }

    @Override
    public String toKey()
    {
        return this.appId;
    }

    @Override
    public String toBatchKey()
    {
        return this.toKey();
    }

    @Override
    public boolean isEmpty()
    {
        return StringUtils.isEmpty(this.urlId);
    }

    /**
     * 客户id
     */
    private String appId;

    /**
     * url id
     */
    private String urlId;

    /**
     * url状态(状态类型参见{@link com.biuqu.model.StatusType})
     */
    private int status;

    /**
     * 所有url资源
     */
    private Set<String> urls;
}
