package com.cloud.base.core.modules.lh_security.server.controller;

import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.core.modules.lh_security.core.param.CheckResParam;
import com.cloud.base.core.modules.lh_security.core.param.TokenParam;
import com.cloud.base.core.modules.lh_security.core.properties.SecurityProperties;
import com.cloud.base.core.modules.lh_security.server.authentication.SecurityCheckAuthority;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lh0811
 * @date 2021/5/23
 */
@Slf4j
@RestController
public class SecurityController {


    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private SecurityCheckAuthority securityCheckAuthority;

    ///////// 鉴权接口
    @PostMapping("${lhit.security.server-url-of-check-url}")
    @ApiOperation(tags = "鉴权接口", value = "鉴权接口-验证用户是否有url权限")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "CheckResParam", dataTypeClass = CheckResParam.class, name = "param", value = "参数")
    })
    public ServerResponse checkUrl(@RequestBody CheckResParam param) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 鉴权接口 验证用户是否有url权限 接口 : SecurityController-checkUrl ");
        if (securityCheckAuthority.checkUrl(param.getToken(), param.getResource())) {
            return ServerResponse.createBySuccess("鉴权通过");
        } else {
            return ServerResponse.createByError(Integer.valueOf(securityProperties.getUnAuthorizedCode()), "该用户无权限访问,请联系管理员", "");
        }
    }

    @PostMapping("${lhit.security.server-url-of-check-perms-code}")
    @ApiOperation(tags = "鉴权接口", value = "鉴权接口-验证用户是否有permsCode（权限码）权限")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "CheckResParam", dataTypeClass = CheckResParam.class, name = "param", value = "参数")
    })
    public ServerResponse checkPermsCode(@RequestBody CheckResParam param) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 鉴权接口 验证用户是否有permsCode 接口 : SecurityController-checkPermsCode ");
        if (securityCheckAuthority.checkPermsCode(param.getToken(), param.getResource())) {
            return ServerResponse.createBySuccess("鉴权通过");
        } else {
            return ServerResponse.createByError(Integer.valueOf(securityProperties.getUnAuthorizedCode()), "该用户无权限访问,请联系管理员", "");
        }
    }

    @PostMapping("${lhit.security.server-url-of-check-static-res-path}")
    @ApiOperation(tags = "鉴权接口", value = "鉴权接口-验证用户是否有staticResPath（静态资源路径）权限")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "CheckResParam", dataTypeClass = CheckResParam.class, name = "param", value = "参数")
    })
    public ServerResponse checkStaticResPath(@RequestBody CheckResParam param) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 鉴权接口 验证用户是否有staticResPath 接口 : SecurityController-checkStaticResPath ");
        if (securityCheckAuthority.checkStaticResPath(param.getToken(), param.getResource())) {
            return ServerResponse.createBySuccess("鉴权通过");
        } else {
            return ServerResponse.createByError(Integer.valueOf(securityProperties.getUnAuthorizedCode()), "该用户无权限访问,请联系管理员", "");
        }
    }

    @PostMapping("${lhit.security.server-url-of-token-to-authority}")
    @ApiOperation(tags = "鉴权接口", value = "鉴权接口-token转换为用户权限信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "CheckResParam", dataTypeClass = CheckResParam.class, name = "param", value = "参数")
    })
    public ServerResponse<SecurityAuthority> tokenToAuthority(@RequestBody TokenParam param) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 鉴权接口 token转换为用户权限信息 接口 : SecurityController-tokenToAuthority ");
        SecurityAuthority securityAuthority = securityCheckAuthority.getSecurityAuthorityByToken(param.getToken());
        return ServerResponse.createBySuccess("获取成功", securityAuthority);
    }


}
