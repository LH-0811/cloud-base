package com.cloud.base.modules.xugou.client.component.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记访问该方法必须有指定的resPath权限
 *
 * @author lh0811
 * @date 2021/5/10
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface HasStaticResPath {
    String resPath();
}
