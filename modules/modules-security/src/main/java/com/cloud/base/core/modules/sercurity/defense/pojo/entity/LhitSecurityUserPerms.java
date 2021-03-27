package com.cloud.base.core.modules.sercurity.defense.pojo.entity;

import lombok.*;

import java.util.List;

/**
 * 用户 权限信息包装类
 *
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LhitSecurityUserPerms<R,U> {

    // 用户角色
    private List<R> roles;

    // 用户权限列表
    private List<LhitSecurityPermission> permissions;

    // 用户id
    private String userId;

    // 用户信息
    private U user;

}
