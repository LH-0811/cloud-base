package com.cloud.base.member.user.feign;

import com.cloud.base.alibaba_cloud.config.FeignConfiguration;
import com.cloud.base.member.user.feign.impl.MchtBaseInfoApiClientFallbackFactory;
import com.cloud.base.memeber.merchant.api.MchtInfoApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author lh0811
 * @date 2021/5/28
 */
@FeignClient(name = "merchant-center-server",contextId = "mchtBaseInfoApi",fallbackFactory = MchtBaseInfoApiClientFallbackFactory.class,configuration = FeignConfiguration.class)
public interface MchtInfoApiClient extends MchtInfoApi {

}
