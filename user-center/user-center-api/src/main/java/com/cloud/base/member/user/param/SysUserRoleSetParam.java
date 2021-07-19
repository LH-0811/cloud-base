package com.cloud.base.member.user.param;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 系统-系统用户表(SysUser)实体类
 *
 * @author lh0811
 * @since 2021-01-18 10:29:35
 */
@Setter
@Getter
public class SysUserRoleSetParam implements Serializable {

    @NotNull(message = "未上传 系统用户id")
    @ApiModelProperty(value = "系统用户id", required = true)
    private Long sysUserId;

    @ApiModelProperty(value = "系统角色id列表")
    private List<Long> roleIds = Lists.newArrayList();

    @ApiModelProperty(value = "如果是哨点管理员 则需要上传哨点id")
    private Long sentryPostId;

    @ApiModelProperty(value = "如果是网格管理员 则需要上传管理地区编号")
    private String regionCode;

}
