package com.cloud.base.user.controller;

import com.cloud.base.common.core.exception.CommonException;
import com.cloud.base.common.core.response.ResponseCode;
import com.cloud.base.common.core.response.ServerResponse;
import com.cloud.base.common.xugou.core.model.entity.SecurityAuthority;
import com.cloud.base.user.repository.entity.SysUser;
import com.cloud.base.user.service.CurrentUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * controller 基础类
 *
 * @author lh0811
 * @date 2021/1/4
 */
@Slf4j
@Component
public class BaseController {

    @Resource
    private CurrentUserService currentUserService;

    public SysUser getCurrentSysUser(SecurityAuthority securityAuthority) throws Exception {
        if (securityAuthority == null) {
            throw CommonException.create(ServerResponse.createByError(ResponseCode.NO_AUTH.getCode(), "用户token无效,请重新登录后访问"));
        }
        SysUser sysUser = currentUserService.getUserByUserId(Long.valueOf(securityAuthority.getSecurityUser().getId()));
        return sysUser;
    }

}
