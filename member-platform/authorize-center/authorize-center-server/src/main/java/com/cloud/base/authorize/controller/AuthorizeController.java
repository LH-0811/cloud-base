package com.cloud.base.authorize.controller;

import com.cloud.base.authorize.security.verification.UsernamePasswordVerification;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.common.vo.LoginVo;
import com.cloud.base.core.modules.lh_security.core.param.TokenParam;
import com.cloud.base.core.modules.lh_security.server.service.SecurityServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author lh0811
 * @date 2021/5/23
 */
@Slf4j
@Api(tags = "认证接口")
@RestController
@RequestMapping("/security")
public class AuthorizeController {


    @Autowired
    private SecurityServer securityServer;


    @PostMapping("/login/username_password")
    @ApiOperation("系统用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UsernamePasswordVerification", dataTypeClass = UsernamePasswordVerification.class, name = "param", value = "参数")
    })
    public ServerResponse<LoginVo> sysUserLoginByUsernamePassword(@Validated @RequestBody UsernamePasswordVerification param) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 系统用户登录 接口 : LoginAuthenticationController-sysUserLoginByUsernamePassword ");
        LoginVo authorize = securityServer.authorize(param);
        return ServerResponse.createBySuccess("登录成功", authorize);
    }

    @GetMapping("/logout")
    @ApiOperation("用户退出")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
    })
    public ServerResponse logout(@RequestHeader(value = "LHTOKEN", defaultValue = "") String token) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 获取当前用户信息 接口 : SysUserController-logout");
        securityServer.tokenDestroy(new TokenParam(token));
        return ServerResponse.createBySuccess("退出成功");
    }

}
