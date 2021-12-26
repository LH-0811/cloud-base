# 戌狗-安全组件---客户端

包说明

```
component: 组件
|
--annotation: 注解提供鉴权方法
|
--provide：资源提供默认实现
service: 客户端组件api
|
--impl: 客户端组件默认实现
util: 工具类包
SecurityClientAutoConfiguration 自动装配。
```

## 一、概述

客户端利用AOP特性，当访问被

HasPermsCode

HasStaticResPath

HasUrl

TokenToAuthority

的方法时，客户端会向授权中心发起请求，获取到当前客户的权限描述类,并判断当前用户是否有权限访问当前资源或方法。

权限描述类

```java

@ApiModel(value = "权限信息类")
public class SecurityAuthority {

    @ApiModelProperty(value = "用户token")
    private String token;

    @ApiModelProperty(value = "用户信息")
    private SecurityUser securityUser;

    @ApiModelProperty(value = "角色信息")
    private List<SecurityRole> securityRoleList;

    @ApiModelProperty(value = "资源列表")
    private List<SecurityRes> securityResList;

}
```

## 二、涉及到的配置文件说明

```java
public class XuGouSecurityProperties {

    /**
     * 标注当前应用是否使用springcloud框架。
     * 如果为true。
     * 当客户端需要鉴权时，会通过配置文件serverName服务名，来随机获取一个鉴权中心示例的ip+port。并发起http请求，去获取用户权限信息。
     * 并判断用户是否有权限访问资源或方法
     * 如果为false
     * 则会在当前spring容器上下文获取到SecurityCheckAuthority示例来判断用户是否有权限。
     */
    private Boolean useCloud = true;

    /**
     * 如果配置了useCloud = true;
     * 则需要配置注册中心中授权中心的服务名
     */
    private String serverName = "authorize-center-server";

    /**
     * 如果配置了useCloud = true;
     * 则需要配置授权中心提供的token换授权信息的接口地址。 
     * 一般默认不配置就好。除非与项目中其他请求冲突
     */
    private String serverUrlOfTokenToAuthority = "/xugou/security/token_to_authority";

}
```

## 三、实现流程

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

## 四、使用鉴权能力

SecurityClient 可以直接在集成的服务中作为工具类使用 例如：

```java
public class Test {
    @Autowired
    private SecurityClient securityClient;

    // 判断是否有permsCode 获取到用户权限信息没有抛出异常则就是有权限的
    SecurityAuthority securityAuthority = securityClient.hasPermsCode(permsCode);
}
```

提供注解 方便使用鉴权能力

HasPermsCode

HasStaticResPath

HasUrl

TokenToAuthority 

凡是被以上注解标注的方法，在通过注解后 都会想方法入参中SecurityAuthority 类的参数

补充用户权限信息。 例如： 
```java
@Slf4j
@Api(tags = "用户中心-系统管理员接口")
@RestController
@RequestMapping("/sys_user")
public class SysUserController extends BaseController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 创建用户
     *
     * @param param
     * @throws Exception
     */
    @PostMapping("/create")
    @ApiOperation("创建用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysUserCreateParam", dataTypeClass = SysUserCreateParam.class, name = "param", value = "参数")
    })
    @HasUrl
    public ServerResponse createUser(@Valid @RequestBody SysUserCreateParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 创建用户 接口 : SysUserAdminController-createUser ");
        sysUserService.createUser(param, getCurrentSysUser(securityAuthority));
        return ServerResponse.createBySuccess("创建成功");
    }
}
```


