package com.cloud.base.core.modules.sercurity.defense.filter;

import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 用于权限验证的过滤器接口
 * 提供默认实现
 *
 * @see DefaultLhitSecurityPermsFilter
 */
public interface LhitSecurityPermsFilter extends HandlerInterceptor {

    void setUrlPatterns(String url);

    String getUrlPatterns();

}
