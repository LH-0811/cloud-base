package com.cloud.base.member.user.feign.impl;

import com.cloud.base.alibaba_cloud.fallback.FeignFallbackFactory;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.member.user.feign.MchtBaseInfoApiClient;
import com.cloud.base.memeber.merchant.param.MchtBaseInfoCreateParam;
import com.cloud.base.memeber.merchant.vo.MchtBaseInfoVo;
import feign.hystrix.FallbackFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lh0811
 * @date 2021/5/28
 */
@Component
public class MchtBaseInfoApiClientFallbackFactory extends FeignFallbackFactory implements FallbackFactory<MchtBaseInfoApiClient> {

    @Override
    public MchtBaseInfoApiClient create(Throwable throwable) {
        return new MchtBaseInfoApiClient() {
            @Override
            public ServerResponse mchtBaseInfoCreate(MchtBaseInfoCreateParam param) throws Exception {
                return ServerResponse.createByError("商户中心异常,请稍后再试:" + errMsg(throwable));
            }

            @Override
            public ServerResponse<List<MchtBaseInfoVo>> getMchtBaseInfoByUserId(Long userId) throws Exception {
                return ServerResponse.createByError("商户中心异常,请稍后再试:" + errMsg(throwable));
            }
        };
    }

}
