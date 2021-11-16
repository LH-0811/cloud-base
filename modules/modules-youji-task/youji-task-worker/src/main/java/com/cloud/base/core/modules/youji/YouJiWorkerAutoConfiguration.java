package com.cloud.base.core.modules.youji;


import com.cloud.base.core.modules.youji.properties.YouJiWorkerProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableConfigurationProperties(YouJiWorkerProperties.class)
public class YouJiWorkerAutoConfiguration {

}

