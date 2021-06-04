package com.cloud.base.member.property.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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
public class PropCouponTemplateVo implements Serializable {

    /**
     * 主键
     */
    @ApiModelProperty(value="主键")
    private Long id;

    /**
     * 商户基本信息id
     */
    @ApiModelProperty(value="商户基本信息id")
    private Long mchtBaseInfoId;
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
    /**
     * 是否删除
     */
    @ApiModelProperty(value="是否删除")
    private Boolean delFlag;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private Date createTime;
    /**
     * 创建人
     */
    @ApiModelProperty(value="创建人")
    private Long createBy;
    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间")
    private Date updateTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value="更新人")
    private Long updateBy;


}
