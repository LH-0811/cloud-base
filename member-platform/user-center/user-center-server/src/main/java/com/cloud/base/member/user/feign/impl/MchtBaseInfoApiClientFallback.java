package com.cloud.base.member.user.feign.impl;

import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.member.user.feign.MchtBaseInfoApiClient;
import com.cloud.base.memeber.merchant.param.MchtBaseInfoCreateParam;
import com.cloud.base.memeber.merchant.vo.MchtBaseInfoVo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lh0811
 * @date 2021/5/28
 */
@Component
public class MchtBaseInfoApiClientFallback implements MchtBaseInfoApiClient {
    @Override
    public ServerResponse mchtBaseInfoCreate(MchtBaseInfoCreateParam param) throws Exception {
        return ServerResponse.createByError("商户中心异常,请稍后再试");
    }

    @Override
    public ServerResponse<List<MchtBaseInfoVo>> getMchtBaseInfoByUserId(Long userId) throws Exception {
        return ServerResponse.createByError("商户中心异常,请稍后再试");
    }
}
