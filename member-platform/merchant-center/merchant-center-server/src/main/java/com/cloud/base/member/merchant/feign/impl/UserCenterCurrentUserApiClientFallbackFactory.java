package com.cloud.base.member.merchant.feign.impl;

import com.cloud.base.alibaba_cloud.fallback.FeignFallbackFactory;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.member.merchant.feign.UserCenterCurrentUserApiClient;
import com.cloud.base.user.vo.SysUserVo;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author lh0811
 * @date 2021/5/28
 */
@Component
public class UserCenterCurrentUserApiClientFallbackFactory extends FeignFallbackFactory implements FallbackFactory<UserCenterCurrentUserApiClient> {
    @Override
    public UserCenterCurrentUserApiClient create(Throwable throwable) {
        return new UserCenterCurrentUserApiClient() {
            @Override
            public ServerResponse<SysUserVo> getUesrInfo() throws Exception {
                return ServerResponse.createByError("用户中心异常,查询用户信息失败,请稍后再试:" + errMsg(throwable));
            }
        };
    }
}
