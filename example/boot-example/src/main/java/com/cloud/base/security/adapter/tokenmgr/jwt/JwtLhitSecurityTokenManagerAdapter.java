package com.cloud.base.security.adapter.tokenmgr.jwt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.modules.sercurity.defense.adapter.LhitSecurityTokenManagerAdapter;
import com.cloud.base.core.modules.sercurity.defense.pojo.entity.LhitSecurityUserPerms;
import com.cloud.base.core.modules.sercurity.defense.util.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

@Slf4j
//@Component
public class JwtLhitSecurityTokenManagerAdapter<U, R> implements LhitSecurityTokenManagerAdapter {

    @Autowired
    private JwtUtils jwtUtils;

    public JwtLhitSecurityTokenManagerAdapter() {
    }


    @Override
    public void saveToken(String token, LhitSecurityUserPerms perms) throws CommonException {
        log.info("JWT 方式不需要存在用户token");
    }

    @Override
    public void removeToken(String token) {
        log.info("JWT 方式用户token无法移除");
    }

    @Override
    public Boolean checkToken(String token, String userId) {
        Claims claims = null;
        try {
            claims = jwtUtils.checkJWT(token);
        } catch (Exception e) {
            return false;
        }
        String userIdClaims = (String) claims.get("userId");
        // 判断token是否正确
        return userId.equals(userIdClaims);
    }

    @Override
    public LhitSecurityUserPerms<R, U> getPermsByToken(String token) {

        Claims claims = null;
        try {
            claims = jwtUtils.checkJWT(token);
        } catch (Exception e) {
            return null;
        }
        LhitSecurityUserPerms<R, U> userPerms = JSON.parseObject(JSON.toJSONString(claims.get("userPerms")) ,new TypeReference<LhitSecurityUserPerms<R,U>>() {}) ;
        // 完成验证后 返回权限信息
        return userPerms;
    }

    @Override
    public U getUserInfoByToken(String token) {
        if (StringUtils.isEmpty(token)) return null;
        Claims claims = null;
        try {
            claims = jwtUtils.checkJWT(token);
        } catch (Exception e) {
            return null;
        }
        LhitSecurityUserPerms<R, U> perms =  JSON.parseObject(JSON.toJSONString(claims.get("userPerms")) ,new TypeReference<LhitSecurityUserPerms<R,U>>() {}) ;
        if (perms == null) return null;
        return perms.getUser();
    }

    @Override
    public void delayExpired(String token) {
        log.info("JWT 方式无法续期token");
    }


}
