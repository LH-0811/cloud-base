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





