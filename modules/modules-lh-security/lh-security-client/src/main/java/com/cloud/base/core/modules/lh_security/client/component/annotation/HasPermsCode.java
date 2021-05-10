package com.cloud.base.core.modules.lh_security.client.component.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记访问该方法必须有指定的permsCode权限
 *
 * @author lh0811
 * @date 2021/5/10
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface HasPermsCode {
    String permsCode();
}
