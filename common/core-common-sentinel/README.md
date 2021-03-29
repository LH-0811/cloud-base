# common-sentinel 模块

与core-common 一致，只是在其基础上添加统一异常处理时对sentinel的支持。


# common提供的封装
```
entity: 
1. 通用实体类（默认分页参数值） 通用方法（堆栈打印到log，listToTree）
2. 统一服务端返回
exception:
1. 统一异常类
2. 控制器加强 处理全局异常 和 controller层入参校验异常
util：工具类

```
