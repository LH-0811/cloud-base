package com.cloud.base.user.repository_plus.entity;

import java.util.Date;
import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;
import io.swagger.annotations.ApiModelProperty;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 系统-资源表(SysRes)实体类
 *
 * @author lh0811
 * @since 2021-11-03 22:28:40
 */
@Setter
@Getter
@TableName("sys_res")
public class SysRes implements Serializable {

    /**
     * 资源id
     */
    @TableId(type = IdType.NONE)
    @ApiModelProperty(value="资源id")
    private Long id;

    /**
     * 父节点id
     */
    @ApiModelProperty(value="父节点id")
    private Long parentId;
    /**
     * 名称
     */
    @ApiModelProperty(value="名称")
    private String name;
    /**
     * 类型：0-菜单组 1-"菜单" 2-"接口" 3-"权限码",4-"静态资源"
     */
    @ApiModelProperty(value="类型：0-菜单组 1-"菜单" 2-"接口" 3-"权限码",4-"静态资源"")
    private Integer type;
    /**
     * 权限标识符
     */
    @ApiModelProperty(value="权限标识符")
    private String code;
    /**
     * 请求地址
     */
    @ApiModelProperty(value="请求地址")
    private String url;
    /**
     * 图标
     */
    @ApiModelProperty(value="图标")
    private String icon;
    /**
     * 排序
     */
    @ApiModelProperty(value="排序")
    private Integer orderNo;
    /**
     * 是否是叶子节点
     */
    @ApiModelProperty(value="是否是叶子节点")
    private Boolean isLeaf;
    /**
     * 路由
     */
    @ApiModelProperty(value="路由")
    private String router;
    /**
     * 备注
     */
    @ApiModelProperty(value="备注")
    private String notes;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private Date createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间")
    private Date updateTime;
    /**
     * 创建人
     */
    @ApiModelProperty(value="创建人")
    private Long createBy;
    /**
     * 更新人
     */
    @ApiModelProperty(value="更新人")
    private Long updateBy;



    @TableField(exist = false)
    @ApiModelProperty(value = "子资源列表")
    private List<SysRes> children;

    @TableField(exist = false)
    @ApiModelProperty(value = "父节点")
    private SysRes parent;

    @TableField(exist = false)
    @ApiModelProperty(value = "标题")
    private String title;

    @TableField(exist = false)
    @ApiModelProperty(value = "标题")
    private String key;

    @TableField(exist = false)
    @ApiModelProperty(value = "标题")
    private String pkey;

    @TableField(exist = false)
    @ApiModelProperty(value = "是否选中")
    private Boolean checked;



}
