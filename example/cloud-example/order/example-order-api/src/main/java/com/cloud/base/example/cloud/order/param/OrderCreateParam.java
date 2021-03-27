package com.cloud.base.example.cloud.order.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class OrderCreateParam {

    @NotNull(message = "用户id 不能为空")
    @ApiModelProperty(value = "用户id",required = true)
    private Long userId;


    @NotNull(message = "产品id 不能为空")
    @ApiModelProperty(value = "产品id",required = true)
    private Long productId;

    @Min(value = 0,message = "数量最小为 0")
    @NotNull(message = "数量 不能为空")
    @ApiModelProperty(value = "数量",required = true)
    private Integer count;

}
