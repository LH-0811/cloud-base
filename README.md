# springcloud 脚手架项目

## 项目概述
这个项目整合了springcloud体系中的各种组件。以及集成配置说明。

同时将自己平时使用的功能性的封装以及工具包都最为模块整合进来。

可以避免某些技术点长时间不使用后的遗忘。

另一方面现在springboot springcloud 已经springcloud-alibaba的版本迭代速度越来越快。

为了保证我们的封装和集成方式在新版本中依然正常运行，需要用该项目进行最新版本的适配实验。这样可以更快的在项目中集合工程中的功能模块。


## 模块划分
```
父工程:

cloud-base - 版本依赖管理  <groupId>com.cloud</groupId>
|
|-common - 通用工具类和包  <groupId>com.cloud.common</groupId>
|
|-dependency - 三方功能依赖管理包 <groupId>com.cloud.dependency</groupId>
|
|-modules - 自定义自实现的功能组件模块 <groupId>com.cloud.modules</groupId>
|
|-cloud-gateway - spring cloud gateway 应用 <groupId>com.cloud.base</groupId>
|
|-example - 示例项目 <groupId>com.cloud.base</groupId>
```
## 版本使用说明
```
<springboot.version>2.3.9.RELEASE</springboot.version>
<springcloud.version>Hoxton.SR8</springcloud.version>
<springcloud-alibaba.version>2.2.1.RELEASE</springcloud-alibaba.version>
```


## 多环境打包说明
在需要独立打包的模块resources资源目录下增加不同环境的配置文件
```
application-dev.yml
application-test.yml
application-prod.yml
```
修改application.yml
```
spring:
  profiles:
    active: @profileActive@
```
在需要独立打包的模块下的pom文件中添加一下打包配置。
```
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <version>${springboot.version}</version>
            <configuration>
                <fork>true</fork>
                <addResources>true</addResources>
            </configuration>
            <executions>
                <execution>
                    <goals>
                        <goal>repackage</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-resources-plugin</artifactId>
            <configuration>
                <delimiters>
                    <delimiter>@</delimiter>
                </delimiters>
                <useDefaultDelimiters>false</useDefaultDelimiters>
            </configuration>
        </plugin>
    </plugins>
    <resources>
        <resource>
            <directory>src/main/resources</directory>
            <filtering>true</filtering>
        </resource>
    </resources>
</build>

<profiles>
    <profile>
        <id>dev</id>
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
        <properties>
            <profileActive>dev</profileActive>
        </properties>
    </profile>
    <profile>
        <id>test</id>
        <properties>
            <profileActive>test</profileActive>
        </properties>
    </profile>
    <profile>
        <id>prod</id>
        <properties>
            <profileActive>prod</profileActive>
        </properties>
    </profile>
</profiles>
```

mvn打包命令
```
# 打开发环境
mvn clean package -P dev -Dmaven.test.skip=ture
# 打测试环境
mvn clean package -P test -Dmaven.test.skip=ture
# 打生产环境
mvn clean package -P prod -Dmaven.test.skip=ture
```





