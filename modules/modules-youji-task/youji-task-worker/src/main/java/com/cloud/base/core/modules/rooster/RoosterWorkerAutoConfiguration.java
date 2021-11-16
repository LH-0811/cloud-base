package com.cloud.base.core.modules.rooster;


import com.cloud.base.core.modules.rooster.properties.RoosterWorkerProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableConfigurationProperties(RoosterWorkerProperties.class)
public class RoosterWorkerAutoConfiguration {

}

