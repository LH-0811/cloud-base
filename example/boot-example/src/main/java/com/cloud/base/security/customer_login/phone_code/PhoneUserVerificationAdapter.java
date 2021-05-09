package com.cloud.base.security.customer_login.phone_code;

import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.sercurity.defense.adapter.LhitSecurityUserVerificationAdapter;
import com.cloud.base.core.modules.sercurity.defense.annotation.LhitUserVerification;
import com.cloud.base.core.modules.sercurity.defense.pojo.entity.LhitSecurityPermission;
import com.cloud.base.core.modules.sercurity.defense.pojo.entity.LhitSecurityRole;
import com.cloud.base.core.modules.sercurity.defense.pojo.entity.LhitSecurityUserPerms;
import com.cloud.base.core.modules.sercurity.defense.pojo.user.DefaultLhitSecurityUser;
import com.google.common.collect.Lists;

/**
 * 用户认证适配器
 * <p>
 * 通过该适配器 指定用户的登录认证过程 并将 用户的权限获取到
 */
@LhitUserVerification
public class PhoneUserVerificationAdapter implements LhitSecurityUserVerificationAdapter<PhoneUserVerification> {


    @Override
    public LhitSecurityUserPerms verification(PhoneUserVerification verification) throws Exception {
        if (!"17600000001".equals(verification.getPhone())) {
            throw CommonException.create(ServerResponse.createByError("手机号错误，默认手机号17600000001"));
        } else if (!"123456".equals(verification.getCode())) {
            throw CommonException.create(ServerResponse.createByError("验证码不正确：默认密码123456"));
        } else {
            LhitSecurityRole role = new LhitSecurityRole("admin");
            LhitSecurityPermission permission = new LhitSecurityPermission("/**", "dept", "all", null);
            DefaultLhitSecurityUser user = DefaultLhitSecurityUser.builder().userId("default").password("user").username("user").build();
            return new LhitSecurityUserPerms(Lists.newArrayList(new LhitSecurityRole[]{role}), Lists.newArrayList(new LhitSecurityPermission[]{permission}), user.getUserId(), user);
        }

    }
}
