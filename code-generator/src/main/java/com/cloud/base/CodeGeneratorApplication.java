package com.cloud.base;

import com.cloud.base.generator.properties.DataBaseProperties;
import com.cloud.base.generator.properties.GeneratorProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 代码生成工具
 *
 * @author lh0811
 * @date 2021/11/6
 */
@EnableConfigurationProperties({
        GeneratorProperties.class,
        DataBaseProperties.class
})
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class CodeGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeGeneratorApplication.class, args);
    }

}
