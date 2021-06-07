package com.cloud.base.member.property.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.assertj.core.util.Lists;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author lh0811
 * @date 2021/6/4
 */
@Getter
@Setter
@ApiModel(value = "用户优惠列表")
public class PropCouponOfUserVo implements Serializable {


    private PropCouponGroupByStatus groupByStatus = new PropCouponGroupByStatus();

    private List<PropCouponGroupByMchtBaseInfo> groupByMcht = Lists.newArrayList();


    @Getter
    @Setter
    @ApiModel(value = "用户优惠按照优惠券状态组合")
    public static class PropCouponGroupByStatus {
        /**
         * 初始化 列表
         */
        @ApiModelProperty(value = "初始化 列表")
        private List<PropCouponDetailInfo> initList = Lists.newArrayList();
        /**
         * 已消费 列表
         */
        @ApiModelProperty(value = "已消费 列表")
        private List<PropCouponDetailInfo> consumedList = Lists.newArrayList();
        /**
         * 已过期 列表
         */
        @ApiModelProperty(value = "已过期 列表")
        private List<PropCouponDetailInfo> overdueList = Lists.newArrayList();
        /**
         * 已失效 列表
         */
        @ApiModelProperty(value = "已失效 列表")
        private List<PropCouponDetailInfo> invalidList = Lists.newArrayList();

    }


    @Getter
    @Setter
    @ApiModel(value = "用户优惠按照商户信息组合")
    public static class PropCouponGroupByMchtBaseInfo {

        @ApiModelProperty(value = "商户 id")
        private Long mchtId;

        @ApiModelProperty(value = "商户 名称")
        private String mchtName;

        @ApiModelProperty(value = "商户 地址")
        private String mchtAddress;

        /**
         * 初始化 列表
         */
        @ApiModelProperty(value = "优惠券 列表")
        private List<PropCouponDetailInfo> couponList;

    }

}
