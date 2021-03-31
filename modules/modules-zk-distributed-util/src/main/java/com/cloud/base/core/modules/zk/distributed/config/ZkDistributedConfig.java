package com.cloud.base.core.modules.zk.distributed.config;

import com.cloud.base.core.modules.zk.distributed.properites.ZkDistributedProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author lh0811
 * @date 2021/3/31
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(ZkDistributedProperties.class)
public class ZkDistributedConfig {


}
