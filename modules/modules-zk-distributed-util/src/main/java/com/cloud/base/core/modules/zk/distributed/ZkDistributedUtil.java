package com.cloud.base.core.modules.zk.distributed;

import com.cloud.base.core.modules.zk.distributed.properites.ZkDistributedProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
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
public class ZkDistributedUtil {

    @Autowired
    private ZkDistributedProperties properites;

    private static CuratorFramework client = null;

    @PostConstruct
    public void init() {
        //重试策略
        RetryPolicy policy = new ExponentialBackoffRetry(properites.getBaseSleepTimeMs(), properites.getMaxRetries());
        //通过工厂创建Curator
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();

        builder.connectString(properites.getServer())
                .connectionTimeoutMs(properites.getConnectionTimeoutMs())
                .sessionTimeoutMs(properites.getSessionTimeoutMs())
                .retryPolicy(policy).build();
        if (StringUtils.isNotEmpty(properites.getDigest())) {
            builder.authorization("digest", properites.getDigest().getBytes());
        }
        client = builder.build();
        //开启连接
        client.start();
        log.info("Zookeeper  Curator 初始化完成!!!");
    }

    public static CuratorFramework getClient() {
        return client;
    }

    public static void closeClient() {
        if (client != null) {
            client.close();
        }
    }

}
