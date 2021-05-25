# 安全组件---客户端

包说明
```
component: 组件
   annotation: 注解提供鉴权方法
   provide：资源提供默认实现
service: 客户端组件
util: 工具类
SecurityClientAutoConfiguration 自动装配
```
## 实现流程
鉴权需要的两个条件
```
1. 请求方携带的身份token
2. 授权服务器的ip和端口号
```

这两个条件由Provide组件提供
```
public interface ProvideResToSecurityClient {

    /**
     * 从请求上下文中获取到用户token
     * 
     * @return
     * @throws Exception
     */
    String getTokenFromApplicationContext() throws Exception;

    /**
     * 从应用上下文中提供安全框架服务端地址
     *
     * @return
     */
    SecurityServerAddr getServerAddrFromApplicationContext() throws CommonException;

}
```


## 提供鉴权能力
```
SecurityClient 可以直接在集成的服务中作为工具类使用
例如：
@Autowired
private SecurityClient securityClient;

// 判断是否有permsCode
SecurityAuthority securityAuthority = securityClient.hasPermsCode(permsCode);

提供注解 方便使用鉴权能力
HasPermsCode
HasStaticResPath
HasUrl
TokenToAuthority
凡是被以上注解标注的方法，在通过注解后 都会想方法入参中SecurityAuthority 类的参数 补充用户权限信息。

例如：
@PostMapping("/sys_user/create")
@ApiOperation("创建用户")
@ApiImplicitParams({
        @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
        @ApiImplicitParam(paramType = "body", dataType = "SysUserCreateParam", dataTypeClass = SysUserCreateParam.class, name = "param", value = "参数")
})
@HasUrl(url = "/sys_admin/sys_user/create")
public ServerResponse createUser(@Validated @RequestBody SysUserCreateParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception
```


