package com.cloud.base.member.property.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author lh0811
 * @date 2021/6/1
 */
@Getter
@Setter
public class PropCouponInfoCreateParam implements Serializable {

    @NotNull(message = "未制定优惠券所属用户")
    @ApiModelProperty(value = "优惠券用户id",required = true)
    private Long userId;

    /**
     * 模板id
     */
    @NotNull(message = "未指定模板id")
    @ApiModelProperty(value="模板id",required = true)
    private Long templateId;


}
