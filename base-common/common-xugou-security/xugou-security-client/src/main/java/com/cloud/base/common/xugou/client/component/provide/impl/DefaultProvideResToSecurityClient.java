package com.cloud.base.common.xugou.client.component.provide.impl;

import com.cloud.base.common.core.exception.CommonException;
import com.cloud.base.common.core.response.ServerResponse;
import com.cloud.base.common.xugou.client.component.provide.ProvideResToSecurityClient;
import com.cloud.base.common.xugou.core.model.entity.SecurityServerAddr;
import com.cloud.base.common.xugou.core.model.properties.XuGouSecurityProperties;
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
    private XuGouSecurityProperties xuGouSecurityProperties;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Value("${server.port}")
    private Integer port;

    @Override
    public String getTokenFromApplicationContext() {
//        log.info("****************从上下文中获取token-{}****************");
        ServletRequestAttributes ra= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request =  ra.getRequest();
        String header = request.getHeader(xuGouSecurityProperties.getTokenKey());
        return header;
    }

    @Override
    public SecurityServerAddr getServerAddrFromApplicationContext() throws CommonException {
        if (xuGouSecurityProperties.getUseCloud()){
            List<ServiceInstance> instances = discoveryClient.getInstances(xuGouSecurityProperties.getServerName());
            if (CollectionUtils.isEmpty(instances)){
                throw CommonException.create(ServerResponse.createByError("未获取到用户中心服务实例"));
            }
            ServiceInstance serviceInstance = instances.get(RandomUtils.nextInt(0, instances.size()));
            return new SecurityServerAddr(Boolean.TRUE,serviceInstance.getHost(),serviceInstance.getPort());
        }else {
            return new SecurityServerAddr(Boolean.FALSE,"localhost",port);
        }
    }
}
