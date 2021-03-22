package com.cloud.base.core.modules.sercurity.defense.pojo.verification;

import lombok.*;

/**
 * 默认的用户名密码 身份认证凭证
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultUsernamePasswordUserVerification implements LhitSecurityUserVerification {

    // 用户名
    private String username;

    // 密码
    private String password;

}
