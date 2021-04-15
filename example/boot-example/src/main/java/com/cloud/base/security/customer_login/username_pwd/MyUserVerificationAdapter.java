package com.cloud.base.security.customer_login.username_pwd;

import com.cloud.base.core.common.entity.ServerResponse;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.modules.sercurity.defense.adapter.LhitSecurityUserVerificationAdapter;
import com.cloud.base.core.modules.sercurity.defense.annotation.LhitUserVerification;
import com.cloud.base.core.modules.sercurity.defense.pojo.entity.LhitSecurityDataRule;
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
public class MyUserVerificationAdapter implements LhitSecurityUserVerificationAdapter<UsernamePasswordUserVerification> {


    @Override
    public LhitSecurityUserPerms verification(UsernamePasswordUserVerification verification) throws Exception {
        if (!"user".equals(verification.getUsername())) {
            throw CommonException.create(ServerResponse.createByError("用户名错误，默认用户：user"));
        } else if (!"123456".equals(verification.getPassword())) {
            throw CommonException.create(ServerResponse.createByError("密码不正确：默认密码123456"));
        } else {
            LhitSecurityRole role = new LhitSecurityRole("admin");

            LhitSecurityDataRule dataRule1 = new LhitSecurityDataRule();
            dataRule1.setApiPath("/data_intercept");
            dataRule1.setRoleId("1");
            dataRule1.setIncludeFields(Lists.newArrayList("name","age"));
            dataRule1.setExcludeFields(Lists.newArrayList("score"));

            LhitSecurityDataRule dataRule2 = new LhitSecurityDataRule();
            dataRule2.setApiPath("/data_intercept/student");
            dataRule2.setRoleId("1");
            dataRule2.setIncludeFields(Lists.newArrayList("name","age"));
            dataRule2.setExcludeFields(Lists.newArrayList("score"));

            LhitSecurityPermission permission = new LhitSecurityPermission("/**", "dept", "all", Lists.newArrayList(dataRule1,dataRule2));
            DefaultLhitSecurityUser user = DefaultLhitSecurityUser.builder().userId("default").password("user").username("user").build();
            return new LhitSecurityUserPerms(Lists.newArrayList(new LhitSecurityRole[]{role}), Lists.newArrayList(new LhitSecurityPermission[]{permission}), user.getUserId(), user);
        }

    }
}
