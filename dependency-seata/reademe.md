# seata1.4安装部署流程

## 1. 部署nacos
下载编译后压缩包方式
可以从 
[最新稳定版本](https://github.com/alibaba/nacos/releases) 
下载nacos-server-$version.zip 包。
```
unzip nacos-server-$version.zip 或者 tar -xvf nacos-server-$version.tar.gz
cd nacos/bin
sh startup.sh -m standalone
```
## 2. 为seata新建命名空间
增加seata的命名空间并记录其id
```
namespace_id = 1c04b365-d834-442a-8b8d-a28861d1fc00
```
## 3. 下载seata1.4版本

下载地址：http://seata.io/zh-cn/blog/download.html

现在二进制包 和 源码都下载下来，需要使用到源码中的脚本

## 4. 修改二进制包中的配置文件

### 4.1 修改file.conf

```

## transaction log store, only used in seata-server
store {
  ## store mode: file、db、redis
  ## 这里的模式改成db
  mode = "db"

  ## file store property
  file {
    ## store location dir
    dir = "sessionStore"
    # branch session size , if exceeded first try compress lockkey, still exceeded throws exceptions
    maxBranchSessionSize = 16384
    # globe session size , if exceeded throws exceptions
    maxGlobalSessionSize = 512
    # file buffer size , if exceeded allocate new buffer
    fileWriteBufferCacheSize = 16384
    # when recover batch read size
    sessionReloadReadSize = 100
    # async, sync
    flushDiskMode = async
  }

  ## database store property
  db {
    ## the implement of javax.sql.DataSource, such as DruidDataSource(druid)/BasicDataSource(dbcp)/HikariDataSource(hikari) etc.
    datasource = "druid"
    ## mysql/oracle/postgresql/h2/oceanbase etc.
    dbType = "mysql"
    # 这里如果数据库是5.7以上的版本 包路径加上cj
    driverClassName = "com.mysql.cj.jdbc.Driver"
    url = "jdbc:mysql://49.232.166.94:3306/example-seata"
    user = "root"
    password = "QQliuhe951@@"
    minConn = 5
    maxConn = 100
    globalTable = "global_table"
    branchTable = "branch_table"
    lockTable = "lock_table"
    queryLimit = 100
    maxWait = 5000
  }

  ## redis store property
  redis {
    host = "127.0.0.1"
    port = "6379"
    password = ""
    database = "0"
    minConn = 1
    maxConn = 10
    maxTotal = 100
    queryLimit = 100
  }

}

```

4.2 修改registry.conf

```

registry {
  # file 、nacos 、eureka、redis、zk、consul、etcd3、sofa
  # 修改注册中心为nacos方式
  type = "nacos"

  loadBalance = "RandomLoadBalance"
  loadBalanceVirtualNodes = 10

  nacos {
    application = "seata-server"
    # 修改nacos地址
    serverAddr = "49.232.166.94:8848"
    group = "SEATA_GROUP"
    # 在nacos中新增的命名空间的id
    namespace = "1c04b365-d834-442a-8b8d-a28861d1fc00"
    cluster = "default"
    # 配置nacos的用户名密码
    username = "nacos"
    password = "nacos"
  }
  eureka {
    serviceUrl = "http://localhost:8761/eureka"
    application = "default"
    weight = "1"
  }
  redis {
    serverAddr = "localhost:6379"
    db = 0
    password = ""
    cluster = "default"
    timeout = 0
  }
  zk {
    cluster = "default"
    serverAddr = "127.0.0.1:2181"
    sessionTimeout = 6000
    connectTimeout = 2000
    username = ""
    password = ""
  }
  consul {
    cluster = "default"
    serverAddr = "127.0.0.1:8500"
  }
  etcd3 {
    cluster = "default"
    serverAddr = "http://localhost:2379"
  }
  sofa {
    serverAddr = "127.0.0.1:9603"
    application = "default"
    region = "DEFAULT_ZONE"
    datacenter = "DefaultDataCenter"
    cluster = "default"
    group = "SEATA_GROUP"
    addressWaitTime = "3000"
  }
  file {
    name = "file.conf"
  }
}

config {
  # file、nacos 、apollo、zk、consul、etcd3
  type = "file"

  nacos {
    serverAddr = "127.0.0.1:8848"
    namespace = ""
    group = "SEATA_GROUP"
    username = ""
    password = ""
  }
  consul {
    serverAddr = "127.0.0.1:8500"
  }
  apollo {
    appId = "seata-server"
    apolloMeta = "http://192.168.1.204:8801"
    namespace = "application"
    apolloAccesskeySecret = ""
  }
  zk {
    serverAddr = "127.0.0.1:2181"
    sessionTimeout = 6000
    connectTimeout = 2000
    username = ""
    password = ""
  }
  etcd3 {
    serverAddr = "http://localhost:2379"
  }
  file {
    name = "file.conf"
  }
}
```

### 4.3 增加conf.txt文件

该文件在seata1.4源码包下
```
源码包/script/config-center/config.txt
```
将该文件复制到 seata二进制解压包的跟目录下

并做修改
```
transport.type=TCP
transport.server=NIO
transport.heartbeat=true
transport.enableClientBatchSendRequest=false
transport.threadFactory.bossThreadPrefix=NettyBoss
transport.threadFactory.workerThreadPrefix=NettyServerNIOWorker
transport.threadFactory.serverExecutorThreadPrefix=NettyServerBizHandler
transport.threadFactory.shareBossWorker=false
transport.threadFactory.clientSelectorThreadPrefix=NettyClientSelector
transport.threadFactory.clientSelectorThreadSize=1
transport.threadFactory.clientWorkerThreadPrefix=NettyClientWorkerThread
transport.threadFactory.bossThreadSize=1
transport.threadFactory.workerThreadSize=default
transport.shutdown.wait=3
service.vgroupMapping.my_test_tx_group=default
service.default.grouplist=127.0.0.1:8091
service.enableDegrade=false
service.disableGlobalTransaction=false
client.rm.asyncCommitBufferLimit=10000
client.rm.lock.retryInterval=10
client.rm.lock.retryTimes=30
client.rm.lock.retryPolicyBranchRollbackOnConflict=true
client.rm.reportRetryCount=5
client.rm.tableMetaCheckEnable=false
client.rm.sqlParserType=druid
client.rm.reportSuccessEnable=false
client.rm.sagaBranchRegisterEnable=false
client.tm.commitRetryCount=5
client.tm.rollbackRetryCount=5
client.tm.defaultGlobalTransactionTimeout=60000
client.tm.degradeCheck=false
client.tm.degradeCheckAllowTimes=10
client.tm.degradeCheckPeriod=2000
store.mode=file
store.file.dir=file_store/data
store.file.maxBranchSessionSize=16384
store.file.maxGlobalSessionSize=512
store.file.fileWriteBufferCacheSize=16384
store.file.flushDiskMode=async
store.file.sessionReloadReadSize=100
## 这里需要修改 因为文件是txt格式 最后使用时要把注释去掉
store.db.datasource=druid
store.db.dbType=mysql
store.db.driverClassName=com.mysql.cj.jdbc.Driver
store.db.url=jdbc:mysql://49.232.166.94:3306/example-seata?useUnicode=true
store.db.user=root
store.db.password=QQliuhe951@@
store.db.minConn=5
store.db.maxConn=30
store.db.globalTable=global_table
store.db.branchTable=branch_table
store.db.queryLimit=100
store.db.lockTable=lock_table
store.db.maxWait=5000
store.redis.host=127.0.0.1
store.redis.port=6379
store.redis.maxConn=10
store.redis.minConn=1
store.redis.database=0
store.redis.password=null
store.redis.queryLimit=100
server.recovery.committingRetryPeriod=1000
server.recovery.asynCommittingRetryPeriod=1000
server.recovery.rollbackingRetryPeriod=1000
server.recovery.timeoutRetryPeriod=1000
server.maxCommitRetryTimeout=-1
server.maxRollbackRetryTimeout=-1
server.rollbackRetryTimeoutUnlockEnable=false
client.undo.dataValidation=true
client.undo.logSerialization=jackson
client.undo.onlyCareUpdateColumns=true
server.undo.logSaveDays=7
server.undo.logDeletePeriod=86400000
client.undo.logTable=undo_log
client.log.exceptionRate=100
transport.serialization=seata
transport.compressor=none
metrics.enabled=false
metrics.registryType=compact
metrics.exporterList=prometheus
metrics.exporterPrometheusPort=9898
```
## 5. 将conf.txt 配置文件导入到nacos配置中心

脚本在源码包
```
源码包/script/config-center/nacos/nacos-config.sh
```
将脚本复制到config.txt同级目录下，执行命令将config.txt中的配置导入到nacos中
```
sh nacos-config.sh -h 49.232.166.94 -p 8848 -g SEATA_GROUP -t 1c04b365-d834-442a-8b8d-a28861d1fc00 -u nacos -w nacos
```
-h nacos 注册中心地址

-p nacos 注册终结端口

-g nacos 在seata的registry.conf配置文件中 指定的nacos 使用的group

-t nacos 中的命名空间的id

## 6. 启动seata服务端

```
sh seata二进制解压包/bin/seata-server.sh
```
启动成功后seata默认监听8091端口,并将服务注册到注册中心中。

# 在springboot业务服务中引用使用seata-client

## 1. 引入seata依赖包

```
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-seata</artifactId>
</dependency>
```
或者直接引用该组件。

## 2. 在springboot 服务中增加依赖
```
seata:
  enabled: true
  application-id: cloud-example-storage
  tx-service-group: my_test_tx_group
  service:
    vgroup-mapping.my_test_tx_group: default
    grouplist: 49.232.166.94:8091
```
