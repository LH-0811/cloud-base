package com.cloud.base.member.user.repository.param;

import com.cloud.base.core.common.entity.CommonEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 系统-系统用户表(SysUser)实体类
 *
 * @author lh0811
 * @since 2021-01-18 10:29:35
 */
@Setter
@Getter
public class SysUserQueryParam implements Serializable {

    /**
     * 用户名
     */
    @ApiModelProperty(value="用户名")
    private String name;

    /**
     * 联系电话
     */
    @ApiModelProperty(value="联系电话")
    private String phone;

    /**
     * 是否已经删除
     */
    @ApiModelProperty(value="是否已经删除")
    private Boolean delFlag;

    @ApiModelProperty(value="页码 默认1")
    private Integer pageNum = CommonEntity.pageNum;

    @ApiModelProperty(value="每页条数 默认15")
    private Integer pageSize = CommonEntity.pageSize;

}
