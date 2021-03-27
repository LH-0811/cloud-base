package com.cloud.base.datasource;

import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 测试多数据源
 *
 * @author lh0811
 * @date 2021/3/22
 */
@Configuration
@MapperScan("com.cloud.base.datasource.dao")
public class MyBatisConfig {


}
