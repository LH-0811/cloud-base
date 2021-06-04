package com.cloud.base.member.property.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 优惠券信息表(PropCouponInfo)实体类
 *
 * @author lh0811
 * @since 2021-05-31 16:28:43
 */
@Setter
@Getter
public class PropCouponInfoVo implements Serializable {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long userId;

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
    /**
     * 是否删除
     */
    @ApiModelProperty(value = "是否删除")
    private Boolean delFlag;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Long createBy;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private Long updateBy;
}
