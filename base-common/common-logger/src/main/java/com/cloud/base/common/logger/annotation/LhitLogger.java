package com.cloud.base.common.logger.annotation;

import com.cloud.base.common.logger.entity.LoggerBusinessType;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LhitLogger {
    /**
     * 模块
     */
    String title() default "";

    /**
     * 功能
     */
    LoggerBusinessType businessType() default LoggerBusinessType.NONE;

    /**
     * 是否保存请求的参数
     */
    boolean isSaveRequestData() default true;

    /**
     * 是否保存返回参数
     */
    boolean isSaveReturnData() default true;


    /**
     * 是否保存操作用户信息
     */
    boolean isSaveOperUserInfo() default true;

    /**
     * 是否保存操作角色信息(*必须打开保存用户信息)
     */
    boolean isSaveOperRoleInfo() default true;

    /**
     * 是否保存操作客户端的网络地址信息
     */
    boolean isSaveOperNetAddr() default true;

}
