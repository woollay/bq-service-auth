package com.biuqu.boot.common.auth.model;

import lombok.Data;

/**
 * 微信远程对象
 *
 * @author BiuQu
 * @date 2023/2/12 11:33
 */
@Data
public class WxRemote
{
    /**
     * 认证码
     */
    private String code;

    /**
     * app Id
     */
    private String appId;

    /**
     * app key
     */
    private String appKey;

    /**
     * 认证类型
     */
    private String authType;
}
