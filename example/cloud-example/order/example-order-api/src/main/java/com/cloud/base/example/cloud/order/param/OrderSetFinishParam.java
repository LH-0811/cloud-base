package com.cloud.base.example.cloud.order.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class OrderSetFinishParam {

    @NotNull(message = "用户id 不能为空")
    @ApiModelProperty(value = "用户id",required = true)
    private Long userId;

    @NotNull(message = "订单id 不能为空")
    @ApiModelProperty(value = "订单id",required = true)
    private Long orderId;


    @Min(value = 0,message = "价格最小为 0")
    @NotNull(message = "价格 不能为空")
    @ApiModelProperty(value = "价格",required = true)
    private BigDecimal money;

}
