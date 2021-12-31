# 酉鸡-定时任务管理

> 作者： LH0811 QQ邮箱:2329385085@qq.com (加qq好友请标注来意)

> 酉鸡（定时任务管理模块） -- 一唱雄鸡天下白。
> 该模块是第一个以十二生肖动物名命名的模块，雄鸡表现出的时间观念与定时任务十分切合。
> 增加些传统和生命感，会激励自己不断迭代，集成12生肖。


## 1. 应用系统集成

酉鸡框架中的应用分为两个角色。一个是作为定时任务统一管理和调度的 Manage 和 具体执行定时任务的工作节点Worker

### 1.1 Worker 工作节点（客户端应用）
1. 引入POM依赖
```xml
<!-- 引入定时任务 酉鸡 -->
<dependency>
    <groupId>com.cloud.modules</groupId>
    <artifactId>youji-task-worker</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```
2. 修改配置文件
```yaml
youji:
  worker:
    manage-host: localhost # 管理端服务ip
    manage-port: 8889 # 管理端服务端口
    current-worker-ip: localhost # 当前工作节点ip
    current-worker-port: 9301 # 当前工作节点端口
```
3. 使用注解注册定时任务到管理端
```java

@Slf4j
@Api(tags = "用户中心-测试")
@RestController
@RequestMapping("/test")
@EnableYouJi  // 标注 该类使用了酉鸡定时任务框架
public class TestController {

    // 使用注解注册定时任务参数到服务端
    @YouJiTask(taskName = "测试任务1",
            taskNo = "Task0001",
            corn = "0/5 * * * * ?",
            param = "name=123",
            enable = true)
    public void testYouJiTask1(String param) throws Exception {
        log.info("[酉鸡 Worker Task1]  !!!!!! 参数:{}", param);
    }

    @YouJiTask(taskName = "测试任务2",
            taskNo = "Task0002",
            corn = "0/2 * * * * ?",
            param = "test-param",
            enable = true)
    public void testYouJiTask2(String param) throws Exception {
        log.info("[酉鸡 Worker Task2]  !!!!!! 参数:{}", param);
    }

}
```
这里需要注意，注解里面的参数，会在第一次注册到管理端时初始化创建到数据库中，

一旦数据库中存在TaskNo对应的记录，则以数据库中记录的为准。

如果需要修改任务名或参数等信息，则可以在管理端前台页面上进行修改。
### 1.2 Manage 管理节点 (管理端应用)

管理端应用当前是一个单节点的应用，如果进行多节点保活是下一步要实现的功能。

新建一个基于springboot的web应用。

1. 引入POM依赖
```xml
<!-- 定时任务模块 -->
<dependency>
    <groupId>com.cloud.modules</groupId>
    <artifactId>youji-task-manage</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```
2. 修改配置文件
```yaml
youji: 
  server:
    heart-beat-corn: 0/5 * * * * ? # 工作节点心跳检查频率
    die-no-heart-num: 10 # 心跳未响应10次后，则移除该工作节点
```
启动项目即可。

# 2. 工作原理

该框架依赖于http接口进行通讯。

客户端启动时，扫描注解类，将任务通过http接口对接的形式，发送给服务端开发的注册任务接口。

服务端启动接收到后，记录到数据库，并立即启动定时任务。

当服务端重新启动时，获取到数据库中保存的定时任务，并启动他们。

当定时任务发起时，则根据数据库记录表中的Bean类和对应的方法，向指定的客户端发起执行请求，客户端则执行定时任务。并记录日志。

# 3. 客户端心跳机制。
服务端会主动向客户端发起心跳，检测工作节点是否可用，超过指定次数后，认为工作节点不可用。

# 4. 工作节点 与 服务节点 开放的Api汇总

Worker：
```text
心跳应答：GET /youji/task/worker/heart_beat
接收定时任务执行: POST /youji/task/worker/receive
```

Manage：
```text
创建即注册定时任务:POST /youji/task/manage/create
查询定时任务列表：POST /youji/task/manage/query
更新定时任务基本信息：POST /youji/task/manage/update
更新定时任务执行计划：POST /youji/task/manage/update/cron
启用停用定时任务：POST /youji/task/manage/update/enable
立即执行定时任务：GET /youji/task/manage/exec/{taskNo}
查询定时任务执行日志: POST /youji/task/manage/log/query
获取定时任务工作节点：GET /youji/task/manage/work/list/{taskNo}
修改工作节点是否可用：POST /youji/task/manage/worker/update/enable
```

