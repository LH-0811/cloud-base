package com.cloud.base.member.merchant.feign;

import com.cloud.base.member.user.api.UserCenterBaseFeign;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "user-center-server",contextId = "userCenterBaseFeign")
public interface UserCenterBaseFeignClient extends UserCenterBaseFeign {

}
