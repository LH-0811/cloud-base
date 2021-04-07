# 基于ZooKeeper的分布式环境下数据一致性工具包

> 刚刚开始构建这个模块，想到哪里就写到哪里


## 配置项
详见ZkDistributedProperties
```

/**
 * @author lh0811
 * @date 2021/3/31
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "zk.distributed")
public class ZkDistributedProperties {
    // 开启标志
    private Boolean enabled = true;

    // 服务器地址（集群地址使用,分隔）
    private String server = "127.0.0.1:2181";

    // 命名空间，被称为ZNode
    private String namespace;

    // 权限控制，加密
    private String digest;

    // 会话超时时间
    private Integer sessionTimeoutMs = 60000;

    // 连接超时时间
    private Integer connectionTimeoutMs = 60000;

    // 最大重试次数
    private Integer maxRetries = 2;

    // 初始休眠时间
    private Integer baseSleepTimeMs = 1000;
}
```



## 场景一: 分布式锁实现

使用自定义注解的方式实现动态的加锁和释放锁(可重入互斥锁)。
具体使用方式如下
在需要被分布式锁控制的方法上增加@ZkDistributedLock注解，该注解默认以 "/cloud_base/zk_distributed_lock_default" 为锁资源路径
```
@Slf4j
@Service("zkLockTestService")
public class ZkLockTestServiceImpl implements ZkLockTestService {
    @Override
    @ZkDistributedLock
    public String testZkClient(String name) throws Exception {
        log.info("开始执行zk锁测试方法:{}",name);
        log.info("执行中。。。。:{}",name);
        // 线程休眠 模拟耗时操作
        Thread.sleep(10000);
        log.info("执行完成:{}",name);
        return "success";
    }
}
```

## 场景二: 订阅者模式实现
构建一个故事板，订阅该故事板的订阅者可以获取到故事板发布的信息。

#### 构建故事板
实现ZkStoryboard接口，并通过@Component托管到spring容器中

getSubjectName()方法返回则为故事板的主题。建议以：/项目名/业务名/主题名 为zkNode的路径
```
/**
 * @author lh0811
 * @date 2021/4/6
 */
@Component
public class ZkStoryboardForDefault  implements ZkStoryboard{
    @Override
    public String getSubjectName() {
        return "/cloud_base/zk_storyboard_default";
    }
}
```

#### 构建订阅者
实现ZkSubscriber接口，并通过@Component托管到spring容器中

getSubjectName()方法返回的为订阅主题名。建议以：/项目名/业务名/主题名 为zkNode的路径
```
/**
 * @author lh0811
 * @date 2021/4/6
 */
@Slf4j
@Component
public class ZkSubscribeForDefault implements ZkSubscriber {

    @Override
    public String getSubjectName() {
        return "/cloud_base/zk_storyboard_default";
    }

    @Override
    public void getInfo(String info) {
        log.info("ZkSubscribeForDefault获取到订阅信息:{}", info);
    }
}
```

#### 发布信息
使用ZkStoryboardEngine类，向故事板发送信息
```
@GetMapping("/storyboard/{msg}")
@ApiOperation("测试订阅者模式")
@ApiImplicitParams({
        @ApiImplicitParam(paramType = "path", dataType = "String", dataTypeClass = String.class, name = "msg", value = "信息"),
})
public ServerResponse storyboard(@PathVariable("msg") String msg) throws Exception {
    zkStoryboardEngine.sendMsy("/cloud_base/zk_storyboard_default",msg);
    return ServerResponse.createBySuccess("测试完成");
}
```
#### 使用建议
StoryBoard发布的消息传递到订阅者后。如果该消息比较重要，建议订阅者在本地创建一个内存队列存储消息。当消息处理成功后移除队列，若失败则放到队列尾部待重试。

## 场景三: 分布式计数器实现
使用 ZkDistributedCounter.getAtomicInteger(String path) 方法来获取一个分布式计数器

