package com.cloud.base.member.user.expand.security.verification.username_password;

import com.alibaba.fastjson.JSONObject;
import com.cloud.base.core.common.entity.ServerResponse;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.modules.sercurity.defense.adapter.LhitSecurityUserVerificationAdapter;
import com.cloud.base.core.modules.sercurity.defense.annotation.LhitUserVerification;
import com.cloud.base.core.modules.sercurity.defense.pojo.entity.LhitSecurityPermission;
import com.cloud.base.core.modules.sercurity.defense.pojo.entity.LhitSecurityUserPerms;
import com.cloud.base.member.user.repository.entity.SysRes;
import com.cloud.base.member.user.repository.entity.SysRole;
import com.cloud.base.member.user.repository.entity.SysUser;
import com.cloud.base.member.user.service.SysUserService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@LhitUserVerification
public class UsernamePasswordVerificationAdapter implements LhitSecurityUserVerificationAdapter<UsernamePasswordVerification> {

    @Autowired
    private SysUserService sysUserService;


    @Autowired
    private HttpServletRequest request;

    @Override
    public LhitSecurityUserPerms verification(UsernamePasswordVerification verification) throws Exception {
        log.info("开始执行 UsernamePasswordVerificationAdapter.{},参数:{}", "verification", JSONObject.toJSONString(verification));

        // 获取到等用户
        SysUser loginUser = sysUserService.getUserByUsernameAndPassword(verification.getUsername(), verification.getPassword());

        if (loginUser == null){
            throw CommonException.create(ServerResponse.createByError("用户名或密码错误"));
        }

        // 获取用户角色列表
        List<SysRole> userRoleList = sysUserService.getUserRoleList(loginUser.getId());

        // 获取用户资源列表
        List<SysRes> resListByUser = sysUserService.getResListByUser(loginUser);

        if (loginUser.getUsername().equalsIgnoreCase("admin")) {
            List<LhitSecurityPermission> perms = Lists.newArrayList(LhitSecurityPermission.builder().url("/**").permsCode("all").build());
            LhitSecurityUserPerms userPerms = new LhitSecurityUserPerms();
            userPerms.setRoles(userRoleList);
            userPerms.setPermissions(perms);
            userPerms.setUser(loginUser);
            userPerms.setUserId(String.valueOf(loginUser.getId()));
            log.info("用户名密码 登录成功");
            return userPerms;
        }else {
            List<SysRes> interfaceList = resListByUser.stream().filter(ele -> StringUtils.isNotEmpty(ele.getUrl())).collect(Collectors.toList());
            List<LhitSecurityPermission> perms = Lists.newArrayList();
            for (SysRes sysRes : interfaceList) {
                LhitSecurityPermission permission = new LhitSecurityPermission();
                permission.setUrl(sysRes.getUrl());
                permission.setPermsCode(sysRes.getCode());
                perms.add(permission);
            }
            LhitSecurityUserPerms userPerms = new LhitSecurityUserPerms();
            userPerms.setRoles(userRoleList);
            userPerms.setPermissions(perms);
            userPerms.setUser(loginUser);
            userPerms.setUserId(String.valueOf(loginUser.getId()));
            log.info("用户名密码 登录成功");
            return userPerms;
        }
    }
}
