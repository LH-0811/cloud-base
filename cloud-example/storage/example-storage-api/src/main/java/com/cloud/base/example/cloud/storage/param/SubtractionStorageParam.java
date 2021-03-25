package com.cloud.base.example.cloud.storage.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SubtractionStorageParam {
    @NotNull(message = "产品id")
    @ApiModelProperty(value = "产品id",required = true)
    private Long productId;

    @Min(value = 0,message = "库存数量必须大于0")
    @NotNull(message = "库存数量 不能为空")
    @ApiModelProperty(value = "库存数量",required = true)
    private Integer count;
}
