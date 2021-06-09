package com.cloud.base.member.property.feign;

import com.cloud.base.alibaba_cloud.config.FeignConfiguration;
import com.cloud.base.member.property.feign.impl.MchtApiClientFallbackFactory;
import com.cloud.base.member.merchant.api.MchtInfoApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author lh0811
 * @date 2021/5/28
 */
@FeignClient(name = "merchant-center-server",contextId = "mchtApiClient",fallbackFactory = MchtApiClientFallbackFactory.class,configuration = FeignConfiguration.class)
public interface MchtApiClient extends MchtInfoApi {

}
