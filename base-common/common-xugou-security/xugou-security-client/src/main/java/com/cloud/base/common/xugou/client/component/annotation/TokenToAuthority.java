package com.cloud.base.common.xugou.client.component.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记必须需要有当前被标记的Controller层的method的url权限
 *
 * @author lh0811
 * @date 2021/5/10
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TokenToAuthority {
    /**
     * 是否需求强制验证权限信息,false 时允许Controller层的SecurityAuthority参数为空
     *
     * contribute by hzy_boy
     *
     * @return
     */
    boolean require() default true;
}
