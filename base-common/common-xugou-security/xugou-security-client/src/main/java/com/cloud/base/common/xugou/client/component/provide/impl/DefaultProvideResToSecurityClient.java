package com.cloud.base.common.xugou.client.component.provide.impl;

import com.cloud.base.common.xugou.client.component.provide.ProvideResToSecurityClient;
import com.cloud.base.common.xugou.core.model.properties.XuGouSecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lh0811
 * @date 2021/5/25
 */
@Slf4j
public class DefaultProvideResToSecurityClient implements ProvideResToSecurityClient {

    @Autowired
    private XuGouSecurityProperties xuGouSecurityProperties;

    @Override
    public String getTokenFromApplicationContext() {
        ServletRequestAttributes ra= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request =  ra.getRequest();
        String header = request.getHeader(xuGouSecurityProperties.getTokenKey());
        return header;
    }

}
