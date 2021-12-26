package com.cloud.base.modules.xugou.server.service.impl;

import com.cloud.base.modules.xugou.core.model.param.TokenParam;
import com.cloud.base.modules.xugou.core.model.vo.AuthenticationVo;
import com.cloud.base.modules.xugou.core.server.api.authentication.SecurityVoucherVerificationProcess;
import com.cloud.base.modules.xugou.server.service.SecurityServer;
import com.cloud.base.modules.xugou.server.token.TokenManager;
import com.cloud.base.modules.xugou.core.server.api.voucher.SecurityVoucher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author lh0811
 * @date 2021/5/23
 */
@Slf4j
public class DefaultSecurityServerImpl implements SecurityServer {


    @Autowired
    private SecurityVoucherVerificationProcess process;

    @Autowired
    private TokenManager tokenManager;


    /**
     * 授权服务 用户授权
     */
    @Override
    public AuthenticationVo authorize(SecurityVoucher param) throws Exception {
        String token = process.voucherVerificationProcess(param);
        AuthenticationVo authenticationVo = new AuthenticationVo();
        authenticationVo.setToken(token);
        return authenticationVo;
    }


    /**
     * 授权服务 销毁token
     */
    @Override
    public void tokenDestroy(TokenParam param) throws Exception {
        tokenManager.removeToken(param.getToken());
    }

}
