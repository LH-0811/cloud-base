package com.cloud.base.example.cloud.order.repository.entity;

import java.math.BigDecimal;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import io.swagger.annotations.ApiModelProperty;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.*;
/**
 * (TOrder)实体类
 *
 * @author lh0811
 * @since 2021-03-25 20:54:29
 */
@Setter
@Getter
@Table(name="t_order")
public class TOrder implements Serializable {

    @Id
    @KeySql(useGeneratedKeys = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
