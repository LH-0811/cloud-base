package com.cloud.base.core.modules.es;

import com.cloud.base.core.modules.es.properties.EsProperites;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lh0811
 * @date 2021/4/10
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(EsProperites.class)
public class EsConfig {
}
