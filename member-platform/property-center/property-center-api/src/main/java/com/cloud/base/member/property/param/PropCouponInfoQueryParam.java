package com.cloud.base.member.property.param;

import com.cloud.base.core.common.entity.CommonEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author lh0811
 * @date 2021/6/1
 */
@Getter
@Setter
public class PropCouponInfoQueryParam  implements Serializable {

    /**
     * 优惠券类型（1-线下 2-满减积分抵扣 3-折扣）
     */
    @ApiModelProperty(value="优惠券类型（1-线下 2-满减积分抵扣 3-折扣）")
    private Integer couponType;

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
    /**
     * 过期时间
     */
    @ApiModelProperty(value = "过期时间(起)")
    private Date expiryTimeLow;
    /**
     * 过期时间
     */
    @ApiModelProperty(value = "过期时间(止)")
    private Date expiryTimeUp;
    /**
     * 状态(0-初始化 1-已消费 2-已过期 3-已失效)
     */
    @ApiModelProperty(value = "状态(0-初始化 1-已消费 2-已过期 3-已失效)")
    private Integer status;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间(起)")
    private Date createTimeLow;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间(起)")
    private Date createTimeUp;
    

    @ApiModelProperty(value = "页码")
    private Integer pageNum = CommonEntity.pageNum;

    @ApiModelProperty(value = "每页条数")
    private Integer pageSize = CommonEntity.pageSize;

}
