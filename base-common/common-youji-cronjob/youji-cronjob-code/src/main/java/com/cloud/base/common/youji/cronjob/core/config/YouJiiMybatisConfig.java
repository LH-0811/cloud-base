package com.cloud.base.common.youji.cronjob.core.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author lh0811
 * @date 2021/3/25
 */
@Configuration
@MapperScan("com.cloud.base.common.youji.cronjob.core.repository.dao.mapper")
public class YouJiiMybatisConfig {
}
