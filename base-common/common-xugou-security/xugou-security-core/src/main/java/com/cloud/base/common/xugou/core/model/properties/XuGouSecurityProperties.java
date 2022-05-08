package com.cloud.base.common.xugou.core.model.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "xugou.security")
public class XuGouSecurityProperties {

    @Getter
    @Setter
    // 请求Header中token的key
    private String tokenKey = "LHTOKEN";

    @Getter
    @Setter
    // token 过期时间 单位是分钟
    private String expire = "9999999";

    @Getter
    @Setter
    // 当前token失效或未上传token返回码
    private String noAuthorizedCode = "401";

    @Getter
    @Setter
    // 无权限访问返回码
    private String unAuthorizedCode = "403";

    /**
     * 标注当前应用是否使用springcloud框架。
     * 如果为true。
     * 当客户端需要鉴权时，会通过配置文件serverName服务名，来随机获取一个鉴权中心示例的ip+port。并发起http请求，去获取用户权限信息。
     * 并判断用户是否有权限访问资源或方法
     * 如果为false
     * 则会在当前spring容器上下文获取到SecurityCheckAuthority示例来判断用户是否有权限。
     */
    @Getter
    @Setter
    private Boolean useCloud = true;


    /**
     * 是否在每次验证token后,都刷新token信息
     */
    @Getter
    @Setter
    private Boolean tokenRefresh = true;

    /**
     * 如果配置了useCloud = true;
     * 则需要配置注册中心中授权中心的服务名
     */
    @Getter
    @Setter
    private String serverName = "authorize-center-server";

    /**
     * 如果配置了useCloud = true;
     * 则需要配置授权中心提供的token换授权信息的接口地址。
     * 一般默认不配置就好。除非与项目中其他请求冲突
     */
    @Getter
    @Setter
    private String serverUrlOfTokenToAuthority = "/xugou/security/token_to_authority";

}
