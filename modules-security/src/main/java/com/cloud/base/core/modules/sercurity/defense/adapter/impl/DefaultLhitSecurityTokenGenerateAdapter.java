package com.cloud.base.core.modules.sercurity.defense.adapter.impl;


import com.cloud.base.core.modules.sercurity.defense.adapter.LhitSecurityTokenGenerateAdapter;
import com.cloud.base.core.modules.sercurity.defense.pojo.entity.LhitSecurityUserPerms;

import java.util.UUID;

public class DefaultLhitSecurityTokenGenerateAdapter<U,R> implements LhitSecurityTokenGenerateAdapter<U,R> {

    @Override
    public String generateToken(LhitSecurityUserPerms<U,R> perms) {
        return UUID.randomUUID().toString();
    }
}
