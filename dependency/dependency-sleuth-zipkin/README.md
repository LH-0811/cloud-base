# sleuth + zipkin 实现链路追踪

## 1. 引入依赖

在需要引入链路跟踪的应用系统中添加依赖
```
<dependency>
    <groupId>com.cloud.dependency</groupId>
    <artifactId>dependency-sleuth-zipkin</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

## 2. 部署zipkin 服务端

获取最新包下载地址
```
https://search.maven.org/remote_content?g=io.zipkin&a=zipkin-server&v=LATEST&c=exec
```
本次下载到的最新包为:zipkin-server-2.23.2-exec.jar

部署获得的jar包：默认端口为9411 可以通过-Dserver.port=9411 来替换端口。
```
nohub java -jar zipkin-server-2.23.2-exec.jar &
```

## 3. 在应用服务中增加配置
```
spring:
  zipkin:
    base-url: http://49.232.166.94:9411/ # 服务端地址
    sender:
      type: web # 数据传输方式，web 表示以 HTTP 报文的形式向服务端发送数据
  sleuth:
    sampler:
      probability: 1.0 # 收集数据百分比，默认 0.1（10%） 生成中最好不要配置1 影响性能。
```

##### 一般在网关和应用服务中都要引入依赖和配置
