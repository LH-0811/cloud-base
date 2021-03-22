package com.cloud.base.core.modules.sercurity.defense.adapter.impl;

import com.google.common.collect.Lists;
import com.cloud.base.core.common.entity.ServerResponse;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.modules.sercurity.defense.adapter.LhitSecurityUserVerificationAdapter;
import com.cloud.base.core.modules.sercurity.defense.annotation.LhitUserVerification;
import com.cloud.base.core.modules.sercurity.defense.pojo.entity.LhitSecurityPermission;
import com.cloud.base.core.modules.sercurity.defense.pojo.entity.LhitSecurityRole;
import com.cloud.base.core.modules.sercurity.defense.pojo.entity.LhitSecurityUserPerms;
import com.cloud.base.core.modules.sercurity.defense.pojo.user.DefaultLhitSecurityUser;
import com.cloud.base.core.modules.sercurity.defense.pojo.verification.DefaultUsernamePasswordUserVerification;

/**
 * 默认用户认证 实现
 */
@LhitUserVerification
public class DefaultLhitSecurityUserVerificationImpl implements LhitSecurityUserVerificationAdapter<DefaultUsernamePasswordUserVerification> {

    @Override
    public LhitSecurityUserPerms<LhitSecurityRole, DefaultLhitSecurityUser> verification(DefaultUsernamePasswordUserVerification verification) throws Exception {
        if (!"user".equals(verification.getUsername())) {
            throw CommonException.create(ServerResponse.createByError("用户名错误，默认用户名user"));
        }
        if (!"user".equals(verification.getPassword())) {
            throw CommonException.create(ServerResponse.createByError("密码不正确：默认密码user"));
        }
        LhitSecurityRole role = new LhitSecurityRole("admin");
        LhitSecurityPermission permission = new LhitSecurityPermission("/**","default_dept", "all");
        DefaultLhitSecurityUser user = DefaultLhitSecurityUser.builder().userId("default").password("user").username("user").build();
        return new LhitSecurityUserPerms(Lists.newArrayList(role), Lists.newArrayList(permission), user.getUserId(), user);
    }

}
