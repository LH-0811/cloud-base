package com.cloud.base.example.consumer.feign;

import com.cloud.base.example.provider.api.ExampleProvdierApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author lh0811
 * @date 2021/3/23
 */
@FeignClient(name = "cloud-example-provider",contextId = "provdierApi") // name就是服务名称  这里的contextId就是这个bean在spring容器中的name
public interface ExampleProvdierApiClient extends ExampleProvdierApi {
}
