package com.cloud.base.example.cloud.account.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class AccountSubtractionParam {

    @NotNull(message = "用户id不能为空")
    @ApiModelProperty(value = "用户id",required = true)
    private Long userId;

    @Min(value = 0,message = "本次消费不能小于0")
    @NotNull(message = "本次消费 不能为空")
    @ApiModelProperty(value = "本次消费",required = true)
    private BigDecimal amt;
}
