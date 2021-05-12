package com.cloud.base.member.user.expand.security.verification.username_password;

import com.alibaba.fastjson.JSONObject;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityRes;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityRole;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityUser;
import com.cloud.base.core.modules.lh_security.server.authentication.SecurityVoucherVerification;
import com.cloud.base.member.user.repository.entity.SysRes;
import com.cloud.base.member.user.repository.entity.SysRole;
import com.cloud.base.member.user.repository.entity.SysUser;
import com.cloud.base.member.user.service.SysUserService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UsernamePasswordVerificationAdapter implements SecurityVoucherVerification<UsernamePasswordVerification> {

    @Autowired
    private SysUserService sysUserService;


    @Override
    public SecurityAuthority verification(UsernamePasswordVerification verification) throws Exception {
        log.info("开始执行 UsernamePasswordVerificationAdapter.{},参数:{}", "verification", JSONObject.toJSONString(verification));
        // 获取到等用户
        SysUser loginUser = sysUserService.getUserByUsernameAndPassword(verification.getUsername(), verification.getPassword());

        if (loginUser == null) {
            throw CommonException.create(ServerResponse.createByError("用户名或密码错误"));
        }
        if (!loginUser.getActiveFlag()) {
            throw CommonException.create(ServerResponse.createByError("用户不可用请联系管理员"));
        }
        // 获取用户角色列表
        List<SysRole> userRoleList = sysUserService.getUserRoleList(loginUser.getId());
        // 获取用户资源列表
        List<SysRes> resAllList = sysUserService.getResListByUser(loginUser);

        SecurityAuthority securityAuthority = new SecurityAuthority();
        securityAuthority.setSecurityUser(new SecurityUser(String.valueOf(loginUser.getId()), loginUser.getUsername()));
        securityAuthority.setSecurityResList(Lists.newArrayList(
                SecurityRes.allUrlRes(),
                SecurityRes.allCodeRes(),
                SecurityRes.allStaticResPath()
        ));
//        if (!CollectionUtils.isEmpty(resAllList)) {
//            List<SecurityRes> securityResList = resAllList.stream().map(ele -> new SecurityRes(ele.getType(), ele.getName(), ele.getCode(), ele.getUrl(), "")).collect(Collectors.toList());
//            securityAuthority.setSecurityResList(securityResList);
//        }
        if (!CollectionUtils.isEmpty(userRoleList)) {
            List<SecurityRole> securityRoleList = userRoleList.stream().map(ele -> new SecurityRole(ele.getName())).collect(Collectors.toList());
            securityAuthority.setSecurityRoleList(securityRoleList);
        }
        log.info("用户名密码 登录成功");
        return securityAuthority;
    }

}
