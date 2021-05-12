package com.cloud.base.member.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ResponseCode;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.sercurity.defense.adapter.LhitSecurityTokenManagerAdapter;
import com.cloud.base.core.modules.sercurity.defense.pojo.entity.LhitSecurityRole;
import com.cloud.base.core.modules.sercurity.defense.pojo.entity.LhitSecurityUserPerms;
import com.cloud.base.core.modules.sercurity.defense.pojo.user.LhitSecurityUser;
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
    private LhitSecurityTokenManagerAdapter<LhitSecurityUser, LhitSecurityRole> lhitSecurityTokenManagerAdapter;

    @Autowired
    private SysUserService sysUserService;


    public LhitSecurityUserPerms<LhitSecurityRole, LhitSecurityUser> getCurrentUserInfoWithPrems(String token) throws Exception {
        return lhitSecurityTokenManagerAdapter.getPermsByToken(token);
    }

    public SysUser getCurrentSysUser(String token) throws Exception {
        SysUser sysUser = JSONObject.parseObject(JSONObject.toJSONString(lhitSecurityTokenManagerAdapter.getUserInfoByToken(token)), SysUser.class);
        if (sysUser == null) {
            throw CommonException.create(ServerResponse.createByError(ResponseCode.NO_AUTH.getCode(), "用户token无效,请重新登录后访问"));
        }
        return sysUser;
    }

}
