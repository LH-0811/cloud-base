# sentinel-1.8.1 按照部署步骤

## 1. 下载sentinel包

sentinel服务代码托管地址：https://github.com/alibaba/Sentinel/tags
在页面选择最新的版本下载
```
https://github.com/alibaba/Sentinel/releases/tag/1.8.1
```
## 2. 启动sentinel服务
```
java -Dserver.port=9000 -jar sentinel-dashboard-1.8.1.jar
```

## 3. 访问验证

```
http://localhost:9000
``` 
## 4. 本地应用接入sentinel

在本地应用中增加配置文件
```
spring:
  cloud:
    sentinel: # 配置sentinel
      transport:
        dashboard: localhost:9000 # 配置sentinel dashboard 启动地址
        port: 8719 # 直接写8719 端口号默认是8719 如果被占用就是+1 直到找到未被占用端口
        # clientIp: localhost # 这个配置是sentinel访问服务的ip，所以测试时sentinel要放到能访问到本地服务的网络环境中一般就localhost
      filter:
        url-patterns: /** # 默认是/* 这里改成/** 则会监控所有的请求
```

## 5. 在本地服务中为资源做流控处理
首先 如果服务中使用了sentinel做流控，那core-common包引用就改为core-common-sentinel
使用SentinelResource 注解自定义资源 并可以指定降级后的异常处理方法
```
// value 资源名称 ， 降级后的处理
@SentinelResource(value = "res_hello", blockHandler = "blockHandler")
@GetMapping("/hello")
@ApiOperation("hello测试流控接口")
public ServerResponse<OrderVo> hello() throws Exception {
    return ServerResponse.createBySuccess("hello");
}

// 降级后的自定义返回
public ServerResponse blockHandler(BlockException e) {
    return ServerResponse.createByError("创建订单：系统繁忙，请稍后再试");
}
```
如果不指定 blockHandler 例如：

```
 // value 资源名称 ， 降级后的处理
 @SentinelResource(value = "res_hello")
 @GetMapping("/hello")
 @ApiOperation("hello测试流控接口")
 public ServerResponse<OrderVo> hello() throws Exception {
     return ServerResponse.createBySuccess("hello");
 }
 ```
在该方法会跑出FlowException异常,在同一异常处理里面已经加了相关的处理
```
@ResponseBody
@ExceptionHandler(value = {FlowException.class})
public ServerResponse flowException(HttpServletRequest request, FlowException e) {
    if (userSentinel) {
        Tracer.trace(e);
    }
    log.error(CommonMethod.getTrace(e));
    return ServerResponse.createByError(500, "系统繁忙请稍后");
}
```
所以默认有这个同一异常处理来兜底
