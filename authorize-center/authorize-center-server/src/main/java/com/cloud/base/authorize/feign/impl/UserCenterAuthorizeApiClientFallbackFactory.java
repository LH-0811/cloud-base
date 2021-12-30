package com.cloud.base.authorize.feign.impl;

import com.cloud.base.alibaba_cloud.fallback.FeignFallbackFactory;
import com.cloud.base.authorize.feign.UserCenterAuthorizeApiClient;
import com.cloud.base.common.core.response.ServerResponse;
import com.cloud.base.common.xugou.core.model.entity.SecurityAuthority;
import com.cloud.base.user.param.UsernamePasswordVerificationParam;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author lh0811
 * @date 2021/5/28
 */
@Component
public class UserCenterAuthorizeApiClientFallbackFactory extends FeignFallbackFactory implements FallbackFactory<UserCenterAuthorizeApiClient> {
    @Override
    public UserCenterAuthorizeApiClient create(Throwable throwable) {
        return new UserCenterAuthorizeApiClient() {
            @Override
            public ServerResponse<SecurityAuthority> verificationUserByUsernameAndPwd(UsernamePasswordVerificationParam param) throws Exception {
                return ServerResponse.createByError("用户中心异常,请稍后再试:" + errMsg(throwable));
            }
        };
    }
}
