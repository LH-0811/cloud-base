package com.cloud.base.member.property.repository.entity;

import java.util.Date;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import io.swagger.annotations.ApiModelProperty;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.*;
/**
 * 优惠券模板表(PropCouponTemplate)实体类
 *
 * @author lh0811
 * @since 2021-05-31 16:28:47
 */
@Setter
@Getter
@Table(name="prop_coupon_template")
public class PropCouponTemplate implements Serializable {

    /**
     * 主键
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="主键")
    private Long id;

    /**
     * 商户基本信息id
     */
    @ApiModelProperty(value="商户基本信息id")
    private Long mchtBaseInfoId;

    @ApiModelProperty(value="商户名称")
    private String mchtName;

    @ApiModelProperty(value="商户地址")
    private String mchtAddress;

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
