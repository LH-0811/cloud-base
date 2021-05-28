package com.cloud.base.member.merchant.feign;

import com.cloud.base.alibaba_cloud.FeignConfiguration;
import com.cloud.base.member.user.api.UserCenterBaseApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "user-center-server",contextId = "userCenterBaseApi",configuration = FeignConfiguration.class)
public interface UserCenterBaseApiClient extends UserCenterBaseApi {

}
