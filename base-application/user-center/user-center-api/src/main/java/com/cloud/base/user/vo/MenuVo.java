package com.cloud.base.user.vo;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author lh0811
 * @date 2021/1/20
 */
@Getter
@Setter
public class MenuVo {
    @ApiModelProperty(value = "菜单id")
    private Long id;
    @ApiModelProperty(value = "父节点id")
    private Long parentId;
    @ApiModelProperty(value = "菜单名称")
    private String text;
    @ApiModelProperty(value = "菜单链接")
    private String link;
    @ApiModelProperty(value = "是否是菜单组")
    private Boolean group;
    @ApiModelProperty(value = "菜单icon")
    private JSONObject icon;
    @ApiModelProperty(value = "子菜单icon")
    private List<MenuVo> children;
}
