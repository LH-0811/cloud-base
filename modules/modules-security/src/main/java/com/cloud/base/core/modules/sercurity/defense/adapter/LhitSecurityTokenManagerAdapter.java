package com.cloud.base.core.modules.sercurity.defense.adapter;


import com.cloud.base.core.modules.sercurity.defense.pojo.entity.LhitSecurityUserPerms;

/**
 * 该适配器用户用户 token的管理
 * <p>
 * 1.保存用户token信息: 建立token -- 用户信息 -- 用户权限 之间的关联关系
 * 2.移除token信息
 * 3.检查当前token是否是可用的
 * 4.通过token 获取用户权限信息
 * 5.通过token 获取用户基本信息
 * <p>
 * 提供基于guava缓存的默认token管理
 *
 * @param <U>
 * @param <R>
 */
public interface LhitSecurityTokenManagerAdapter<U, R > {

    /**
     * 保存 用户 token
     *
     * @param token
     * @param perms
     */
    void saveToken(String token, LhitSecurityUserPerms<R, U> perms) throws Exception;

    /**
     * 移除token
     *
     * @param token
     */
    void removeToken(String token);

    /**
     * 检查token是否可用
     *
     * @param token
     * @param userId
     * @return
     */
    Boolean checkToken(String token, String userId);


    /**
     * 根据用户token 获取用户信息
     *
     * @param token
     * @return
     */
    LhitSecurityUserPerms<R,U> getPermsByToken(String token);


    U getUserInfoByToken(String token);


    /**
     * token 续期策略
     * @param token
     */
    void delayExpired(String token);
}
