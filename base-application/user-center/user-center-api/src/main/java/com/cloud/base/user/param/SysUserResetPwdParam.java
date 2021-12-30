package com.cloud.base.user.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class SysUserResetPwdParam implements Serializable {

    /**
     * 系统用户id
     */
    @NotNull(message = "未上传 系统用户id")
    @ApiModelProperty(value="系统用户id",required = true)
    private Long userId;


}
