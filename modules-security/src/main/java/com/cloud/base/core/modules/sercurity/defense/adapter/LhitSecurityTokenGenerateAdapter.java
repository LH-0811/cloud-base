package com.cloud.base.core.modules.sercurity.defense.adapter;


import com.cloud.base.core.modules.sercurity.defense.pojo.entity.LhitSecurityUserPerms;

/**
 * token 生成器。用于生成token
 */
public interface LhitSecurityTokenGenerateAdapter<U,R> {

    String generateToken(LhitSecurityUserPerms<U, R> perms);

}
