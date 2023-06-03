package com.biuqu.boot.common.auth.constants;

import com.biuqu.constants.Const;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

/**
 * 认证相关的常量
 *
 * @author BiuQu
 * @date 2023/2/7 08:33
 */
public final class AuthConst
{
    /**
     * 来源类型
     */
    public static final String JWT_SOURCE_TYPE = "source_type";

    /**
     * jwt类型(业务token还是刷新token)
     */
    public static final String JWT_TYPE = "jwt_type";

    /**
     * 业务token
     */
    public static final String JWT_TYPE_TOKEN = Const.JWT_TYPE_TOKEN;

    /**
     * 刷新token
     */
    public static final String JWT_TYPE_REFRESH = Const.JWT_TYPE_REFRESH;

    /**
     * 资源列表
     */
    public static final String JWT_RESOURCES = "resources";

    /**
     * Jwt访问范围
     */
    public static final String JWT_SCOPE = "read";

    /**
     * 客户唯一标识
     */
    public static final String CLIENT_ID = "client_id";

    /**
     * 自定义的token刷新接口
     */
    public static final AuthorizationGrantType REFRESH_GRANT_TYPE = new AuthorizationGrantType("jwt_refresh");

    private AuthConst()
    {
    }
}
