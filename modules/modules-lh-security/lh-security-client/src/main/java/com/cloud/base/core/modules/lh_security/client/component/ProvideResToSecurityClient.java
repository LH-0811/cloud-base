package com.cloud.base.core.modules.lh_security.client.component;

import com.cloud.base.core.modules.lh_security.client.entity.SecurityServerAddr;

/**
 * 从应用上下文中获取到当前用户的token
 *
 * @author lh0811
 * @date 2021/5/10
 */
public interface ProvideResToSecurityClient {

    /**
     * 从服务上下文中获取token 如果是基于 spring-boot-starter-web 也就是基于springMVC的服务 可以直接从上下文中那倒request
     *
     * ServletRequestAttributes ra= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
     * HttpServletRequest request =  ra.getRequest();
     *
     * 由于该工程不依赖  spring-boot-starter-web
     * 所以需要应用自己实现该方法来获取到token
     *
     * @return
     */
    String getTokenFromApplicationContext();


    /**
     * 从应用上下文中提供安全框架服务端地址
     *
     * @return
     */
    SecurityServerAddr getServerAddrFromApplicationContext();

}
