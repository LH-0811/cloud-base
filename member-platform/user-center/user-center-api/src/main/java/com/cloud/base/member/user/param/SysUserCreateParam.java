package com.cloud.base.member.user.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统-系统用户表(SysUser)实体类
 *
 * @author lh0811
 * @since 2021-01-18 10:29:35
 */
@Setter
@Getter
public class SysUserCreateParam implements Serializable {



    /**
     * 用户类型 1-系统管理员 2-商户 3-C端客户
     */
    @NotNull(message = "用户类型不能为空")
    @Range(min = 1,max = 3,message = "用户类型不合法")
    @ApiModelProperty(value = "用户类型 1-系统管理员 2-商户 3-C端客户",required = true)
    private Integer userType;

    /**
     * 用户名
     */
    @NotBlank(message = "未上传 用户名")
    @ApiModelProperty(value = "用户名",required = true)
    private String username;

    /**
     * 电话
     */
    @NotBlank(message = "未上传 电话")
    @ApiModelProperty(value = "电话",required = true)
    private String phone;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String eMail;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickName;

}
