package com.cloud.base.member.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 角色
 *
 * @author lh0811
 * @date 2021/5/28
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum RoleConstant {


    SYS_ADMIN(1L,"ROLE001","系统管理员"),
    MCHT_ADMIN(2L,"ROLE002","商户管理员"),
    ;


    // 角色id
    private Long roleId;

    // 角色no
    private String roleNo;

    // 角色名称
    private String roleName;



}
