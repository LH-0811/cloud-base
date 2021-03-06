package com.cloud.base.authorize.security.verification;

import com.alibaba.fastjson.JSONObject;
import com.cloud.base.authorize.feign.UserCenterAuthorizeApiClient;
import com.cloud.base.common.core.exception.CommonException;
import com.cloud.base.common.core.response.ServerResponse;
import com.cloud.base.common.xugou.core.model.entity.SecurityAuthority;
import com.cloud.base.common.xugou.core.server.api.authentication.SecurityVoucherVerification;
import com.cloud.base.user.param.UsernamePasswordVerificationParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UsernamePasswordVerificationAdapter implements SecurityVoucherVerification<UsernamePasswordVerification> {

    @Autowired
    private UserCenterAuthorizeApiClient userCenterAuthorizeFeignClient;


    @Override
    public SecurityAuthority verification(UsernamePasswordVerification verification) throws Exception {
        log.info("开始执行 UsernamePasswordVerificationAdapter.{},参数:{}", "verification", JSONObject.toJSONString(verification));
        ServerResponse<SecurityAuthority> serverResponse = userCenterAuthorizeFeignClient.verificationUserByUsernameAndPwd(new UsernamePasswordVerificationParam(verification.getUsername(), verification.getPassword()));
        if (serverResponse.isSuccess()){
            log.info("用户名密码 登录成功");
            return serverResponse.getData();
        }else {
            log.info("用户名密码 登录失败");
            throw CommonException.create(serverResponse);
        }
    }
}
