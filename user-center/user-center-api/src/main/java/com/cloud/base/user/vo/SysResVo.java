package com.cloud.base.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 系统-资源表(SysRes)实体类
 *
 * @author lh0811
 * @since 2021-01-18 10:29:35
 */
@Setter
@Getter
public class SysResVo implements Serializable {

    /**
     * 资源id
     */
    @ApiModelProperty(value = "资源id")
    private Long id;

    /**
     * 父节点id
     */
    @ApiModelProperty(value = "父节点id")
    private Long parentId;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 类型：1-菜单 2-接口 3-权限码,4-静态资源
     */
    @ApiModelProperty(value = "类型：1-菜单 2-接口 3-权限码,4-静态资源")
    private Integer type;

    /**
     * 权限标识符
     */
    @ApiModelProperty(value = "权限标识符")
    private String code;

    /**
     * 请求地址
     */
    @ApiModelProperty(value = "请求地址")
    private String url;
    /**
     * 图标
     */
    @ApiModelProperty(value = "图标")
    private String icon;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer orderNo;
    /**
     * 是否是叶子节点
     */
    @ApiModelProperty(value = "是否是叶子节点")
    private Boolean isLeaf;
    /**
     * 路由
     */
    @ApiModelProperty(value = "路由")
    private String router;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String notes;



    @ApiModelProperty(value = "子资源列表")
    private List<SysResVo> children;

    @ApiModelProperty(value = "父节点")
    private SysResVo parent;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "标题")
    private String key;

    @ApiModelProperty(value = "标题")
    private String pkey;

    @ApiModelProperty(value = "是否选中")
    private String checked;

}
