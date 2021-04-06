package com.cloud.base.core.modules.zk.distributed;

import com.cloud.base.core.modules.zk.distributed.properites.ZkDistributedProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lh0811
 * @date 2021/3/31
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(ZkDistributedProperties.class)
public class ZkDistributedConfig {


}
