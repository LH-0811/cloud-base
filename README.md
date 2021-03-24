# springcloud 脚手架项目

## 模块划分
```
dependency-* : 
*描述:引用某一类框架的依赖的集合 
*例如:mybatis + PageHelper + tk.mybasts 整合为 dependency-mybatis-tk,
如果有其他的orm集合则新增一个dependency
*注意:该模块只是集合某一类框架maven引用而存在,并不做具体的实现。

core-*: 
*描述：核心代码 
*例如：基础异常处理，基础系统响应包装

modules-*：
*描述：自封装的一些功能模块
*例如：安全组件功能、日志功能、多数据功能
*注意：这一类属于针对某种通用功能的实现，理论上业务服务只需要引入这些功能组件就可以针对业务使用现有功能。
但是由于是一种功能的实现，所以每个模块都应该有个readme来描述如何使用该功能模块。

boot-example: springboot单体应用示例

cloud-example-*: springcloud 应用示例

```
