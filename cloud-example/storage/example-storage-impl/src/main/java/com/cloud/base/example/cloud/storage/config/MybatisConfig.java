package com.cloud.base.example.cloud.storage.config;

import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author lh0811
 * @date 2021/3/25
 */
@MapperScan("com.cloud.base.example.cloud.storage.repository.dao")
@Configuration
public class MybatisConfig {
}
