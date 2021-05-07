package com.cloud.base.member.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AccessTokenVo {

    @ApiModelProperty(value = "accessToken")
    private String access_token;

    @ApiModelProperty(value = "凭证有效时间，单位：秒。目前是7200秒之内的值。")
    private Long expires_in;

    @ApiModelProperty(value = "错误码")
    private Integer errcode;

    @ApiModelProperty(value = "错误信息")
    private String errmsg;

    @ApiModelProperty(value = "获取accessToken的时间")
    private Date get_time;
}
