package com.cloud.base.member.user.service.impl;

import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityRes;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityRole;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityUser;
import com.cloud.base.member.user.param.UsernamePasswordVerificationParam;
import com.cloud.base.member.user.repository.entity.SysRes;
import com.cloud.base.member.user.repository.entity.SysRole;
import com.cloud.base.member.user.repository.entity.SysUser;
import com.cloud.base.member.user.service.AuthorizeService;
import com.cloud.base.member.user.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lh0811
 * @date 2021/5/23
 */
@Service("authorizeService")
public class AuthorizeServiceImpl implements AuthorizeService {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 通过用户名密码 获取用户信息 并组装权限信息
     */
    @Override
    public SecurityAuthority verification(UsernamePasswordVerificationParam param) throws Exception {
        // 获取到等用户
        SysUser loginUser = sysUserService.getUserByUsernameAndPassword(param.getUsername(), param.getPassword());

        if (loginUser == null) {
            throw CommonException.create(ServerResponse.createByError("用户名或密码错误"));
        }
        if (!loginUser.getActiveFlag()) {
            throw CommonException.create(ServerResponse.createByError("用户不可用请联系管理员"));
        }
        // 获取用户角色列表
        List<SysRole> userRoleList = sysUserService.getUserRoleList(loginUser.getId());
        // 获取用户资源列表
        List<SysRes> resAllList = sysUserService.getResListByUser(loginUser.getId());

        SecurityAuthority securityAuthority = new SecurityAuthority();
        securityAuthority.setSecurityUser(new SecurityUser(String.valueOf(loginUser.getId()), loginUser.getUsername()));
//        securityAuthority.setSecurityResList(Lists.newArrayList(
//                SecurityRes.allUrlRes(),
//                SecurityRes.allCodeRes(),
//                SecurityRes.allStaticResPath()
//        ));
        if (!CollectionUtils.isEmpty(resAllList)) {
            List<SecurityRes> securityResList = resAllList.stream().map(ele -> new SecurityRes(ele.getType(), ele.getName(), ele.getCode(), ele.getUrl(), "")).collect(Collectors.toList());
            securityAuthority.setSecurityResList(securityResList);
        }
        if (!CollectionUtils.isEmpty(userRoleList)) {
            List<SecurityRole> securityRoleList = userRoleList.stream().map(ele -> new SecurityRole(ele.getName())).collect(Collectors.toList());
            securityAuthority.setSecurityRoleList(securityRoleList);
        }

        return securityAuthority;
    }

}
