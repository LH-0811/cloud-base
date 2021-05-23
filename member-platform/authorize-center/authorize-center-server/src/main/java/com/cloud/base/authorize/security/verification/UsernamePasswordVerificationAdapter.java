package com.cloud.base.authorize.security.verification;

import com.alibaba.fastjson.JSONObject;
import com.cloud.base.authorize.feign.UserCenterAuthorizeFeignClient;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.core.modules.lh_security.server.authentication.SecurityVoucherVerification;
import com.cloud.base.member.user.param.UsernamePasswordVerificationParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UsernamePasswordVerificationAdapter implements SecurityVoucherVerification<UsernamePasswordVerification> {

    @Autowired
    private UserCenterAuthorizeFeignClient userCenterAuthorizeFeignClient;


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
