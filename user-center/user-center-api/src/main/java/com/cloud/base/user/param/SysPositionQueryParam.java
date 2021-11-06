package com.cloud.base.user.param;

import com.cloud.base.core.common.entity.CommonEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户中心-岗位信息(SysPosition)实体类
 *
 * @author lh0811
 * @since 2021-08-10 21:55:23
 */
@Setter
@Getter
public class SysPositionQueryParam implements Serializable {


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

    @ApiModelProperty(value = "最早创建时间")
    private Date createTimeLow;

    @ApiModelProperty(value = "最晚创建时间")
    private Date createTimeUp;

    @ApiModelProperty(value = "页码")
    private Integer pageNum = CommonEntity.pageNum;

    @ApiModelProperty(value = "每页数据条数")
    private Integer pageSize = CommonEntity.pageSize;


}
