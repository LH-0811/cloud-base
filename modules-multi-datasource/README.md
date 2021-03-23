# 多数据源模块

## 示例配置
```
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    # 使用多数据源时 这个配置便是 默认数据源
    druid:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3306/test1?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
      username: root
      password: 123456
      initial-size: 10
      max-active: 100
      min-idle: 10
      max-wait: 60000
      pool-prepared-statements: true
      max-pool-prepared-statement-per-connection-size: 20
      time-between-eviction-runs-millis: 60000
      min-evictable-idle-time-millis: 300000
      #Oracle需要打开注释
      #validation-query: SELECT 1 FROM DUAL
      test-while-idle: true
      test-on-borrow: false
      test-on-return: false
      stat-view-servlet:
        enabled: true
        url-pattern: /druid/*
        #login-username: admin
        #login-password: admin
      filter:
        stat:
          log-slow-sql: true
          slow-sql-millis: 1000
          merge-sql: false
        wall:
          config:
            multi-statement-allow: true
dynamic:
  datasource:
    slave1:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3306/test1?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
      username: root
      password: 123456
    slave2:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3306/test2?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
      username: root
      password: 123456
```
## 使用说明
根据上面的配置信息。

配置druid数据源时要指定默认的数据库连接。

动态数据源在druid中配置。

使用示例：

```
    /**
     * 查询人员信息
     *
     * @param param
     * @return
     * @throws Exception
     */
    @DataSource("slave2")
    @Override
    public PageInfo<People> peopleQuery(PeopleQueryParam param) throws Exception {
        log.info(">>>开始 查询人员信息");
        Dao.exec();
    }


    // 这里不知道数据源 就是连接池配置的，默认的数据源
    /**
     * 创建人员信息
     *
     * @param param
     * @throws Exception
     */
    @DataSource
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void peopleCreate(PeopleCreateParam param) throws Exception {
        log.info(">>>开始 创建人员信息");
        Dao.exec();
    }
```

