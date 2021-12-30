package com.cloud.base.common.multisource.annotation;

import java.lang.annotation.*;

/**
 * 多数据源注解
 *
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataSource {
    String value() default "";
}
