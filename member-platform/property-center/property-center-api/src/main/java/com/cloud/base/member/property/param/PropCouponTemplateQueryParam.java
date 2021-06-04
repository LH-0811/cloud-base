package com.cloud.base.member.property.param;

import com.cloud.base.core.common.entity.CommonEntity;
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
public class PropCouponTemplateQueryParam implements Serializable {

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
     * 满减-满多少
     */
    @ApiModelProperty(value="满减-满多少(起)")
    private Integer fullScoreLow;

    /**
     * 满减-满多少
     */
    @ApiModelProperty(value="满减-满多少(止)")
    private Integer fullScoreUp;
    /**
     * 满减-减多少
     */
    @ApiModelProperty(value="满减-减多少(起)")
    private Integer reduceScoreLow;

    /**
     * 满减-减多少
     */
    @ApiModelProperty(value="满减-减多少(止)")
    private Integer reduceScoreUp;

    /**
     * 满折-满几件
     */
    @ApiModelProperty(value="满折-满几件(起)")
    private Integer fullNumberLow;

    /**
     * 满折-满几件
     */
    @ApiModelProperty(value="满折-满几件(止)")
    private Integer fullNumberUp;

    /**
     * 满折-打几折
     */
    @ApiModelProperty(value="满折-打几折(起)")
    private Integer reduceDiscountLow;
    /**
     * 满折-打几折
     */
    @ApiModelProperty(value="满折-打几折(止)")
    private Integer reduceDiscountUp;

    /**
     * 是否可用
     */
    @ApiModelProperty(value="是否可用")
    private Boolean enableFlag;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间(起)")
    private Date createTimeLow;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间(止)")
    private Date createTimeUp;

    @ApiModelProperty(value = "页码")
    private Integer pageNum = CommonEntity.pageNum;

    @ApiModelProperty(value = "每页条数")
    private Integer pageSize = CommonEntity.pageSize;


}
