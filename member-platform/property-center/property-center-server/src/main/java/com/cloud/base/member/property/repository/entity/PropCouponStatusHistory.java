package com.cloud.base.member.property.repository.entity;

import java.util.Date;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import io.swagger.annotations.ApiModelProperty;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.*;
/**
 * 资产-优惠券生命周期历史(PropCouponStatusHistory)实体类
 *
 * @author lh0811
 * @since 2021-06-08 20:47:09
 */
@Setter
@Getter
@Table(name="prop_coupon_status_history")
public class PropCouponStatusHistory implements Serializable {

    /**
     * 主键
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="主键")
    private Long id;

    /**
     * 优惠券id
     */
    @ApiModelProperty(value="优惠券id")
    private Long couponInfoId;
    /**
     * 状态：0-初始化创建 1-消费 2-过期 3-失效
     */
    @ApiModelProperty(value="状态：0-初始化创建 1-消费 2-过期 3-失效")
    private Integer status;
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