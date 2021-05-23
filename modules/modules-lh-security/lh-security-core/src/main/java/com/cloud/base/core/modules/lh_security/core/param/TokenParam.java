package com.cloud.base.core.modules.lh_security.core.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author lh0811
 * @date 2021/5/23
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenParam {

    @ApiModelProperty(value = "用户token")
    private String token;
}
