package com.cloud.base.member.property.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author lh0811
 * @date 2021/6/8
 */
@Getter
@Setter
public class PropScoreAccountCreateParam {

    @NotNull(message = "未上传 用户id")
    @ApiModelProperty(value = "用户id")
    private Long userId;

    @NotNull(message = "未上传 商户")
    @ApiModelProperty(value = "商户id")
    private Long mchtId;

}
