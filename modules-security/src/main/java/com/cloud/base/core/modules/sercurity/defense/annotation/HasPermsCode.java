package com.cloud.base.core.modules.sercurity.defense.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 判断方法级的权限
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface HasPermsCode {

    // 参数输入要拥有的权限码
    String[] value() default {};

}
