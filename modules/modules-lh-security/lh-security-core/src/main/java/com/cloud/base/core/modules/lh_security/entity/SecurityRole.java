package com.cloud.base.core.modules.lh_security.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lh0811
 * @date 2021/5/7
 */
@Getter
@Setter
@ApiModel(value = "安全框架角色信息")
public class SecurityRole {

    @ApiModelProperty(value = "角色名称")
    private String name;

}
