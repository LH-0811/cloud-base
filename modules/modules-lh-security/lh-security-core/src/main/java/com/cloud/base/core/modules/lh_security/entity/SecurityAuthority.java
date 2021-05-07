package com.cloud.base.core.modules.lh_security.entity;


import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 权限信息类
 *
 * @author lh0811
 * @date 2021/5/7
 */
@Getter
@Setter
public class SecurityAuthority {

    @ApiModelProperty(value = "用户信息")
    private SecurityUser securityUser;

    @ApiModelProperty(value = "角色信息")
    private List<SecurityRole> securityRoleList;

    @ApiModelProperty(value = "资源列表")
    private List<SecurityRes> securityResList;

}
