package com.cloud.base.member.merchant.feign.impl;

import com.cloud.base.alibaba_cloud.fallback.FeignFallbackFactory;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.member.property.api.PropScoreAccountApi;
import com.cloud.base.member.property.param.PropScoreAccountCreateParam;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author lh0811
 * @date 2021/6/17
 */
@Component
public class PropScoreAccountApiClientFallbackFactory  extends FeignFallbackFactory implements FallbackFactory<PropScoreAccountApi> {
    @Override
    public PropScoreAccountApi create(Throwable throwable) {
        return new PropScoreAccountApi() {
            @Override
            public ServerResponse createPropScoreAccount(PropScoreAccountCreateParam param) throws Exception {
                return ServerResponse.createByError("资产中心异常,初始化会员积分账户失败,请稍后再试:" + errMsg(throwable));
            }
        };
    }
}
