package com.cloud.base.core.modules.lh_security.server.service.impl;

import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.common.vo.LoginVo;
import com.cloud.base.core.modules.lh_security.core.param.TokenParam;
import com.cloud.base.core.modules.lh_security.server.authentication.SecurityVoucherVerificationProcess;
import com.cloud.base.core.modules.lh_security.server.service.SecurityService;
import com.cloud.base.core.modules.lh_security.server.token.TokenManager;
import com.cloud.base.core.modules.lh_security.server.voucher.SecurityVoucher;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author lh0811
 * @date 2021/5/23
 */
@Slf4j
public class SecurityServiceImpl implements SecurityService {


    @Autowired
    private SecurityVoucherVerificationProcess process;

    @Autowired
    private TokenManager tokenManager;


    /**
     * 授权服务 用户授权
     */
    @Override
    public LoginVo authorize(SecurityVoucher param) throws Exception {
        String token = process.voucherVerificationProcess(param);
        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);
        return loginVo;
    }


    /**
     * 授权服务 销毁token
     */
    @Override
    public void tokenDestroy(TokenParam param) throws Exception {
        tokenManager.removeToken(param.getToken());
    }

}
