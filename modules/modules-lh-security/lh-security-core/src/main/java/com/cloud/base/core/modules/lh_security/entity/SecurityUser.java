package com.cloud.base.core.modules.lh_security.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author lh0811
 * @date 2021/5/7
 */
@ApiModel(value = "安全框架用户信息")
public class SecurityUser {

    @ApiModelProperty(value = "用户id")
    private String id;

    @ApiModelProperty(value = "用户名")
    private String username;

}
