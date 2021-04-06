# 基于ZooKeeper的分布式环境下数据一致性工具包

> 刚刚开始构建这个模块，想到哪里就写到哪里

## 场景一: 分布式锁实现
使用自定义注解的方式实现动态的加锁和释放锁。
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


## 场景一: 假设有工单池。产生工单后将工单丢到工单池中，此时分布式系统中有工单执行服务10个实例。保证每个工单只能被执行一次，不会被重复消费

## 场景二: 假设有业务A，开始执行A业务时需通知B，C业务完成数据准备，在B，C业务执行完成后，A业务才能继续执行。



## 场景四: 分布式计数器实现

## 场景五: 订阅者模式实现

## 场景六: 基本节点数据监听

