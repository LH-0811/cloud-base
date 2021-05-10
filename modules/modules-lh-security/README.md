# 安全组件
当前组件是为了在基于gateway网格的情况下 需要在网格做统一鉴权使用的。

gateway是基于webFlux的网关应用，不能兼容springMVC。

所以服务端和客户端是通过http来完成通讯的。

## 基础CORE
提供了基础的Entity和Param

## 服务端
服务端提供了 用户授权、用户鉴权的能力。

实现流程：
```
1. 自定义凭证类（SecurityVoucher）、凭证类认证器(SecurityVoucherVerification)
2. 授权认证流程(SecurityVoucherVerificationProcess)
    2.1 授权认证流程（SecurityVoucherVerificationProcess） 接受 凭证类
    2.2 找到凭证类（SecurityVoucher）对应的 凭证类认证器(SecurityVoucherVerification)
    2.3 调用凭证认证器的认证方法完成认证，并获取到用户权限信息(SecurityAuthority)
    2.4 根据权限 通过tokenManager管理器(tokenManager)生成token，并将信息保存。
    2.5 完成授权
3. 鉴权服务
    3.1 鉴权能力实现(SecurityCheckAuthority)
    3.2 鉴权能力基于netty开放http接口,地址在配置文件中指定。
        3.2.1 checkUrl - URL级
        3.2.2 checkPermsCode - 权限码
        3.2.3 checkStaticResPath - 静态资源地址
``` 

接入应用实现自定义授权步骤
```
1. 自定义那个凭证类 实现 (SecurityVoucher) 接口
2. 实现对应凭证的认证器（SecurityVoucherVerification<T extends SecurityVoucher>）
3. 使用SecurityVoucherVerificationProcess接收凭证类并完成认证授权

@Slf4j
@RestController
public class LoginController {

    @Autowired
    private SecurityVoucherVerificationProcess process;

    @GetMapping("/login")
    public ServerResponse login(@RequestParam String username,@RequestParam String password) throws Exception {
        String token = process.voucherVerificationProcess(new DefaultUsernamePasswordVoucher(username, password));
        return ServerResponse.createBySuccess("登录成功",token);
    }
}
```
配置文件说明：
```
// token 过期时间 单位是分钟
lhit.security.server.expire = "9999999";

// 无权限返回码
lhit.security.server.no-authorizedCode = "401";

// 权限不足返回码
lhit.security.server.un-authorizedCode = "403";

// 校验url权限的服务url
lhit.security.server.server-url-of-check-url = "/check_url";

// 校验permsCode权限的服务url
lhit.security.server.server-url-of-check-perms-code = "/check_perms_code";

// 校验permsCode权限的服务url
lhit.security.server.server-url-of-check-static-res-path = "/check_static_res_path";
```
 

## 客户端

客户端提供了三种注解来完成三种资源类型的鉴权HasUrl、HasPermsCode、HasStaticResPath

因为该客户端不能限制用户token的传入方式，所以需要集成应用将token的获取方式提供给SecurityClient,实现GetTokenFromContext接口
```
/**
 * @author lh0811
 * @date 2021/5/10
 */
@Component
public class DefaultGetTokenFromContext implements GetTokenFromContext {
    @Autowired
    private SecurityClientProperties securityClientProperties;

    @Override
    public String getToken() {
        ServletRequestAttributes ra= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request =  ra.getRequest();
        String header = request.getHeader(securityClientProperties.getTokenKey());
        return header;
    }
}
```

集成到应用中之后可以直接使用
```
@GetMapping("/url")
@HasUrl(url = "/url")
public ServerResponse url() throws Exception {
    return ServerResponse.createBySuccess("成功");
}

@GetMapping("/perms_code")
@HasPermsCode(permsCode = "perms:code")
public ServerResponse permsCode() throws Exception {
    return ServerResponse.createBySuccess("成功");
}

@GetMapping("/static_res_path")
@HasStaticResPath(resPath = "/static/res/path")
public ServerResponse staticResPath() throws Exception {
    return ServerResponse.createBySuccess("成功");
}
```
配置文件说明:
```
// 请求头中token的key
lhit.security.client.token-key = "LHTOKEN";

// 请求头中token的key
lhit.security.client.server-addr = "127.0.0.1";

// token 过期时间 单位是分钟
lhit.security.client.server-port = 8999;

// 无权限返回码
lhit.security.client.no-authorizedCode = "401";

// 权限不足返回码
lhit.security.client.un-authorizedCode = "403";

// 校验url权限的服务url
lhit.security.client.server-url-of-check-url = "/check_url";

// 校验permsCode权限的服务url
lhit.security.client.server-url-of-check-perms-code = "/check_perms_code";

// 校验permsCode权限的服务url
lhit.security.client.server-url-of-check-static-res-path = "/check_static_res_path";
```
