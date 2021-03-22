package com.cloud.base.core.modules.sercurity.defense.pojo.entity;

import lombok.*;

/**
 * 用户token 与 用户基本信息的包装类
 *
 * @param <T> 用户基本信息类
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LhitSecurityTokenUserInfo<T> {

    // 用户token
    private String token;

    // 用户基本信息
    private T userInfo;
}
