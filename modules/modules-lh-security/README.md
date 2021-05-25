# 安全组件

> 说明：组件分为服务端和客户端两个部分
> 
> 集成服务端的应用 要提供，用户认证，token鉴权服务
>
> 集成客户端的应用 有主动请求服务端进行鉴权的能力

配置文件说明:
```
@Getter
@Setter
// 请求Header中token的key
private String tokenKey = "LHTOKEN";

@Getter
@Setter
// token 过期时间 单位是分钟
private String expire = "9999999";

@Getter
@Setter
// 无权限返回码
private String noAuthorizedCode = "401";

@Getter
@Setter
// 权限不足返回码
private String unAuthorizedCode = "403";

@Getter
@Setter
// 校验url权限的服务url
private String serverUrlOfCheckUrl = "/security/check_url";

@Getter
@Setter
// 校验permsCode权限的服务url
private String serverUrlOfCheckPermsCode = "/security/check_perms_code";

@Getter
@Setter
// 校验permsCode权限的服务url
private String serverUrlOfCheckStaticResPath = "/security/check_static_res_path";

@Getter
@Setter
// 校验permsCode权限的服务url
private String serverUrlOfTokenToAuthority = "/security/token_to_authority";

@Getter
@Setter
// 鉴权服务端是否使用 springcloud
private Boolean useCloud = true;

@Getter
@Setter
private String serverName = "authorize-center-server";
```

## 一、服务端集成
#### 1.1 在pom中增加坐标
```
<!-- 安全框架 服务端依赖 -->
<dependency>
    <groupId>com.cloud.modules</groupId>
    <artifactId>lh-security-server</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```
#### 1.2 实现 用户认证授权和 token销毁接口
使用 Security服务提供的 SecurityServer组件可以完成 
```
@Slf4j
@Api(tags = "认证接口")
@RestController
@RequestMapping("/security")
public class AuthorizeController {


    @Autowired
    private SecurityServer securityServer;


    @PostMapping("/login/username_password")
    @ApiOperation("系统用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UsernamePasswordVerification", dataTypeClass = UsernamePasswordVerification.class, name = "param", value = "参数")
    })
    public ServerResponse<AuthenticationVo> sysUserLoginByUsernamePassword(@Validated @RequestBody UsernamePasswordVerification param) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 系统用户登录 接口 : LoginAuthenticationController-sysUserLoginByUsernamePassword ");
        AuthenticationVo authorize = securityServer.authorize(param);
        return ServerResponse.createBySuccess("登录成功", authorize);
    }

    @GetMapping("/logout")
    @ApiOperation("用户退出")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
    })
    public ServerResponse logout(@RequestHeader(value = "LHTOKEN", defaultValue = "") String token) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 获取当前用户信息 接口 : SysUserController-logout");
        securityServer.tokenDestroy(new TokenParam(token));
        return ServerResponse.createBySuccess("退出成功");
    }

}
```







