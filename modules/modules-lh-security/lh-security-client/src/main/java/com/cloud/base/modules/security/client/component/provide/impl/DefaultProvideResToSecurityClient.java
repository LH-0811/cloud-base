package com.cloud.base.modules.security.client.component.provide.impl;

import com.cloud.base.common.exception.CommonException;
import com.cloud.base.common.response.ServerResponse;
import com.cloud.base.modules.security.client.component.provide.ProvideResToSecurityClient;
import com.cloud.base.modules.security.core.entity.SecurityServerAddr;
import com.cloud.base.modules.security.core.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author lh0811
 * @date 2021/5/25
 */
@Slf4j
public class DefaultProvideResToSecurityClient implements ProvideResToSecurityClient {
    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Value("${server.port}")
    private Integer port;

    @Override
    public String getTokenFromApplicationContext() {
        log.info("****************从上下文中获取token-{}****************");
        ServletRequestAttributes ra= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request =  ra.getRequest();
        String header = request.getHeader(securityProperties.getTokenKey());
        return header;
    }

    @Override
    public SecurityServerAddr getServerAddrFromApplicationContext() throws CommonException {
        if (securityProperties.getUseCloud()){
            List<ServiceInstance> instances = discoveryClient.getInstances(securityProperties.getServerName());
            if (CollectionUtils.isEmpty(instances)){
                throw CommonException.create(ServerResponse.createByError("未获取到用户中心服务示例"));
            }
            ServiceInstance serviceInstance = instances.get(RandomUtils.nextInt(0, instances.size()));
            return new SecurityServerAddr(Boolean.TRUE,serviceInstance.getHost(),serviceInstance.getPort());
        }else {
            return new SecurityServerAddr(Boolean.FALSE,"127.0.0.1",port);
        }
    }
}
