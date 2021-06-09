package com.cloud.base.member.merchant.feign.impl;

import com.cloud.base.alibaba_cloud.fallback.FeignFallbackFactory;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.member.merchant.feign.UserCenterCommonApiClient;
import com.cloud.base.member.user.param.UserOfMchtQueryParam;
import com.cloud.base.member.user.vo.SysUserVo;
import com.github.pagehelper.PageInfo;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author lh0811
 * @date 2021/6/9
 */
@Component
public class UserCenterCommonApiClientFallbackFactory extends FeignFallbackFactory implements FallbackFactory< UserCenterCommonApiClient> {


    @Override
    public UserCenterCommonApiClient create(Throwable throwable) {
        return new UserCenterCommonApiClient() {
            @Override
            public ServerResponse<PageInfo<SysUserVo>> getUserVoListOfMcht(UserOfMchtQueryParam param) throws Exception {
                return ServerResponse.createByError("用户中心异常,查询商户会员列表失败,请稍后再试:" + errMsg(throwable));
            }
        };
    }
}
