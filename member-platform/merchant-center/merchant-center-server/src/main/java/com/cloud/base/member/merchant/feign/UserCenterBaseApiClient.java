package com.cloud.base.member.merchant.feign;

import com.cloud.base.alibaba_cloud.config.FeignConfiguration;
import com.cloud.base.member.merchant.feign.impl.UserCenterBaseApiClientFallbackFactory;
import com.cloud.base.member.user.api.UserCenterBaseApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "user-center-server", contextId = "userCenterBaseApi", fallbackFactory = UserCenterBaseApiClientFallbackFactory.class, configuration = FeignConfiguration.class)
public interface UserCenterBaseApiClient extends UserCenterBaseApi {

}
