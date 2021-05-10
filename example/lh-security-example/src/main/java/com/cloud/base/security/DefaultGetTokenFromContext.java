package com.cloud.base.security;

import com.cloud.base.core.modules.lh_security.client.component.GetTokenFromContext;
import com.cloud.base.core.modules.lh_security.client.properties.SecurityClientProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lh0811
 * @date 2021/5/10
 */
@Component
public class DefaultGetTokenFromContext implements GetTokenFromContext {
    @Autowired
    private SecurityClientProperties securityClientProperties;

    @Override
    public String getToken() {
        ServletRequestAttributes ra= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request =  ra.getRequest();
        String header = request.getHeader(securityClientProperties.getTokenKey());
        return header;
    }
}
