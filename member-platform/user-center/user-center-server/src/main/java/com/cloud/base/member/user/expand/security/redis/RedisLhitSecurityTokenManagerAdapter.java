package com.cloud.base.member.user.expand.security.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.modules.sercurity.defense.adapter.LhitSecurityTokenManagerAdapter;
import com.cloud.base.core.modules.sercurity.defense.pojo.entity.LhitSecurityUserPerms;
import com.cloud.base.core.modules.sercurity.defense.properties.LhitSecurityProperties;
import com.cloud.base.member.user.repository.entity.SysRole;
import com.cloud.base.member.user.repository.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisLhitSecurityTokenManagerAdapter<U, R> implements LhitSecurityTokenManagerAdapter<SysUser, SysRole> {


    @Autowired
    private LhitSecurityProperties lhitSecurityProperties;


    @Autowired
    private RedisTemplate<String, String> reidsClient;


    public static final String UserTokenPrefix = "lhit:security:userId:token:";
    public static final String TokenPermsPrefix = "lhit:security:token:perms:";

    public RedisLhitSecurityTokenManagerAdapter() {

    }


    @Override
    public void saveToken(String token, LhitSecurityUserPerms perms) throws CommonException {

        if (StringUtils.isEmpty(token))
            throw CommonException.create(ServerResponse.createByError("保存用户token时，token不能为空"));

        if (perms.getUser() == null)
            throw CommonException.create(ServerResponse.createByError("保存用户token时，userId不能为空"));

        if (perms == null)
            throw CommonException.create(ServerResponse.createByError("保存用户token时，用户权限不能为空"));

        // 保存token与权限的关系
        reidsClient.opsForValue().set(UserTokenPrefix + perms.getUserId(), token, Integer.valueOf(lhitSecurityProperties.getExpire()), TimeUnit.MINUTES);
        reidsClient.opsForValue().set(TokenPermsPrefix + token, JSONObject.toJSONString(perms), Integer.valueOf(lhitSecurityProperties.getExpire()), TimeUnit.MINUTES);

    }

    @Override
    public void removeToken(String token) {

        // 获取到token对应的权限信息
        LhitSecurityUserPerms<SysRole, SysUser> permsByToken = getPermsByToken(token);
        if (permsByToken == null){
            return;
        }
        LhitSecurityUserPerms perms = JSONObject.parseObject(JSON.toJSONString(permsByToken), LhitSecurityUserPerms.class);
        if (perms == null) return;
        // 如果存在  移除token 与 perms的缓存
        reidsClient.delete(TokenPermsPrefix + token);
        // 移除掉userId 对应的 token缓存
        reidsClient.delete(UserTokenPrefix + perms.getUserId());

    }

    @Override
    public Boolean checkToken(String token, String userId) {
        // 获取到用户id对应的token
        String saveToken = reidsClient.opsForValue().get(UserTokenPrefix + userId);
        if (StringUtils.isEmpty(saveToken)) return false;
        // 判断token是否正确
        return token.equals(saveToken);
    }

    @Override
    public LhitSecurityUserPerms<SysRole, SysUser> getPermsByToken(String token) {

        String permsStr = reidsClient.opsForValue().get(TokenPermsPrefix + token);
        if (StringUtils.isEmpty(permsStr)){
            return null;
        }
        // 获取token 存在的有效权限信息
        LhitSecurityUserPerms perms = JSONObject.parseObject(permsStr,new TypeReference<LhitSecurityUserPerms<SysRole, SysUser>>() {});
        if (perms == null) return null;
        // 完成验证后 返回权限信息
        return perms;
    }

    @Override
    public SysUser getUserInfoByToken(String token) {

        if (StringUtils.isEmpty(token)) return null;

        // 获取token 存在的有效权限信息
        String permsStr = reidsClient.opsForValue().get(TokenPermsPrefix + token);
        if (StringUtils.isEmpty(permsStr)){
            return null;
        }
        LhitSecurityUserPerms<SysRole, SysUser> perms = JSONObject.parseObject(permsStr,new TypeReference<LhitSecurityUserPerms<SysRole, SysUser>>() {});

        if (perms == null) return null;

        return perms.getUser();

    }

    @Override
    public void delayExpired(String token) {

        if (StringUtils.isEmpty(token)) return;

        // 获取token 存在的有效权限信息
        String permsStr = reidsClient.opsForValue().get(TokenPermsPrefix + token);
        if (StringUtils.isEmpty(permsStr)){
            return;
        }
        LhitSecurityUserPerms<SysRole, SysUser> perms = JSONObject.parseObject(permsStr,new TypeReference<LhitSecurityUserPerms<SysRole, SysUser>>() {});


        // 保存token与权限的关系
        reidsClient.opsForValue().set(UserTokenPrefix + perms.getUserId(), token, Integer.valueOf(lhitSecurityProperties.getExpire()), TimeUnit.MINUTES);
        reidsClient.opsForValue().set(TokenPermsPrefix + token, JSONObject.toJSONString(perms), Integer.valueOf(lhitSecurityProperties.getExpire()), TimeUnit.MINUTES);
    }


}
