package com.cloud.base.user.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author lh0811
 * @date 2021/3/25
 */
@Configuration
@MapperScan("com.cloud.base.user.repository_plus.dao.mapper")
public class MybatisConfig {
}
