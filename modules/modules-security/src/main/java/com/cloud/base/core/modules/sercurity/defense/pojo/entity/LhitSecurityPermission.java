package com.cloud.base.core.modules.sercurity.defense.pojo.entity;

import lombok.*;

import java.util.List;

/**
 * 权限封装类
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LhitSecurityPermission {

    // 资源定位符
    private String url;
    // 所属部门
    private String dept;
    // permsCode 权限码
    private String permsCode;
    // 数据限制
    private List<LhitSecurityDataRule> dateRuleList;

}
