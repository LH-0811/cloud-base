package com.cloud.base.member.user.controller;

import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.common.vo.LoginVo;
import com.cloud.base.core.modules.lh_security.client.component.annotation.TokenToAuthority;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.core.modules.lh_security.core.param.TokenParam;
import com.cloud.base.core.modules.lh_security.server.authentication.SecurityVoucherVerificationProcess;
import com.cloud.base.core.modules.lh_security.server.service.SecurityService;
import com.cloud.base.core.modules.lh_security.server.token.TokenManager;
import com.cloud.base.core.modules.logger.annotation.LhitLogger;
import com.cloud.base.core.modules.logger.entity.LoggerBusinessType;
import com.cloud.base.member.user.expand.security.verification.username_password.UsernamePasswordVerification;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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
    private SecurityService securityService;

    @PostMapping("/sys_user/username_password")
    @ApiOperation("系统用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UsernamePasswordVerification", dataTypeClass = UsernamePasswordVerification.class, name = "param", value = "参数")
    })
    @LhitLogger(title = "用户登录",businessType = LoggerBusinessType.QUERY,isSaveOperUserInfo = false,isSaveOperRoleInfo = false)
    public ServerResponse<LoginVo> sysUserLoginByUsernamePassword(@Validated @RequestBody UsernamePasswordVerification param) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 系统用户登录 接口 : LoginAuthenticationController-sysUserLoginByUsernamePassword ");
        LoginVo authorize = securityService.authorize(param);
        return ServerResponse.createBySuccess("登录成功", authorize);
    }

    @GetMapping("/logout")
    @ApiOperation("用户退出")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
    })
    @LhitLogger(title = "用户退出",businessType = LoggerBusinessType.QUERY)
    @TokenToAuthority
    public ServerResponse logout(@ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 获取当前用户信息 接口 : SysUserController-logout");
        securityService.tokenDestroy(new TokenParam(securityAuthority.getToken()));
        return ServerResponse.createBySuccess("退出成功");
    }

}
