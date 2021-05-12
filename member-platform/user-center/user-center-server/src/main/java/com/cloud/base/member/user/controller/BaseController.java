package com.cloud.base.member.user.controller;

import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ResponseCode;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.core.modules.lh_security.server.token.TokenManager;
import com.cloud.base.member.user.repository.entity.SysUser;
import com.cloud.base.member.user.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * controller 基础类
 *
 * @author lh0811
 * @date 2021/1/4
 */
@Slf4j
@Component
public class BaseController {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private SysUserService sysUserService;


    public SysUser getCurrentSysUser(SecurityAuthority securityAuthority) throws Exception {
        if (securityAuthority == null) {
            throw CommonException.create(ServerResponse.createByError(ResponseCode.NO_AUTH.getCode(), "用户token无效,请重新登录后访问"));
        }
        SysUser sysUser = sysUserService.getUserByUserId(Long.valueOf(securityAuthority.getSecurityUser().getId()));
        return sysUser;
    }

}