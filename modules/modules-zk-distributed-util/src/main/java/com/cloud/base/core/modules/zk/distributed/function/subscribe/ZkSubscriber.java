package com.cloud.base.core.modules.zk.distributed.function.subscribe;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 信息板订阅者
 *
 * @author lh0811
 * @date 2021/4/6
 */
public interface ZkSubscriber {
    // 获取到故事板主题名
    String getSubjectName();

    // 后去到故事板信息
    void getInfo(String info);
}
