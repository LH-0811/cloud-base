package com.cloud.base.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author lh0811
 * @date 2021/8/25
 */
@Getter
@Setter
@ApiModel(value = "用户信息返回vo")
public class UserInfoVo {

    @ApiModelProperty(value = "用户信息")
    private SysUserVo userInfo;

    @ApiModelProperty(value = "菜单信息")
    private List<MenuVo> menuTree;

    @ApiModelProperty(value = "菜单列表")
    private List<SysResVo> menuList;

}
