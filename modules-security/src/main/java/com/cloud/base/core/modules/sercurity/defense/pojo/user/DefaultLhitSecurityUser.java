package com.cloud.base.core.modules.sercurity.defense.pojo.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

/**
 * 简单的用户类
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DefaultLhitSecurityUser implements LhitSecurityUser {

    private String userId;

    private String username;

    @JsonIgnore
    private String password;

}
