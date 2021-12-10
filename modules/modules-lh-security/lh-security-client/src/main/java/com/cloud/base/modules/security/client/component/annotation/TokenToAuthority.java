package com.cloud.base.modules.security.client.component.annotation;

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
}
