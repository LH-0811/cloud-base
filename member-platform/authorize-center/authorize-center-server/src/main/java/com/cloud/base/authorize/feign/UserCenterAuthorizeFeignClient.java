package com.cloud.base.authorize.feign;

import com.cloud.base.member.user.api.UserCenterAuthorizeFeign;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author lh0811
 * @date 2021/5/23
 */
// name就是服务名称  这里的contextId就是这个bean在spring容器中的name
@FeignClient(name = "user-center-server",contextId = "userCenterAuthorizeFeign")
public interface UserCenterAuthorizeFeignClient extends UserCenterAuthorizeFeign {

}
