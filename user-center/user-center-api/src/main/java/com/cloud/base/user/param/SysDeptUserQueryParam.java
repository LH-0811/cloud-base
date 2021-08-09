package com.cloud.base.user.param;

import com.cloud.base.core.common.entity.CommonEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 部门用户查询参数
 *
 * @author lh0811
 * @date 2021/8/7
 */
@Getter
@Setter
public class SysDeptUserQueryParam {

    @ApiModelProperty(value = "部门id")
    private Long deptId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "用户手机号")
    private String phone;

    @ApiModelProperty(value = "用户可用状态")
    private Boolean activeFlag;

    @ApiModelProperty(value = "最早创建时间")
    private Date createTimeLow;

    @ApiModelProperty(value = "最晚创建时间")
    private Date createTimeUp;

    @ApiModelProperty(value = "页码")
    private Integer pageNum = CommonEntity.pageNum;

    @ApiModelProperty(value = "每页数据条数")
    private Integer pageSize = CommonEntity.pageSize;


}
