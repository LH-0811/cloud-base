package com.cloud.base.member.merchant.repository.entity;

import java.util.Date;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.annotations.ApiModelProperty;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;

/**
 * 商户中心-商户福利信息设置(MchtGiftSettings)实体类
 *
 * @author lh0811
 * @since 2021-05-26 22:06:01
 */
@Setter
@Getter
@Table(name = "mcht_gift_settings")
public class MchtGiftSettings implements Serializable {

    /**
     * 商户礼物配置注解id
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "商户礼物配置注解id")
    private Long id;

    /**
     * 商户基本信息id
     */
    @ApiModelProperty(value = "商户基本信息id")
    private Long mchtId;
    /**
     * 消费积分奖励 (消费金额-积分)
     */
    @ApiModelProperty(value = "消费积分奖励 (消费金额-积分)")
    private String consumeScoreGift;
    /**
     * 是否有生日福利
     */
    @ApiModelProperty(value = "是否有生日福利")
    private Boolean birthdayGiftFlag;
    /**
     * 生日：1-积分 2-优惠券
     */
    @ApiModelProperty(value = "生日：1-积分 2-优惠券")
    private Integer birthdayGiftType;
    /**
     * 生日：奖励积分数
     */
    @ApiModelProperty(value = "生日：奖励积分数")
    private Integer birthdayGiftScore;
    /**
     * 生日：奖励优惠券模板
     */
    @ApiModelProperty(value = "生日：奖励优惠券模板")
    private Long birthdayGiftCouponsTemplateId;
    /**
     * 是否有月度福利
     */
    @ApiModelProperty(value = "是否有月度福利")
    private Boolean monthGiftFlag;
    /**
     * 月度：1-积分 2-优惠券
     */
    @ApiModelProperty(value = "月度：1-积分 2-优惠券")
    private Integer monthGiftType;
    /**
     * 月度：奖励积分数
     */
    @ApiModelProperty(value = "月度：奖励积分数")
    private Integer monthGiftScore;
    /**
     * 月度：奖励优惠券模板
     */
    @ApiModelProperty(value = "月度：奖励优惠券模板")
    private Long monthGiftCouponsTemplateId;
    /**
     * 月度：奖励时间(日 例如 1 表示每月一号)
     */
    @ApiModelProperty(value = "月度：奖励时间(日 例如 1 表示每月一号)")
    private Integer monthGiftDay;
    /**
     * 是否有年度福利
     */
    @ApiModelProperty(value = "是否有年度福利")
    private Boolean yearGiftFlag;
    /**
     * 年度：1-积分 2-优惠券
     */
    @ApiModelProperty(value = "年度：1-积分 2-优惠券")
    private Integer yearGiftType;
    /**
     * 年度：奖励积分数
     */
    @ApiModelProperty(value = "年度：奖励积分数")
    private Integer yearGiftScore;
    /**
     * 年度：奖励优惠券模板
     */
    @ApiModelProperty(value = "年度：奖励优惠券模板")
    private Long yearGiftCouponsTemplateId;
    /**
     * 年度：奖励发放日期 （MM-dd）
     */
    @ApiModelProperty(value = "年度：奖励发放日期 （MM-dd）")
    private String yearGiftMonthDay;
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


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public enum GiftType {
        SCORE(1,"积分"),
        COUPON(2,"优惠券");

        private Integer code;
        private String msg;
    }
}
