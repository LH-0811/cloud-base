package com.cloud.base.member.merchant.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 商户中心-商户福利信息设置(MchtGiftSettings)实体类
 *
 * @author lh0811
 * @since 2021-05-26 22:06:01
 */
@Setter
@Getter
public class MchtGiftSettingsSaveParam implements Serializable {

    /**
     * 商户礼物配置注解id
     */
    @NotNull(message = "id不能为空")
    @ApiModelProperty(value = "商户礼物配置注解id")
    private Long id;

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
     * 生日：0-未设置 1-积分 2-优惠券
     */
    @ApiModelProperty(value = "生日：0-未设置 1-积分 2-优惠券")
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
    @ApiModelProperty(value = "月度：0-未设置 1-积分 2-优惠券")
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
    @Range(min = 1,max = 31,message = "月度奖励时间发放日必须在1-31之间")
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
    @ApiModelProperty(value = "年度：0-未设置 1-积分 2-优惠券")
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
}
