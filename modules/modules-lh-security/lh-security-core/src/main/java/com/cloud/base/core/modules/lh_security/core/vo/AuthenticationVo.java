package com.cloud.base.core.modules.lh_security.core.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationVo {

    @ApiModelProperty(value = "用户token")
    private String token;

}
