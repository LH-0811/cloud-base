package com.cloud.base.user.param;

import com.cloud.base.core.common.entity.CommonEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 系统角色查询 bean
 *
 * @auth lh0811
 * @date 2020/11/2
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SysRoleQueryParam implements Serializable {


    @ApiModelProperty(value = "角色名称")
    private String name;

    @ApiModelProperty(value = "角色编号")
    private String no;

    @ApiModelProperty(value = "状态 是否可用")
    private Boolean status;

    @ApiModelProperty(value = "页码 默认1")
    private Integer pageNum= CommonEntity.pageNum;

    @ApiModelProperty(value = "每页条数 默认15")
    private Integer pageSize = CommonEntity.pageSize;

}
