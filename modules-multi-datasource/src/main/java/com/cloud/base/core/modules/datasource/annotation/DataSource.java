package com.cloud.base.core.modules.datasource.annotation;

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
