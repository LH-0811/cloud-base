package com.cloud.base.member.user.service;

import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.member.user.param.UsernamePasswordVerificationParam;

/**
 * 用户授权使用接口
 *
 * @author lh0811
 * @date 2021/5/23
 */
public interface AuthorizeService {
    /**
     * 通过用户名密码 获取用户信息 并组装权限信息
     */
    SecurityAuthority verification(UsernamePasswordVerificationParam param) throws Exception;
}
