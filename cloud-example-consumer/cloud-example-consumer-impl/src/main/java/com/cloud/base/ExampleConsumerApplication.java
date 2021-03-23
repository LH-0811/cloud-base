package com.cloud.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class ExampleConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleConsumerApplication.class, args);
    }

}
