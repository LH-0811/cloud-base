package com.cloud.base.core.modules.sercurity.defense.adapter.impl;

import com.cloud.base.core.modules.sercurity.defense.adapter.*;
import com.cloud.base.core.modules.sercurity.defense.pojo.entity.LhitSecurityRole;
import com.cloud.base.core.modules.sercurity.defense.pojo.entity.LhitSecurityUserPerms;
import com.cloud.base.core.modules.sercurity.defense.pojo.user.DefaultLhitSecurityUser;
import com.cloud.base.core.modules.sercurity.defense.pojo.verification.LhitSecurityUserVerification;
import org.springframework.beans.factory.annotation.Autowired;


public class DefaultLhitSecurityUserAuthenticationLoginAdapter implements LhitSecurityUserAuthenticationLoginAdapter {

    @Autowired
    private LhitSecurityTokenManagerAdapter<DefaultLhitSecurityUser, LhitSecurityRole> lhitSecurityTokenManagerAdapter;

    @Autowired
    private LhitSecurityTokenGenerateAdapter lhitSecurityTokenGenerateAdapter;

    @Autowired
    private LhitSecurityVerificationDispatchAdapter lhitSecurityVerificationDispatchAdapter;

    @Override
    public String userAuthenticationLogin(LhitSecurityUserVerification verification) throws Exception {

        // 获取到凭证类分发器 找到凭证类型对应的 登录适配器
        LhitSecurityUserVerificationAdapter adapter = lhitSecurityVerificationDispatchAdapter.dispatchVerification(verification);

        // 使用是登录认证配置 验证用户凭证
        LhitSecurityUserPerms userPerms = adapter.verification(verification);

        // 使用token生成器来生成token
        String token = lhitSecurityTokenGenerateAdapter.generateToken(userPerms);

        // 保存用户信息
        lhitSecurityTokenManagerAdapter.saveToken(token, userPerms);

        return token;
    }

}
