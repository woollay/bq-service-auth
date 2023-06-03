package com.biuqu.boot.dao.auth;

import com.biuqu.boot.common.auth.model.ClientResource;
import com.biuqu.boot.dao.BizDao;

/**
 * 客户信息dao
 * <p>
 * (通过加密机做加密和完整性保护)
 *
 * @author BiuQu
 * @date 2023/2/6 22:35
 */
public interface ClientResourceSecDao extends BizDao<ClientResource>
{
}
