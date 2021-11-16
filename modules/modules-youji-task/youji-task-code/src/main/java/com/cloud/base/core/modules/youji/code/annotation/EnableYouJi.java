package com.cloud.base.core.modules.youji.code.annotation;

import io.swagger.annotations.ApiModelProperty;

import java.lang.annotation.*;

/**
 * 任务注解
 *
 * @author lh0811
 * @date 2021/11/13
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableYouJi {

}
