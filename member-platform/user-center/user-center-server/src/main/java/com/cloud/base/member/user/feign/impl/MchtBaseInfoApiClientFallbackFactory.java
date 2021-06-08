package com.cloud.base.member.user.feign.impl;

import com.cloud.base.alibaba_cloud.fallback.FeignFallbackFactory;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.member.user.feign.MchtInfoApiClient;
import com.cloud.base.memeber.merchant.param.MchtInfoCreateParam;
import com.cloud.base.memeber.merchant.vo.MchtInfoVo;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lh0811
 * @date 2021/5/28
 */
@Component
public class MchtBaseInfoApiClientFallbackFactory extends FeignFallbackFactory implements FallbackFactory<MchtInfoApiClient> {

    @Override
    public MchtInfoApiClient create(Throwable throwable) {
        return new MchtInfoApiClient() {
            @Override
            public ServerResponse mchtBaseInfoCreate(MchtInfoCreateParam param) throws Exception {
                return ServerResponse.createByError("商户中心异常,创建商户信息失败,请稍后再试:" + errMsg(throwable));
            }

            @Override
            public ServerResponse<List<MchtInfoVo>> getMchtBaseInfoByUserId(Long userId) throws Exception {
                return ServerResponse.createByError("商户中心异常,获取用户关联商户信息失败,请稍后再试:" + errMsg(throwable));
            }

            @Override
            public ServerResponse<MchtInfoVo> getMchtBaseInfoVoById(Long mchtBaseInfoId) throws Exception {
                return ServerResponse.createByError("商户中心异常,获取商户详情失败,请稍后再试:" + errMsg(throwable));
            }
        };
    }

}
