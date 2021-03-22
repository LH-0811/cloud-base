package com.cloud.base.core.modules.sercurity.defense.adapter;

import java.util.List;

/**
 * 资源保护 adapter
 * 该适配器 用于指定那些uri指向的资源是需要保护的
 * 只有拥有相关权限的用户才可以访问和操作
 * <p>
 * 提供了默认的资源保护适配器
 * 其默认实现 指定了 /** 也就是所有的资源都是要保护的
 *
 */
public interface LhitSecurityResourceProtectAdapter {


    /**
     * 获取到所有需要保护的资源的url路径
     *
     * @return
     */
    List<String> getProtectUrlPatterns();

}
