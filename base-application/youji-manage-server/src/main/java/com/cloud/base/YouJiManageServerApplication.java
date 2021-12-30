package com.cloud.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 代码生成工具
 *
 * @author lh0811
 * @date 2021/11/6
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class YouJiManageServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(YouJiManageServerApplication.class, args);
    }

}
