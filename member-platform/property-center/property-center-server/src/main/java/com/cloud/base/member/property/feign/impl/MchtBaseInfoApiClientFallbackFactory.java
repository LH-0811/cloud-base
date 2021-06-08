package com.cloud.base.member.property.feign.impl;

import com.cloud.base.alibaba_cloud.fallback.FeignFallbackFactory;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.member.property.feign.MchtApiClient;
import com.cloud.base.member.merchant.param.MchtInfoCreateParam;
import com.cloud.base.member.merchant.vo.MchtInfoVo;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lh0811
 * @date 2021/5/28
 */
@Component
public class MchtBaseInfoApiClientFallbackFactory extends FeignFallbackFactory implements FallbackFactory<MchtApiClient> {

    @Override
    public MchtApiClient create(Throwable throwable) {
        return new MchtApiClient() {
            @Override
            public ServerResponse mchtBaseInfoCreate(MchtInfoCreateParam param) throws Exception {
                return ServerResponse.createByError("商户中心异常,创建商户信息失败,请稍后再试:" + errMsg(throwable));
            }

            @Override
            public ServerResponse<List<MchtInfoVo>> getMchtBaseInfoByUserId(Long userId) throws Exception {
                return ServerResponse.createByError("商户中心异常,获取用户商户信息列表失败,请稍后再试:" + errMsg(throwable));
            }

            @Override
            public ServerResponse<MchtInfoVo> getMchtBaseInfoVoById(Long mchtBaseInfoId) throws Exception {
                return ServerResponse.createByError("商户中心异常,获取商户信息失败,请稍后再试:" + errMsg(throwable));
            }

        };
    }

}
