package com.cloud.base.member.property.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 优惠券模板表(PropCouponTemplate)实体类
 *
 * @author lh0811
 * @since 2021-05-31 16:28:47
 */
@Setter
@Getter
public class PropCouponTemplateUpdateParam implements Serializable {

    /**
     * 主键
     */
    @NotNull(message = "Id不能为空")
    @ApiModelProperty(value="主键")
    private Long id;

    /**
     * 优惠券类型（1-线下 2-满减积分抵扣 3-折扣）
     */
    @ApiModelProperty(value="优惠券类型（1-线下 2-满减积分抵扣 3-折扣）")
    private Integer couponType;
    /**
     * 优惠券描述
     */
    @ApiModelProperty(value="优惠券描述")
    private String describe;
    /**
     * 有效时长(分钟)
     */
    @ApiModelProperty(value="有效时长(分钟)")
    private Integer effectiveMinute;
    /**
     * 满减-满多少
     */
    @ApiModelProperty(value="满减-满多少")
    private Integer fullScore;
    /**
     * 满减-减多少
     */
    @ApiModelProperty(value="满减-减多少")
    private Integer reduceScore;
    /**
     * 折扣产品id
     */
    @ApiModelProperty(value = "产品ID")
    private Long discountProductId;
    /**
     * 满折-满几件
     */
    @ApiModelProperty(value="满折-满几件")
    private Integer fullNumber;
    /**
     * 满折-打几折
     */
    @ApiModelProperty(value="满折-打几折")
    private Integer reduceDiscount;
    /**
     * 线下能力说明
     */
    @ApiModelProperty(value="线下能力说明")
    private String offlineFunction;
    /**
     * 是否可用
     */
    @ApiModelProperty(value="是否可用")
    private Boolean enableFlag;



}
