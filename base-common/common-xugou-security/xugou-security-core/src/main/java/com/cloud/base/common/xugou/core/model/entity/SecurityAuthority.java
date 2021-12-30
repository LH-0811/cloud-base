package com.cloud.base.common.xugou.core.model.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "权限信息类")
public class SecurityAuthority {

    @ApiModelProperty(value = "用户token")
    private String token;

    @ApiModelProperty(value = "用户信息")
    private SecurityUser securityUser;

    @ApiModelProperty(value = "角色信息")
    private List<SecurityRole> securityRoleList;

    @ApiModelProperty(value = "资源列表")
    private List<SecurityRes> securityResList;

}
