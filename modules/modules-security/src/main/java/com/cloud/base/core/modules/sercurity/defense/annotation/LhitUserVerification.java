package com.cloud.base.core.modules.sercurity.defense.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注 用户验证适配器 在springboot项目启动时会加载到 用户验证适配器集合中
 *
 * 通过ApplicationListener
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface LhitUserVerification {

    // 如果是true 则该验证必须通过
    // 默认false 默认是 所有的Verification 必须有一个为true
    boolean must() default false ;

}
