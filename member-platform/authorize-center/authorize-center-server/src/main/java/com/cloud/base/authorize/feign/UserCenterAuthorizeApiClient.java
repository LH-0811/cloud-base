package com.cloud.base.authorize.feign;

import com.cloud.base.alibaba_cloud.FeignConfiguration;
import com.cloud.base.member.user.api.UserCenterAuthorizeApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author lh0811
 * @date 2021/5/23
 */
// name就是服务名称  这里的contextId就是这个bean在spring容器中的name
@FeignClient(name = "user-center-server",contextId = "userCenterAuthorizeApi",configuration = FeignConfiguration.class)
public interface UserCenterAuthorizeApiClient extends UserCenterAuthorizeApi {

}
