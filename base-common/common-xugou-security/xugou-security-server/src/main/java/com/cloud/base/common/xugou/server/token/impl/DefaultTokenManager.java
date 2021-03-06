package com.cloud.base.common.xugou.server.token.impl;

import com.cloud.base.common.core.exception.CommonException;
import com.cloud.base.common.core.response.ServerResponse;
import com.cloud.base.common.xugou.core.model.entity.SecurityAuthority;
import com.cloud.base.common.xugou.core.model.properties.XuGouSecurityProperties;
import com.cloud.base.common.xugou.server.token.TokenGenerate;
import com.cloud.base.common.xugou.server.token.TokenManager;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * 默认token管理器实现（基于 guavaCache）
 *
 * @author lh0811
 * @date 2021/5/8
 */
@Slf4j
public class DefaultTokenManager implements TokenManager {


    // token生成器
    @Autowired
    private TokenGenerate tokenGenerate;

    // 配置文件
    @Autowired
    private XuGouSecurityProperties xuGouSecurityProperties;

    // 缓存userId - token 对应关系
    private Cache<String, String> userIdTokenCache;

    // 缓存 token - SecurityAuthority 对应关系
    private Cache<String, SecurityAuthority> tokenAuthorityCache;

    public DefaultTokenManager() {

    }

    // 初始化缓存 过期时间在配置文件中指定
    @PostConstruct
    private void init() {
        this.userIdTokenCache = CacheBuilder.newBuilder().expireAfterAccess(Long.valueOf(xuGouSecurityProperties.getExpire()), TimeUnit.MINUTES)
                .build();
        this.tokenAuthorityCache = CacheBuilder.newBuilder().expireAfterAccess(Long.valueOf(xuGouSecurityProperties.getExpire()), TimeUnit.MINUTES)
                .build();
    }

    @Override
    public String tokenGenerateAndSave(SecurityAuthority securityAuthority) throws Exception {
        // 生成token
        String token = tokenGenerate.generate(securityAuthority);
        securityAuthority.setToken(token);
        // 存储token
        saveToken(token, securityAuthority);
        return token;
    }

    @Override
    public void saveToken(String token, SecurityAuthority authority) throws Exception {
        if (StringUtils.isEmpty(token))
            throw CommonException.create(ServerResponse.createByError("保存用户token失败，token不能为空"));
        if (authority.getSecurityUser() == null)
            throw CommonException.create(ServerResponse.createByError("保存用户token时，用户信息不能为空"));
        if (authority == null)
            throw CommonException.create(ServerResponse.createByError("保存用户token时，用户权限不能为空"));
        // 如果存在缓存 先失效
        String oldToken = userIdTokenCache.getIfPresent(authority.getSecurityUser().getId() + "-" + authority.getClientType());
        if (!StringUtils.isEmpty(oldToken)) {
            tokenAuthorityCache.invalidate(oldToken);
            userIdTokenCache.invalidate(authority.getSecurityUser().getId());
        }
        // 保存token与权限的关系
        userIdTokenCache.put(authority.getSecurityUser().getId() + "-" + authority.getClientType(), token);
        tokenAuthorityCache.put(token, authority);
    }

    @Override
    public void removeToken(String token) throws Exception {
        // 获取到token对应的权限信息
        SecurityAuthority authority = tokenAuthorityCache.getIfPresent(token);
        if (authority == null) return;
        // 如果存在  移除token 与 perms的缓存
        tokenAuthorityCache.invalidate(token);
        // 移除掉userId 对应的 token缓存
        userIdTokenCache.invalidate(authority.getSecurityUser().getId() + "-" + authority.getClientType());
    }

    @Override
    public Boolean checkToken(String token, String userId, String clientType) throws Exception {
        // 获取到用户id对应的token
        String saveToken = userIdTokenCache.getIfPresent(userId + "-" + clientType);
        if (StringUtils.isEmpty(saveToken)) return false;
        // 判断token是否正确
        return token.equals(saveToken);
    }

    @Override
    public SecurityAuthority getSecurityAuthorityByToken(String token) throws Exception {
        if (StringUtils.isBlank(token)) {
            throw CommonException.create(ServerResponse.createByError(Integer.valueOf(xuGouSecurityProperties.getNoAuthorizedCode()), "未上传用户token，请登录后重试", ""));
        }
        // 获取token 存在的有效权限信息
        SecurityAuthority authority = tokenAuthorityCache.getIfPresent(token);
        if (authority == null) {
            throw CommonException.create(ServerResponse.createByError(Integer.parseInt(xuGouSecurityProperties.getNoAuthorizedCode()), "token无效"));
        }
        // 完成验证后 返回权限信息
        return authority;
    }

    @Override
    public void delayExpired(String token) throws Exception {
        //  使用cache中expireAfterAccess策略，则可以不用续期了，每次访问缓存会自动续期。
    }

}
