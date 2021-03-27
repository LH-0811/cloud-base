package com.cloud.base.security.adapter.tokenmgr.reids;

//import com.alibaba.fastjson.JSONObject;
//import com.alibaba.fastjson.TypeReference;
//import com.lhit.starter.security.defense.adapter.LhitSecurityTokenManagerAdapter;
//import com.lhit.starter.security.defense.exception.UserTokenException;
//import com.lhit.starter.security.defense.pojo.entity.LhitSecurityUserPerms;
//import com.lhit.starter.security.defense.properties.LhitSecurityProperties;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//
//import java.util.concurrent.TimeUnit;

//@Slf4j
public class RedisLhitSecurityTokenManagerAdapter<U, R> /*implements LhitSecurityTokenManagerAdapter*/ {

//
//    @Autowired
//    private LhitSecurityProperties lhitSecurityProperties;
//
//
//    @Autowired
//    private RedisTemplate<String, String> reidsClient;
//
//
//    public static final String UserTokenPrefix = "lhit:security:userId:token:";
//    public static final String TokenPermsPrefix = "lhit:security:token:perms:";
//
//    public RedisLhitSecurityTokenManagerAdapter() {
//
//    }
//
//
//    @Override
//    public void saveToken(String token, LhitSecurityUserPerms perms) throws UserTokenException {
//
//        if (StringUtils.isEmpty(token))
//            throw UserTokenException.create("保存用户token时，token不能为空");
//
//        if (perms.getUser() == null)
//            throw UserTokenException.create("保存用户token时，userId不能为空");
//
//        if (perms == null)
//            throw UserTokenException.create("保存用户token时，用户权限不能为空");
//
//        // 保存token与权限的关系
//        reidsClient.opsForValue().set(UserTokenPrefix + perms.getUserId(), token, Integer.valueOf(lhitSecurityProperties.getExpire()), TimeUnit.MINUTES);
//        reidsClient.opsForValue().set(TokenPermsPrefix + token, JSONObject.toJSONString(perms), Integer.valueOf(lhitSecurityProperties.getExpire()), TimeUnit.MINUTES);
//
//    }
//
//    @Override
//    public void removeToken(String token) {
//
//        // 获取到token对应的权限信息
//        String permsStr = reidsClient.opsForValue().get(token);
//        if (StringUtils.isEmpty(permsStr)){
//            return;
//        }
//        LhitSecurityUserPerms perms = JSONObject.parseObject(permsStr, LhitSecurityUserPerms.class);
//        if (perms == null) return;
//        // 如果存在  移除token 与 perms的缓存
//        reidsClient.delete(TokenPermsPrefix + token);
//        // 移除掉userId 对应的 token缓存
//        reidsClient.delete(UserTokenPrefix + perms.getUserId());
//
//    }
//
//    @Override
//    public Boolean checkToken(String token, String userId) {
//        // 获取到用户id对应的token
//        String saveToken = reidsClient.opsForValue().get(UserTokenPrefix + userId);
//        if (StringUtils.isEmpty(saveToken)) return false;
//        // 判断token是否正确
//        return token.equals(saveToken);
//    }
//
//    @Override
//    public LhitSecurityUserPerms<R, U> getPermsByToken(String token) {
//
//        String permsStr = reidsClient.opsForValue().get(TokenPermsPrefix + token);
//        if (StringUtils.isEmpty(permsStr)){
//            return null;
//        }
//        // 获取token 存在的有效权限信息
//        LhitSecurityUserPerms perms = JSONObject.parseObject(permsStr,new TypeReference<LhitSecurityUserPerms<R,U>>() {});
//        if (perms == null) return null;
//        // 完成验证后 返回权限信息
//        return perms;
//    }
//
//    @Override
//    public U getUserInfoByToken(String token) {
//
//        if (StringUtils.isEmpty(token)) return null;
//
//        // 获取token 存在的有效权限信息
//        String permsStr = reidsClient.opsForValue().get(TokenPermsPrefix + token);
//        if (StringUtils.isEmpty(permsStr)){
//            return null;
//        }
//        LhitSecurityUserPerms<R,U> perms = JSONObject.parseObject(permsStr,new TypeReference<LhitSecurityUserPerms<R,U>>() {});
//
//        if (perms == null) return null;
//
//        return perms.getUser();
//
//    }
//
//    @Override
//    public void delayExpired(String token) {
//
//        if (StringUtils.isEmpty(token)) return;
//
//        // 获取token 存在的有效权限信息
//        String permsStr = reidsClient.opsForValue().get(TokenPermsPrefix + token);
//        if (StringUtils.isEmpty(permsStr)){
//            return;
//        }
//        LhitSecurityUserPerms<R,U> perms = JSONObject.parseObject(permsStr,new TypeReference<LhitSecurityUserPerms<R,U>>() {});
//
//
//        // 保存token与权限的关系
//        reidsClient.opsForValue().set(UserTokenPrefix + perms.getUserId(), token, Integer.valueOf(lhitSecurityProperties.getExpire()), TimeUnit.MINUTES);
//        reidsClient.opsForValue().set(TokenPermsPrefix + token, JSONObject.toJSONString(perms), Integer.valueOf(lhitSecurityProperties.getExpire()), TimeUnit.MINUTES);
//    }


}
