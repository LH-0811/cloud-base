package com.cloud.base.example.cloud.order.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author lh0811
 * @date 2021/3/25
 */
@Getter
@Setter
public class OrderVo implements Serializable {

    @ApiModelProperty(value="${column.comment}")
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value="用户id")
    private Long userId;
    /**
     * 产品id
     */
    @ApiModelProperty(value="产品id")
    private Long productId;
    /**
     * 数量
     */
    @ApiModelProperty(value="数量")
    private Integer count;
    /**
     * 金额
     */
    @ApiModelProperty(value="金额")
    private BigDecimal money;
    /**
     * 订单状态：0创建中，1已完结
     */
    @ApiModelProperty(value="订单状态：0创建中，1已完结")
    private Integer status;

}
