package com.cloud.base.modules.security.core.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author lh0811
 * @date 2021/5/10
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckResParam {

    @ApiModelProperty(value = "用户token")
    private String token;

    @ApiModelProperty(value = "资源：URL、permsCode、staticResPath")
    private String resource;
}
