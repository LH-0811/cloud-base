package com.cloud.base.member.property.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@ApiModel(value = "优惠券详情")
public class PropCouponDetailInfo {
    ////////模板信息////////////////////////////////////////////////////////////////////////////////////

    /**
     * 模板id
     */
    @ApiModelProperty(value = "模板id")
    private Long templateId;

    /**
     * 商户基本信息id
     */
    @ApiModelProperty(value = "商户基本信息id")
    private Long mchtBaseInfoId;


    @ApiModelProperty(value="商户名称")
    private String mchtName;

    @ApiModelProperty(value="商户地址")
    private String mchtAddress;

    /**
     * 主键
     */
    @ApiModelProperty(value = "优惠券信息")
    private Long couponInfoId;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long userId;

    /**
     * 优惠券类型（1-线下 2-满减积分抵扣 3-折扣）
     */
    @ApiModelProperty(value = "优惠券类型（1-线下 2-满减积分抵扣 3-折扣）")
    private Integer couponType;
    /**
     * 优惠券描述
     */
    @ApiModelProperty(value = "优惠券描述")
    private String describe;
    /**
     * 有效时长(分钟)
     */
    @ApiModelProperty(value = "有效时长(分钟)")
    private Integer effectiveMinute;
    /**
     * 满减-满多少
     */
    @ApiModelProperty(value = "满减-满多少")
    private Integer fullScore;
    /**
     * 满减-减多少
     */
    @ApiModelProperty(value = "满减-减多少")
    private Integer reduceScore;
    /**
     * 满折-满几件
     */
    @ApiModelProperty(value = "满折-满几件")
    private Integer fullNumber;
    /**
     * 满折-打几折
     */
    @ApiModelProperty(value = "满折-打几折")
    private Integer reduceDiscount;
    /**
     * 线下能力说明
     */
    @ApiModelProperty(value = "线下能力说明")
    private String offlineFunction;
    /**
     * 是否可用
     */
    @ApiModelProperty(value = "是否可用")
    private Boolean templateEnableFlag;


    ////////优惠券信息////////////////////////////////////////////////////////////////////////////////////

    /**
     * 过期时间
     */
    @ApiModelProperty(value = "过期时间")
    private Date expiryTime;
    /**
     * 状态(0-初始化 1-已消费 2-已过期 3-已失效)
     */
    @ApiModelProperty(value = "状态(0-初始化 1-已消费 2-已过期 3-已失效)")
    private Integer status;
    /**
     * 失效时间
     */
    @ApiModelProperty(value = "失效时间")
    private Date invalidTime;
    /**
     * 失效操作人
     */
    @ApiModelProperty(value = "失效操作人")
    private Long invalidBy;
    /**
     * 失效原因
     */
    @ApiModelProperty(value = "失效原因")
    private String invalidReason;
}
