package com.cloud.base.core.modules.sercurity.defense.util;

import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.sercurity.defense.pojo.entity.LhitSecurityUserPerms;
import com.cloud.base.core.modules.sercurity.defense.properties.LhitSecurityProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author lh0811
 * @date 2021/2/25
 */
@Slf4j
public class JwtUtils {

    @Autowired
    private LhitSecurityProperties lhitSecurityProperties;

    public static final String SUBJECT = "lh-security-subject";
    //秘钥
    public static final String APPSECRET = "lh-security-secret";

    /**
     * 生成jwt
     *
     * @param userPerms
     * @return
     */
    public String geneJsonWebToken(LhitSecurityUserPerms userPerms) {

        String token = Jwts.builder().setSubject(SUBJECT)
                .claim("roles", userPerms.getRoles())
                .claim("permissions", userPerms.getPermissions())
                .claim("userId", userPerms.getUserId())
                .claim("user", userPerms.getUser())
                .claim("userPerms",userPerms)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + (1000 * 60 * 60 * Integer.parseInt(lhitSecurityProperties.getExpire()))))
                .signWith(SignatureAlgorithm.HS256, APPSECRET).compact();

        return token;
    }


    /**
     * 校验token
     *
     * @param token
     * @return
     */
    public Claims checkJWT(String token) throws Exception {
        try {
            final Claims claims = Jwts.parser().setSigningKey(APPSECRET).
                    parseClaimsJws(token).getBody();
            return claims;
        } catch (Exception e) {
            throw CommonException.create(e, ServerResponse.createByError("token无效"));
        }
    }
}
