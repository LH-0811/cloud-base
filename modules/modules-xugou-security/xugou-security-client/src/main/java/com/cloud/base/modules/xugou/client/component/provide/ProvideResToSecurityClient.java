package com.cloud.base.modules.xugou.client.component.provide;

import com.cloud.base.common.exception.CommonException;
import com.cloud.base.modules.xugou.core.model.entity.SecurityServerAddr;

/**
 * 从应用上下文中获取到当前用户的token
 *
 * @author lh0811
 * @date 2021/5/10
 */
public interface ProvideResToSecurityClient {

    /**
     * 从请求上下文中获取到用户token
     *
     * @return
     * @throws Exception
     */
    String getTokenFromApplicationContext() throws Exception;

    /**
     * 从应用上下文中提供安全框架服务端地址
     *
     * @return
     */
    SecurityServerAddr getServerAddrFromApplicationContext() throws CommonException;

}
