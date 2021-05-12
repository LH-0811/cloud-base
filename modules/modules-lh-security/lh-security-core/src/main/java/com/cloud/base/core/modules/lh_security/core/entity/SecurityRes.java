package com.cloud.base.core.modules.lh_security.core.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author lh0811
 * @date 2021/5/7
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "安全框架资源")
public class SecurityRes {

    // 全部code资源权限
    public static String ALL = "ALL";

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
        MENU(1, "菜单"),
        INTERFACE(2, "接口"),
        PERMS_CODE(3, "权限码"),
        STATIC_RES(4, "静态资源");

        private Integer code;
        private String msg;
    }


    /**
     * 全部url权限
     *
     * @return
     */
    public static SecurityRes allUrlRes() {
        SecurityRes allUrl = new SecurityRes();
        allUrl.setResType(ResType.INTERFACE.getCode());
        allUrl.setName("全url资源");
        allUrl.setCode("");
        allUrl.setUrl("/**");
        allUrl.setPath("");
        return allUrl;
    }

    /**
     * 全部code权限
     *
     * @return
     */
    public static SecurityRes allCodeRes() {
        SecurityRes allCode = new SecurityRes();
        allCode.setResType(ResType.PERMS_CODE.getCode());
        allCode.setName("全Code权限");
        allCode.setCode(ALL);
        allCode.setUrl("");
        allCode.setPath("");
        return allCode;
    }


    /**
     * 全部staticResPath权限
     *
     * @return
     */
    public static SecurityRes allStaticResPath() {
        SecurityRes allCode = new SecurityRes();
        allCode.setResType(ResType.STATIC_RES.getCode());
        allCode.setName("全StaticResPath权限");
        allCode.setCode("");
        allCode.setUrl("");
        allCode.setPath(ALL);
        return allCode;
    }




}
