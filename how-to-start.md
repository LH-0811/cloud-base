# DEV
1.本地启动nacos: http://127.0.0.1:8848
```shell
sh startup.sh -m standalone
```
2.本地启动sentinel: http://127.0.0.1:9000
```shell
nohup java -Dauth.enabled=false -Dserver.port=9000 -jar sentinel-dashboard-1.8.1.jar &
```
3.本地启动seata: http://127.0.0.1:8091
```shell
nohup sh seata-server.sh &
```
4.本地启动zipkin: http://127.0.0.1:9411/
```shell
nohup java -jar zipkin-server-2.23.2-exec.jar &
```

