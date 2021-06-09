package com.cloud.base.member.merchant.feign;

import com.cloud.base.alibaba_cloud.config.FeignConfiguration;
import com.cloud.base.member.merchant.feign.impl.UserCenterCurrentUserApiClientFallbackFactory;
import com.cloud.base.member.user.api.UserCenterCurrentUserApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "user-center-server", contextId = "userCenterCurrentUserApi", fallbackFactory = UserCenterCurrentUserApiClientFallbackFactory.class, configuration = FeignConfiguration.class)
public interface UserCenterCurrentUserApiClient extends UserCenterCurrentUserApi {

}
