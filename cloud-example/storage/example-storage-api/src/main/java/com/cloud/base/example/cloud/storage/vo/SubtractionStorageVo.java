package com.cloud.base.example.cloud.storage.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubtractionStorageVo {

    @ApiModelProperty(value = "产品code")
    private Long productId;

    @ApiModelProperty(value = "库存数量")
    private Integer count;

    @ApiModelProperty(value = "总价")
    private BigDecimal totalPrice;
}
