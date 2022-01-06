# springcloud 脚手架项目

> 作者： LH0811 QQ邮箱:2329385085@qq.com (加qq好友请标注来意)

## 系统架构图
![avatar](./1_assets/image/服务架构图.jpg)
## 快速启动
需要的数据库表在：https://gitee.com/lh_0811/cloud-base/tree/master/base-application/user-center/db 目录下

### 本地app启动

1.本地启动nacos: http://localhost:8848

```shell
sh startup.sh -m standalone
```
2.本地启动sentinel: http://localhost:9000

```shell
nohup java -Dauth.enabled=false -Dserver.port=9000 -jar sentinel-dashboard-1.8.1.jar &
```
3.本地启动zipkin: http://localhost:9411/

```shell
nohup java -jar zipkin-server-2.23.2-exec.jar &
```

### 利用docker快速部署本地环境

1. 启动nacos http://localhost:8848

   ```shell
   docker run -d \
   --name nacos \
   -e MODE=standalone \
   -p 8848:8848 \
   -v /Users/lh0811/Work/Data/Docker/nacos/config:/home/nacos/config \
   -v /Users/lh0811/Work/Data/Docker/nacos/data:/home/nacos/data \
   nacos/nacos-server:2.0.2
   ```

2. 启动sentinel: http://localhost:9000

   ```shell
   docker run -d --name sentinel -p 9000:9000 lh0811/localhst-sentinel:1.8.1
   ```

3. 启动zipkin: http://localhost:9411/

   ```shell
   docker run -d --name zipkin -p 9411:9411 lh0811/localhst-zipkin:2.23.4
   ```



## 项目概述

springboot+springcloud

注册中心：nacos

网关:gateway

RPC:feign

以下是可插拔功能组件

流控熔断降级:sentinel

全链路跟踪:sleth+zipkin

分布式事务:seata

封装功能模块：全局异常处理、日志输出打印持久化、多数据源、鉴权授权模块、zk（分布式锁和订阅者模式）

maven：实现多环境打包、直推镜像到docker私服。

<hr/>

这个项目整合了springcloud体系中的各种组件。以及集成配置说明。

同时将自己平时使用的功能性的封装以及工具包都最为模块整合进来。

可以避免某些技术点长时间不使用后的遗忘。

另一方面现在springboot springcloud 已经springcloud-alibaba的版本迭代速度越来越快。

为了保证我们的封装和集成方式在新版本中依然正常运行，需要用该项目进行最新版本的适配实验。这样可以更快的在项目中集合工程中的功能模块。

## 项目预览
前端项目地址：https://gitee.com/lh_0811/cloud-base-angular-admin
![avatar](./1_assets/image/用户管理.jpg)
![avatar](./1_assets/image/资源管理.png)
![avatar](./1_assets/image/流控.jpg)
![avatar](./1_assets/image/代码生成.jpg)
![avatar](./1_assets/image/定时任务管理.jpg)

## 新建业务工程模块说明。

由于springboot遵循 约定大于配置的原则。所以本工程中所有的额类都在的包路径都在com.cloud.base下。

如果新建的业务项目有规定使用指定的基础包路径则需要在启动类增加包扫描注解将com.cloud.base下的所有类加入到扫描范围下。

```
@ComponentScan(basePackages = "com.cloud.base")
```

如果可以继续使用com.cloud.base 则约定将启动类放在该路径下即可。

## 模块划分

```
父工程:

cloud-base - 版本依赖管理  <groupId>com.cloud</groupId>
|
|--base-common - 通用工具类和包  <groupId>com.cloud.common</groupId>
|   |
|   |--common-core  通用包 该包包含了SpringMVC的依赖，会与WebFlux的服务有冲突
|   |
|   |--common-logger 日志功能封装
|   |
|   |--common-multisource 多数据功能封装
|   |
|   |--common-xugou-security 戌狗-分布式安全授权鉴权框架封装
|   |
|   |--common-youji-task 酉鸡-分布式定时任务管理模块
|
|--base-dependency - 三方功能依赖集合 无任何实现 <groupId>com.cloud.dependency</groupId>
|   |
|   |--dependency-alibaba-cloud 关于alibaba-cloud的依赖集合
|   |
|   |--dependency-mybatis-tk 关于ORM mybatis+tk.mybatis+pagehelper的依赖集合
|   |
|   |--dependency-mybatis-plus 关于ORM mybatis+mybatis—plus+pagehelper的依赖集合
|   |
|   |--dependency-seata 关于分布式事务seata的依赖集合
|   |
|   |--dependency-sentinel 关于流控组件sentinel的依赖集合
|   |
|   |--dependency-sentinel-gateway 关于网关集成流控组件sentinel的依赖集合（仅仅gateway网关使用该依赖）
|   |
|   |--dependency-sleuth-zipkin 关于链路跟踪sleuth-zipkin的依赖集合
|
|--base-code-gen  代码生成工具
|
|--base-application 以下是独立部署的应用 
|		|
|		|--base-authorize 基础授权中心应用
|		|
|		|--base-gateway 网关应用应用
|		|
|		|--user-center 用户中心服务应用
|		|
|		|--youji-manage-server 定时任务管理应用
```

## 版本使用说明

```html
<springboot.version>2.4.2</springboot.version>

<springcloud.version>2020.0.1</springcloud.version>

<springcloud-alibaba.version>2021.1</springcloud-alibaba.version>
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

``` xml
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

## 整合dockerfile插件，可直接将jar包构建为docker image 并推送到远程仓库

增加插件依赖

``` xml
<!-- docker image build -->
<plugin>
    <groupId>com.spotify</groupId>
    <artifactId>dockerfile-maven-plugin</artifactId>
    <version>1.4.10</version>
    <executions>
        <execution>
            <id>default</id>
            <goals>
                <!--如果package时不想用docker打包,就注释掉这个goal-->
                <!--                        <goal>build</goal>-->
                <goal>push</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <repository>localhost:8099/example/${project.artifactId}</repository>
        <tag>${profileActive}-${project.version}</tag>
        <username>admin</username>
        <password>Harbor12345</password>
        <buildArgs>
            <JAR_FILE>target/${project.build.finalName}.jar</JAR_FILE>
        </buildArgs>
    </configuration>
</plugin>
```

在pom.xml同级目录下增加Dockerfile

```dockerfile
FROM openjdk:8u171
MAINTAINER lh0811
ADD ./target/${JAR_FILE} /opt/app.jar
RUN chmod +x /opt/app.jar
CMD java -jar /opt/app.jar
```



