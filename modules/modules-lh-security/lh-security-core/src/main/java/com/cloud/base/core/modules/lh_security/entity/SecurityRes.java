package com.cloud.base.core.modules.lh_security.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lh0811
 * @date 2021/5/7
 */
@Getter
@Setter
@ApiModel(value = "安全框架资源")
public class SecurityRes {

    @ApiModelProperty(value = "资源类型")
    private Integer resType;

    @ApiModelProperty(value = "资源名称")
    private String name;

    @ApiModelProperty(value = "资源code")
    private String code;

    @ApiModelProperty(value = "资源URL")
    private String url;
    @ApiModelProperty(value = "静态资源路径")
    private String path;

    @Getter
    @ApiModel(value = "安全框架资源类型")
    @AllArgsConstructor
    public enum ResType {
        MENU(1,"菜单"),
        INTERFACE(2,"接口"),
        PERMS_CODE(3,"权限码"),
        STATIC_RES(4,"静态资源");

        private Integer code;
        private String msg;
    }
}
