package com.cloud.base.common.youji.cronjob.worker;


import com.cloud.base.common.youji.cronjob.worker.properties.Youji2WorkerProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableConfigurationProperties(Youji2WorkerProperties.class)
public class Youji2ServerAutoConfiguration {

}

