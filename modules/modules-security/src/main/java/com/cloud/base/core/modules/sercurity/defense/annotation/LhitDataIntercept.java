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
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface LhitDataIntercept {
}
