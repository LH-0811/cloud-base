package com.cloud.base.security;

import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.lh_security.authentication.SecurityVoucherVerificationProcess;
import com.cloud.base.core.modules.lh_security.voucher.DefaultUsernamePasswordVoucher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lh0811
 * @date 2021/5/9
 */
@Slf4j
@RestController
public class LoginController {

    @Autowired
    private SecurityVoucherVerificationProcess process;

    @GetMapping("/login")
    public ServerResponse login(@RequestParam String username,@RequestParam String password) throws Exception {
        String token = process.voucherVerificationProcess(new DefaultUsernamePasswordVoucher(username, password));
        return ServerResponse.createBySuccess("登录成功",token);
    }

}
