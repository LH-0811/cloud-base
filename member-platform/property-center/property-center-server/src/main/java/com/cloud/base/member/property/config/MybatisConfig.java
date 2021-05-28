package com.cloud.base.member.property.config;

import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author lh0811
 * @date 2021/3/25
 */
@MapperScan("com.cloud.base.member.property.repository.dao")
@Configuration
public class MybatisConfig {
}
