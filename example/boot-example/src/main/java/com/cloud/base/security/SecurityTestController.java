package com.cloud.base.security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cloud.base.core.modules.sercurity.defense.adapter.LhitSecurityTokenManagerAdapter;
import com.cloud.base.core.modules.sercurity.defense.adapter.LhitSecurityUserAuthenticationLoginAdapter;
import com.cloud.base.core.modules.sercurity.defense.pojo.entity.LhitSecurityRole;
import com.cloud.base.core.modules.sercurity.defense.pojo.entity.LhitSecurityUserPerms;
import com.cloud.base.core.modules.sercurity.defense.pojo.user.LhitSecurityUser;
import com.cloud.base.core.modules.sercurity.defense.pojo.verification.DefaultUsernamePasswordUserVerification;
import com.cloud.base.security.customer_login.phone_code.PhoneUserVerification;
import com.cloud.base.security.customer_login.username_pwd.UsernamePasswordUserVerification;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/security")
@Api(tags = "security 授权鉴权测试接口")
public class SecurityTestController {

    @Autowired
    private LhitSecurityUserAuthenticationLoginAdapter userAuthenticationLoginAdapter;

    @Autowired
    private LhitSecurityTokenManagerAdapter<LhitSecurityUser, LhitSecurityRole> lhitSecurityTokenManagerAdapter;

    @GetMapping("/default/login/{username}/{password}")
    public String defaultLogin(@PathVariable String username, @PathVariable String password) throws Exception {
        DefaultUsernamePasswordUserVerification verification = new DefaultUsernamePasswordUserVerification(username, password);
        String token = userAuthenticationLoginAdapter.userAuthenticationLogin(verification);
        return "defaultLogin登录成功,token:" + token;
    }


    @GetMapping("/custom/login/{username}/{password}")
    public String customLogin(@PathVariable String username, @PathVariable String password) throws Exception {
        UsernamePasswordUserVerification verification = new UsernamePasswordUserVerification(username, password);
        String token = userAuthenticationLoginAdapter.userAuthenticationLogin(verification);
        return "customLogin登录成功,token:" + token;
    }


    @GetMapping("/phone/login/{phone}/{code}")
    public String phoneLogin(@PathVariable String phone, @PathVariable String code) throws Exception {
        PhoneUserVerification phoneUserVerification = PhoneUserVerification.builder().phone(phone).code(code).build();
        String token = userAuthenticationLoginAdapter.userAuthenticationLogin(phoneUserVerification);
        return "phoneLogin登录成功,token:" + token;
    }


    @GetMapping("/info")
    public String userInfo(@RequestHeader(value = "LHTOKEN", defaultValue = "") String token) throws Exception {
        if (StringUtils.isEmpty(token)) {
            return "用户token不能为空";
        }
        LhitSecurityUserPerms<LhitSecurityRole, LhitSecurityUser> permsByToken = lhitSecurityTokenManagerAdapter.getPermsByToken(token);
        LhitSecurityUser userInfoByToken = JSON.parseObject(JSONObject.toJSONString(lhitSecurityTokenManagerAdapter.getUserInfoByToken(token)),LhitSecurityUser.class)  ;
        Map<String, Object> map = new HashMap<>();
        map.put("perms", permsByToken);
        map.put("userInfo", userInfoByToken);
        return JSONObject.toJSONString(map);
    }


    //////////以下两个方法用于测试 用户权限///////////////

    @GetMapping("/res/res1")
    public String res1() {
        return "res1 success";
    }

    @GetMapping("/res/res2")
    public String res2() {
        return "res2 success";
    }

}
