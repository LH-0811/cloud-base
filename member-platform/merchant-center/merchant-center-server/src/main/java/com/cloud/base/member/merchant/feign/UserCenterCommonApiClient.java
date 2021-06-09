package com.cloud.base.member.merchant.feign;

import com.cloud.base.alibaba_cloud.config.FeignConfiguration;
import com.cloud.base.member.merchant.feign.impl.UserCenterCommonApiClientFallbackFactory;
import com.cloud.base.member.user.api.UserCenterCommonApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author lh0811
 * @date 2021/6/9
 */
@FeignClient(name = "user-center-server", contextId = "userCenterCommonApi", fallbackFactory = UserCenterCommonApiClientFallbackFactory.class, configuration = FeignConfiguration.class)
public interface UserCenterCommonApiClient extends UserCenterCommonApi {
}
