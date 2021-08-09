package com.cloud.base.authorize.feign;

import com.cloud.base.alibaba_cloud.config.FeignConfiguration;
import com.cloud.base.authorize.feign.impl.UserCenterAuthorizeApiClientFallbackFactory;
import com.cloud.base.user.api.UserCenterAuthorizeApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author lh0811
 * @date 2021/5/23
 */
// name就是服务名称  这里的contextId就是这个bean在spring容器中的name
@FeignClient(name = "user-center-server", contextId = "userCenterAuthorizeApi", fallbackFactory = UserCenterAuthorizeApiClientFallbackFactory.class, configuration = FeignConfiguration.class)
public interface UserCenterAuthorizeApiClient extends UserCenterAuthorizeApi {

}
