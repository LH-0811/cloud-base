package com.cloud.base.core.modules.sercurity.defense.adapter.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.cloud.base.core.common.entity.ServerResponse;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.modules.sercurity.defense.adapter.LhitSecurityTokenManagerAdapter;
import com.cloud.base.core.modules.sercurity.defense.pojo.entity.LhitSecurityUserPerms;
import com.cloud.base.core.modules.sercurity.defense.properties.LhitSecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DefaultLhitSecurityTokenManagerAdapter<U, R> implements LhitSecurityTokenManagerAdapter {


    @Autowired
    private LhitSecurityProperties lhitSecurityProperties;

    private Cache<String, String> userIdTokenCache;

    private Cache<String, LhitSecurityUserPerms> tokenPermsCache;

    public DefaultLhitSecurityTokenManagerAdapter() {

    }

    @PostConstruct
    private void init() {
        this.userIdTokenCache = CacheBuilder.newBuilder().expireAfterWrite(Long.valueOf(lhitSecurityProperties.getExpire()), TimeUnit.MINUTES).removalListener(new RemovalListener<String, String>() {
            public void onRemoval(RemovalNotification<String, String> notification) {
            }
        }).build();
        this.tokenPermsCache = CacheBuilder.newBuilder().expireAfterWrite(Long.valueOf(lhitSecurityProperties.getExpire()), TimeUnit.MINUTES).removalListener(new RemovalListener<String, LhitSecurityUserPerms>() {
            public void onRemoval(RemovalNotification<String, LhitSecurityUserPerms> notification) {
            }
        }).build();
    }


    @Override
    public void saveToken(String token, LhitSecurityUserPerms perms) throws Exception {

        if (StringUtils.isEmpty(token))
            throw CommonException.create(ServerResponse.createByError("保存用户token失败，token不能为空"));

        if (perms.getUser() == null)
            throw CommonException.create(ServerResponse.createByError("保存用户token时，userId不能为空"));

        if (perms == null)
            throw CommonException.create(ServerResponse.createByError("保存用户token时，用户权限不能为空"));


        // 如果存在缓存 就先
        String oldToken = userIdTokenCache.getIfPresent(perms.getUserId());
        if (!StringUtils.isEmpty(oldToken)) {
            tokenPermsCache.invalidate(oldToken);
            userIdTokenCache.invalidate(perms.getUserId());
        }

        // 保存token与权限的关系
        userIdTokenCache.put(perms.getUserId(), token);
        tokenPermsCache.put(token,perms);

    }

    @Override
    public void removeToken(String token) {

        // 获取到token对应的权限信息
        LhitSecurityUserPerms perms = tokenPermsCache.getIfPresent(token);
        if (perms == null) return;

        // 如果存在  移除token 与 perms的缓存
        tokenPermsCache.invalidate(token);

        // 移除掉userId 对应的 token缓存
        userIdTokenCache.invalidate(perms.getUserId());

    }

    @Override
    public Boolean checkToken(String token, String userId) {

        // 获取到用户id对应的token
        String saveToken = userIdTokenCache.getIfPresent(userId);
        if (StringUtils.isEmpty(saveToken)) return false;

        // 判断token是否正确
        return token.equals(saveToken);
    }

    @Override
    public LhitSecurityUserPerms<R, U> getPermsByToken(String token) {

        // 获取token 存在的有效权限信息
        LhitSecurityUserPerms perms = tokenPermsCache.getIfPresent(token);

        if (perms == null) return null;

        // 完成验证后 返回权限信息
        return perms;
    }

    @Override
    public U getUserInfoByToken(String token) {

        if (StringUtils.isEmpty(token)) return null;

        // 获取token 存在的有效权限信息
        LhitSecurityUserPerms<R, U> perms = tokenPermsCache.getIfPresent(token);

        if (perms == null) return null;

        return perms.getUser();

    }

    @Override
    public void delayExpired(String token) {

        // 获取到权限信息
        LhitSecurityUserPerms perms = tokenPermsCache.getIfPresent(token);

        // 从新保存
        tokenPermsCache.put(token,perms);
        userIdTokenCache.put(perms.getUserId(),token);

    }

}
