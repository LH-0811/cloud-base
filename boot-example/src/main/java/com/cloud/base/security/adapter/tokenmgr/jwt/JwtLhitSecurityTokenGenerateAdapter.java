package com.cloud.base.security.adapter.tokenmgr.jwt;

import com.cloud.base.core.modules.sercurity.defense.adapter.LhitSecurityTokenGenerateAdapter;
import com.cloud.base.core.modules.sercurity.defense.pojo.entity.LhitSecurityUserPerms;
import com.cloud.base.core.modules.sercurity.defense.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
//@Component
public class JwtLhitSecurityTokenGenerateAdapter<U, R> implements LhitSecurityTokenGenerateAdapter<U, R> {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public String generateToken(LhitSecurityUserPerms<U, R> perms) {
        return jwtUtils.geneJsonWebToken(perms);
    }




}
