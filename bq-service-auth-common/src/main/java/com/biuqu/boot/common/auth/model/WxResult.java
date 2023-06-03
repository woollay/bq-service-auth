package com.biuqu.boot.common.auth.model;

import com.biuqu.annotation.*;
import com.biuqu.constants.Const;
import com.biuqu.model.BaseSecurity;
import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 微信返回的结果对象
 *
 * @author BiuQu
 * @date 2023/2/12 11:29
 */
@Data
public class WxResult extends BaseSecurity
{
    /**
     * 认证后的openId
     */
    private String openId;

    /**
     * 文件路径
     */
    @FileSecurityAnn(value = "imageBase64")
    private String path;

    /**
     * 头像base64
     */
    @FileDataSecurityAnn(value = "imageBase64")
    private String img;

    /**
     * 用户名
     */
    @EncryptionSecurityAnn
    private String name;

    /**
     * 秘钥
     */
    @HashSecurityAnn
    private String pwd;

    @IntegritySecurityAnn
    @Override
    public String toIntegrity()
    {
        List<String> keys = Lists.newArrayList();
        keys.add(openId);
        keys.add(path);
        keys.add(name);
        keys.add(pwd);
        return StringUtils.join(keys, Const.SECURITY_LINK);
    }
}
