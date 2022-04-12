package com.cloud.base.common.xugou.client.component.provide;

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

}
