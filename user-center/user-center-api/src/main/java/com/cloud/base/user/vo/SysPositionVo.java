package com.cloud.base.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author lh0811
 * @date 2021/8/18
 */
@Getter
@Setter
public class SysPositionVo {


    /**
     * 岗位id
     */
    @ApiModelProperty(value="岗位id")
    private Long id;

    /**
     * 岗位编号
     */
    @ApiModelProperty(value="岗位编号")
    private String no;
    /**
     * 岗位名称
     */
    @ApiModelProperty(value="岗位名称")
    private String name;
    /**
     * 部门备注
     */
    @ApiModelProperty(value="部门备注")
    private String notes;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private Date createTime;


}
