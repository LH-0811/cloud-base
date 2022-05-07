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
public class SysResSimpleVo implements Serializable {

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
     * 是否是叶子节点
     */
    @ApiModelProperty(value = "是否是叶子节点")
    private Boolean isLeaf;

    @ApiModelProperty(value = "子资源列表")
    private List<SysResSimpleVo> children;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "标题")
    private String key;

    @ApiModelProperty(value = "标题")
    private String pkey;

    @ApiModelProperty(value = "是否选中")
    private String checked;

}
