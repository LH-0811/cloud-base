package com.cloud.base.core.modules.lh_security.client.component;

import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.modules.lh_security.client.entity.SecurityServerAddr;

/**
 * 从应用上下文中获取到当前用户的token
 *
 * @author lh0811
 * @date 2021/5/10
 */
public interface ProvideResToSecurityClient {

    public String getTokenFromApplicationContext() throws Exception;

    /**
     * 从应用上下文中提供安全框架服务端地址
     *
     * @return
     */
    SecurityServerAddr getServerAddrFromApplicationContext() throws CommonException;

}
