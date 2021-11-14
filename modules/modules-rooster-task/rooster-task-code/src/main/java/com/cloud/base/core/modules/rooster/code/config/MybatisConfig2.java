package com.cloud.base.core.modules.rooster.code.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author lh0811
 * @date 2021/3/25
 */
@Configuration
@MapperScan("com.cloud.base.core.modules.rooster.code.repository.dao.mapper")
public class MybatisConfig2 {
}
