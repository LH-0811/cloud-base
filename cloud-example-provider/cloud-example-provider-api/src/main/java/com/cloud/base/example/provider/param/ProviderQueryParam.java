package com.cloud.base.example.provider.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 查询参数
 *
 * @author lh0811
 * @date 2021/3/23
 */
@Getter
@Setter
public class ProviderQueryParam {

    @ApiModelProperty(value = "参数1")
    private String param1;

    @ApiModelProperty(value = "参数2")
    private String param2;

}
