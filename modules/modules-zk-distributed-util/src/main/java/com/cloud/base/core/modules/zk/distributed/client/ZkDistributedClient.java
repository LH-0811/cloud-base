package com.cloud.base.core.modules.zk.distributed.client;

import com.cloud.base.core.modules.zk.distributed.properites.ZkDistributedProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author lh0811
 * @date 2021/3/31
 */
@Slf4j
@Component
public class ZkDistributedClient {

    @Autowired
    private ZkDistributedProperties properites;

    // 利用spring容器的单例特性创建单例的客户端对象
    private static CuratorFramework client = null;

    @PostConstruct
    public void init() {
        // 初始化客户端并开启连接
        openZkConnection();
        log.info("Zookeeper  Curator 初始化完成!!!");
    }

    // 开启zookeeper连接客户端
    public void openZkConnection(){
        if (client == null){
            //重试策略
            RetryPolicy policy = new ExponentialBackoffRetry(properites.getBaseSleepTimeMs(), properites.getMaxRetries());
            //通过工厂创建Curator
            CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();

            builder.connectString(properites.getServer())
                    .connectionTimeoutMs(properites.getConnectionTimeoutMs())
                    .sessionTimeoutMs(properites.getSessionTimeoutMs())
                    .retryPolicy(policy).build();
            if (StringUtils.isNotEmpty(properites.getNamespace())) {
                builder.namespace(properites.getNamespace());
            }
            if (StringUtils.isNotEmpty(properites.getDigest())) {
                builder.authorization("digest", properites.getDigest().getBytes());
            }
            client = builder.build();
        }
        if (client.getState().compareTo(CuratorFrameworkState.STARTED)!=0){
            //开启连接
            client.start();
        }
    }

    // 关闭对外的set方法
    private void setClient(CuratorFramework client) { }

    // 公开get方法
    public CuratorFramework getClient() {
        return client;
    }

    // 公开客户端关闭方法
    public void closeClient() {
        if (client != null) {
            if (client.getState().compareTo(CuratorFrameworkState.STOPPED)!=0){
                client.close();
                client = null;
            }
        }
    }

}
