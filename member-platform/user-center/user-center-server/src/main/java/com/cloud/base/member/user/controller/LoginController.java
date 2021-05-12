package com.cloud.base.member.user.controller;

import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.lh_security.client.component.annotation.HasUrl;
import com.cloud.base.core.modules.lh_security.client.component.annotation.TokenToAuthority;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.core.modules.lh_security.server.authentication.SecurityVoucherVerificationProcess;
import com.cloud.base.core.modules.lh_security.server.token.TokenManager;
import com.cloud.base.core.modules.logger.annotation.LhitLogger;
import com.cloud.base.core.modules.logger.entity.LoggerBusinessType;
import com.cloud.base.member.user.expand.security.verification.username_password.UsernamePasswordVerification;
import com.cloud.base.member.user.vo.LoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 登录接口
 *
 * @author lh0811
 * @date 2021/1/18
 */
@Slf4j
@Api(tags = "登录接口")
@RestController
@RequestMapping("/login")
public class LoginController extends BaseController {

    @Autowired
    private SecurityVoucherVerificationProcess process;

    @Autowired
    private TokenManager tokenManager;

    @PostMapping("/sys_user/username_password")
    @ApiOperation("系统用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UsernamePasswordVerification", dataTypeClass = UsernamePasswordVerification.class, name = "param", value = "参数")
    })
    @LhitLogger(title = "用户登录",businessType = LoggerBusinessType.QUERY,isSaveOperUserInfo = false,isSaveOperRoleInfo = false)
    public ServerResponse<LoginVo> sysUserLoginByUsernamePassword(@Validated @RequestBody UsernamePasswordVerification param) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 系统用户登录 接口 : LoginAuthenticationController-sysUserLoginByUsernamePassword ");
        String token = process.voucherVerificationProcess(param);
        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);
        return ServerResponse.createBySuccess("登录成功", loginVo);
    }


    @GetMapping("/current_user_prems")
    @ApiOperation("获取当前用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
    })
    @HasUrl(url = "/123")
    public ServerResponse<SecurityAuthority> getUesrInfo(@RequestHeader(value = "LHTOKEN", defaultValue = "") String token,SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 获取当前用户信息 接口 : SysUserController-getUesrInfo");
        return ServerResponse.createBySuccess("获取成功", securityAuthority);
    }

    @GetMapping("/logout")
    @ApiOperation("用户退出")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
    })
    @LhitLogger(title = "用户退出",businessType = LoggerBusinessType.QUERY)
    public ServerResponse logout(@RequestHeader(value = "LHTOKEN", defaultValue = "") String token) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 获取当前用户信息 接口 : SysUserController-logout");
        tokenManager.removeToken(token);
        return ServerResponse.createBySuccess("退出成功");
    }

}
