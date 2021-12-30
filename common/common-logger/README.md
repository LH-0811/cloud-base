# lh-springboot-starter-logger
web项目日志存储框架，用于记录controller层 记录用户请求和接口返回数据的日志记录。Demo：https://github.com/LH-0811/demo-lh-logger

# 1. 安装框架到本地maven。
##### 1.1 clone本项目到本地
```
git clone https://github.com/LH-0811/lh-springboot-starter-logger.git
```
##### 1.2 使用maven安装到本地

```
cd ${项目所在目录}/lh-springboot-starter-logger
maven clean install
```

# 2. 如何使用框架。
##### 2.1 在已有的web项目工程中引用当前项目
```
        <dependency>
            <groupId>com.lhit</groupId>
            <artifactId>lh-springboot-starter-logger</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
```

##### 2.2 在配置文件中打开项目配置
```
lhit.logger.enabled=true
```
##### 2.3 在需要记录日志的接口上添加注解
```
  @LhitLogger(title = "添加用户", businessType = LoggerBusinessType.INSERT,isSaveRequestData = true,isSaveOperNetAddr = true,isSaveOperRoleInfo = true,isSaveOperUserInfo = true,isSaveReturnData = true)
    @PostMapping("/user/insert/{key1}")
    public ResponseEntity addAllUser(@RequestBody SysUser sysUser, @PathVariable String key1, @RequestParam(value = "key2") String key2) {
        log.info("UserController key1:{}",key1);
        log.info("UserController key2:{}",key2);
        log.info("UserController sysUser:{}",sysUser);
        int i = 1/0;
        userList.add(sysUser);
        return ResponseEntity.ok("添加成功");
    }
```

##### 2.4 需要自定义的adapter
1. 自定义通过客户端的request请求获取到当前系统中用户信息的adapter
```
import com.example.demolhlogger.pojo.SysUser;
import com.lhit.starter.logger.adapter.LhitLoggerUserInfoFromRequestAdapter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class MyUserInfoFromRequest implements LhitLoggerUserInfoFromRequestAdapter<SysUser> {
    @Override
    public SysUser getUserInfoFromRequest(HttpServletRequest httpServletRequest) {
        //todo 这里做演示 只是简单的返回一个用户信息 这里应该是通过request中的信息 获取到 真正对应的用户信息。
        return SysUser.builder().userId(1).username("user1").password("123456").build();
    }
}
```
2. 自定义通过用户信息 获取到用户角色信息的adapter
```
import com.example.demolhlogger.pojo.SysRole;
import com.example.demolhlogger.pojo.SysUser;
import com.lhit.starter.logger.adapter.LhitLoggerRoleInfoByUserAdapter;
import org.springframework.stereotype.Component;

@Component
public class MyRoleInfoByUserAdapter implements LhitLoggerRoleInfoByUserAdapter<SysRole, SysUser> {
    @Override
    public SysRole getRoleInfoByUserInfo(SysUser sysUser) {
        //todo 这里做演示 只是简单的返回一个角色信息，真正需要通过用户信息来确定用户对应的角色信息
        if (sysUser.getUserId() == 1)
            return SysRole.builder().name("用户1-系统角色").build();
        return SysRole.builder().name("默认-系统角色").build();
    }
}
```
3. 自定义日志存储方式adapter
```
import com.alibaba.fastjson.JSONObject;
import com.lhit.starter.logger.adapter.LhitLoggerStorageAdapter;
import com.lhit.starter.logger.entity.LhitLoggerEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyLoggerStorageAdapter implements LhitLoggerStorageAdapter {
    @Override
    public void storageLogger(LhitLoggerEntity lhitLoggerEntity) {
        //todo 这里是做简单的示例，真正项目中应该是存到mysql或者是其他DB中
        log.info("自定义的日志存储:{}", JSONObject.toJSONString(lhitLoggerEntity));
    }
}
```

# 3. 框架是如何工作的。

框架通过aop来实现方法的拦截，因为这个框架只是处理服务器中controller层的api日志，所以不是@Controller|@RestController注解类中的方法是无效的。



