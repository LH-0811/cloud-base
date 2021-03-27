package com.cloud.base.security.customer_login.username_pwd;

import com.cloud.base.core.modules.sercurity.defense.pojo.verification.LhitSecurityUserVerification;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsernamePasswordUserVerification implements LhitSecurityUserVerification {


    // 用户名
    private String username;

    // 密码
    private String password;


}
