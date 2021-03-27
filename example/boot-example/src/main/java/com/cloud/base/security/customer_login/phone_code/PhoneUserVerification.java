package com.cloud.base.security.customer_login.phone_code;

import com.cloud.base.core.modules.sercurity.defense.pojo.verification.LhitSecurityUserVerification;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneUserVerification implements LhitSecurityUserVerification {


    // 用户名
    private String phone;

    // 密码
    private String code;


}
