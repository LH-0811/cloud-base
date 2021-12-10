package com.cloud.base.modules.security.core.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 安全框架用户信息
 *
 * @author lh0811
 * @date 2021/5/7
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "安全框架用户信息")
public class SecurityUser {

    @ApiModelProperty(value = "用户id")
    private String id;

    @ApiModelProperty(value = "用户名")
    private String username;

}
