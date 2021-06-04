package com.cloud.base.member.property.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author lh0811
 * @date 2021/6/4
 */
@Getter
@Setter
public class PropCouponDetailVo  implements Serializable {

    @ApiModelProperty(value = "优惠券模板信息")
    private PropCouponTemplateVo propCouponTemplateVo;

    @ApiModelProperty(value = "优惠券信息")
    private PropCouponInfoVo couponInfoVo;

}
