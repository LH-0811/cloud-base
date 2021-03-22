package com.cloud.base.login;

import com.cloud.base.core.modules.sercurity.defense.adapter.LhitSecurityTokenManagerAdapter;
import com.cloud.base.core.modules.sercurity.defense.adapter.LhitSecurityUserAuthenticationLoginAdapter;
import com.cloud.base.core.modules.sercurity.defense.pojo.entity.LhitSecurityRole;
import com.cloud.base.core.modules.sercurity.defense.pojo.user.LhitSecurityUser;
import com.cloud.base.core.modules.sercurity.defense.pojo.verification.DefaultUsernamePasswordUserVerification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoAuthenticationController {

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

//
//    @GetMapping("/custom/login/{username}/{password}")
//    public String customLogin(@PathVariable String username, @PathVariable String password) throws Exception {
//        UsernamePasswordUserVerification verification = new UsernamePasswordUserVerification(username, password);
//        String token = userAuthenticationLoginAdapter.userAuthenticationLogin(verification);
//        return "customLogin登录成功,token:" + token;
//    }
//
//
//    @GetMapping("/phone/login/{phone}/{code}")
//    public String phoneLogin(@PathVariable String phone, @PathVariable String code) throws Exception {
//        PhoneUserVerification phoneUserVerification = PhoneUserVerification.builder().phone(phone).code(code).build();
//        String token = userAuthenticationLoginAdapter.userAuthenticationLogin(phoneUserVerification);
//        return "phoneLogin登录成功,token:" + token;
//    }
//
//
//    @GetMapping("/info")
//    public String userInfo(@RequestHeader(value = "LHTOKEN", defaultValue = "") String token) throws Exception {
//        if (StringUtils.isEmpty(token)) {
//            return "用户token不能为空";
//        }
//        LhitSecurityUserPerms<LhitSecurityRole, LhitSecurityUser> permsByToken = lhitSecurityTokenManagerAdapter.getPermsByToken(token);
//        LhitSecurityUser userInfoByToken = JSON.parseObject(JSONObject.toJSONString(lhitSecurityTokenManagerAdapter.getUserInfoByToken(token)),LhitSecurityUser.class)  ;
//        Map<String, Object> map = new HashMap<>();
//        map.put("perms", permsByToken);
//        map.put("userInfo", userInfoByToken);
//        return JSONObject.toJSONString(map);
//    }


}
