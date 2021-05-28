package com.cloud.base.example.cloud.order.service;

import com.cloud.base.alibaba_cloud.FeignConfiguration;
import com.cloud.base.example.cloud.account.api.ExampleAccountApi;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient(name = "cloud-example-account",contextId = "exampleAccountApi",configuration = FeignConfiguration.class) // name就是服务名称  这里的contextId就是这个bean在spring容器中的name
public interface ExampleAccountService extends ExampleAccountApi {
}
