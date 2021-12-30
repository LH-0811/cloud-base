package com.cloud.base.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.util.List;

/**
 * @author lh0811
 * @date 2021/8/18
 */
@Getter
@Setter
public class SysDeptVo {

    /**
     * 部门id
     */
    @ApiModelProperty(value = "部门id")
    private Long id;


    @ApiModelProperty(value = "父级部门id")
    private Long parentId;

    /**
     * 部门编号
     */
    @ApiModelProperty(value = "部门编号")
    private String no;

    @ApiModelProperty(value = "是否是叶子节点")
    private Boolean isLeaf;

    /**
     * 路由
     */
    @ApiModelProperty(value = "路由")
    private String router;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    private String name;
    /**
     * 部门备注
     */
    @ApiModelProperty(value = "部门备注")
    private String notes;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Long createBy;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private Long updateBy;

    ////////////////////子数据
    @ApiModelProperty(value = "子资源列表")
    private List<SysDeptVo> children;


    ////////////////////树形数据相关参数
    @ApiModelProperty(value = "父节点")
    private SysDeptVo parent;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "本节点key")
    private String key;

    @ApiModelProperty(value = "父级key")
    private String pkey;


    ////////////////////树形数据相关参数
    @ApiModelProperty(value = "级联值")
    private String value;

    @ApiModelProperty(value = "级联文本")
    private String label;
}
