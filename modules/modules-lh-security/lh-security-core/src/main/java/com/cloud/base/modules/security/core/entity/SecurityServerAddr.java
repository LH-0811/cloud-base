package com.cloud.base.modules.security.core.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 安全服务框架服务端地址
 *
 * @author lh0811
 * @date 2021/5/11
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SecurityServerAddr {

    @ApiModelProperty(value = "是否是cloud项目")
    private Boolean isCloud;

    @ApiModelProperty(value = "安全框架服务端地址")
    private String address;

    @ApiModelProperty(value = "安全框架服务端端口")
    private Integer port;

    public String toHttpAddrAndPort(){
        return "http://"+address+":"+port;
    }
}
