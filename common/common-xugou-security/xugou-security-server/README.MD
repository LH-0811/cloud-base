# 戌狗-安全组件---服务端

包说明
```
authentication: 授权鉴权相关组件封装
controller: 对外开放接口
service: 核心服务（token颁发 和 销毁）
token: token管理、token生成
voucher: 示例-凭证类封装

SecurityServerAutoConfiguration 自动装配
```
## 一、概述
戌狗的服务端组件。

帮助开发人员快速在分布式系统中构建一个授权中心

或

在单体应用中快速集成授权鉴权能力。

利用接口和Spring Bean自动装配特性，保证服务框架的可拓展性。

可以快速实现多种的用户认证方式：

用户名+密码，手机号+验证码, 邮箱+验证码 ... 等等。

可以任意修改或者拓展 用户的token管理：

颁发用户Token，销毁用户Token，保证用户单点登录，允许用户多端登录 ... 等等。

以上能力，在框架的基础上均可快速拓展实现。


## 二、涉及到的配置文件说明

```java
@ConfigurationProperties(prefix = "xugou.security")
public class XuGouSecurityProperties {

    // 请求Header中token的key
    private String tokenKey = "LHTOKEN";

    // token 过期时间 单位是分钟
    private String expire = "9999999";

    // 当前token失效或未上传token返回码
    private String noAuthorizedCode = "401";

    // 无权限访问返回码
    private String unAuthorizedCode = "403";

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
```text
1. 调用方凭证类封装

2. 授权服务通过SecurityServer中提供的authorize()方法进行认证
    2.1 authorize()中调用SecurityVoucherVerificationProcess接口中提供的认证过程处理方法认证凭证并返回token
    2.2 将认证返回的token管理到TokenManager并与用户权限信息关联

3. 销毁token通过SecurityServer中提供的tokenDestroy()方法销毁token
```
![](OpenSources/cloud-base/common/modules-xugou-security/xugou-security-server/SecurityServer.jpg)

## 四、对客户端提供接口
```
// 校验permsCode权限的服务url
private String serverUrlOfTokenToAuthority = "/xugou/security/token_to_authority";
```
以上接口可以通过http直接调用

## 五、自定义认证方式
 
提供了默认的登录凭证 和 凭证类认证器

```java
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "用户名密码凭证类")
public class DefaultUsernamePasswordVoucher implements SecurityVoucher {

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

}

/**
 * 默认用户校验适配器
 *
 * @author lh0811
 * @date 2021/5/8
 */
public class DefaultUsernamePasswordVoucherVoucherVerification implements SecurityVoucherVerification<DefaultUsernamePasswordVoucher> {
    @Override
    public SecurityAuthority verification(DefaultUsernamePasswordVoucher defaultUsernamePasswordVoucher) throws Exception {
        if (defaultUsernamePasswordVoucher.getUsername().equals("admin") && defaultUsernamePasswordVoucher.getPassword().equals("123456")) {
            SecurityAuthority securityAuthority = new SecurityAuthority();
            securityAuthority.setSecurityUser(new SecurityUser("1", "admin"));
            securityAuthority.setSecurityRoleList(Lists.newArrayList(new SecurityRole("管理员")));
            securityAuthority.setSecurityResList(Lists.newArrayList(SecurityRes.allCodeRes(), SecurityRes.allUrlRes()));
            return securityAuthority;
        }
        throw CommonException.create(ServerResponse.createByError("用户名或密码错误，默认admin-123456"));
    }
}
```

如果想要自定义凭证类
只需要增加一个SecurityVoucher 的 实现类 自定义凭证字段
增加一个SecurityVoucherVerification<T>实现类 将泛型指定为其对应的凭证类。并完成认证。返回用户的权限信息即可。

## 六、自定义token规则
自定义TokenGenerate实现类 并给该类标注@Component，将其托管到spring容器中，会自动将默认的实现替换为自定义实现。

```java
/**
 * 默认token生成
 *
 * @author lh0811
 * @date 2021/5/8
 */
@Component
public class DefaultTokenGenerate implements TokenGenerate {
    @Override
    public String generate(SecurityAuthority authority) throws Exception {
        return UUID.randomUUID().toString();
    }
}
```


## 七、 自定义token管理
自定义TokenManager实现类 并给该类标注@Component，将其托管到spring容器中，会自动将默认的实现替换为自定义实现。

这里可以使用mysql、redis 等任意存储方式。默认提供了内存缓存GuavaCache的实现方式。

拓展该类 可以实现，单点登录，多点登录 任意控制。

```java
/**
 * 默认token管理器实现（基于 guavaCache）
 *
 * @author lh0811
 * @date 2021/5/8
 */
@Slf4j
@Component
public class DefaultTokenManager implements TokenManager {


    // token生成器
    @Autowired
    private TokenGenerate tokenGenerate;

    // 配置文件
    @Autowired
    private XuGouSecurityProperties xuGouSecurityProperties;

    // 缓存userId - token 对应关系
    private Cache<String, String> userIdTokenCache;

    // 缓存 token - SecurityAuthority 对应关系
    private Cache<String, SecurityAuthority> tokenAuthorityCache;

    public DefaultTokenManager() {

    }

    // 初始化缓存 过期时间在配置文件中指定
    @PostConstruct
    private void init() {
        this.userIdTokenCache = CacheBuilder.newBuilder().expireAfterAccess(Long.valueOf(xuGouSecurityProperties.getExpire()), TimeUnit.MINUTES)
                .build();
        this.tokenAuthorityCache = CacheBuilder.newBuilder().expireAfterAccess(Long.valueOf(xuGouSecurityProperties.getExpire()), TimeUnit.MINUTES)
                .build();
    }

    @Override
    public String tokenGenerateAndSave(SecurityAuthority securityAuthority) throws Exception {
        // 生成token
        String token = tokenGenerate.generate(securityAuthority);
        securityAuthority.setToken(token);
        // 存储token
        saveToken(token, securityAuthority);
        return token;
    }

    @Override
    public void saveToken(String token, SecurityAuthority authority) throws Exception {
        if (StringUtils.isEmpty(token))
            throw CommonException.create(ServerResponse.createByError("保存用户token失败，token不能为空"));
        if (authority.getSecurityUser() == null)
            throw CommonException.create(ServerResponse.createByError("保存用户token时，用户信息不能为空"));
        if (authority == null)
            throw CommonException.create(ServerResponse.createByError("保存用户token时，用户权限不能为空"));
        // 如果存在缓存 就先
        String oldToken = userIdTokenCache.getIfPresent(authority.getSecurityUser().getId());
        if (!StringUtils.isEmpty(oldToken)) {
            tokenAuthorityCache.invalidate(oldToken);
            userIdTokenCache.invalidate(authority.getSecurityUser().getId());
        }
        // 保存token与权限的关系
        userIdTokenCache.put(authority.getSecurityUser().getId(), token);
        tokenAuthorityCache.put(token, authority);
    }

    @Override
    public void removeToken(String token) throws Exception {
        // 获取到token对应的权限信息
        SecurityAuthority authority = tokenAuthorityCache.getIfPresent(token);
        if (authority == null) return;
        // 如果存在  移除token 与 perms的缓存
        tokenAuthorityCache.invalidate(token);
        // 移除掉userId 对应的 token缓存
        userIdTokenCache.invalidate(authority.getSecurityUser().getId());
    }

    @Override
    public Boolean checkToken(String token, String userId) throws Exception {
        // 获取到用户id对应的token
        String saveToken = userIdTokenCache.getIfPresent(userId);
        if (StringUtils.isEmpty(saveToken)) return false;
        // 判断token是否正确
        return token.equals(saveToken);
    }

    @Override
    public SecurityAuthority getSecurityAuthorityByToken(String token) throws Exception {
        if (StringUtils.isBlank(token)) {
            throw CommonException.create(ServerResponse.createByError(Integer.valueOf(xuGouSecurityProperties.getNoAuthorizedCode()), "未上传用户token，请登录后重试", ""));
        }
        // 获取token 存在的有效权限信息
        SecurityAuthority authority = tokenAuthorityCache.getIfPresent(token);
        if (authority == null) {
            throw CommonException.create(ServerResponse.createByError(Integer.parseInt(xuGouSecurityProperties.getNoAuthorizedCode()),"token无效"));
        }
        // 完成验证后 返回权限信息
        return authority;
    }

    @Override
    public void delayExpired(String token) throws Exception {
        //  使用cache中expireAfterAccess策略，则可以不用续期了，每次访问缓存会自动续期。
    }

}

```

## 八、如何实现应用授权

提供一个最简单的 客户端授权.

自定义客户端授权凭证类

```java
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AppIdSecretVerification implements SecurityVoucher {

    @NotBlank(message = "应用id 未上传")
    @ApiModelProperty(value = "应用id",required = true)
    private String appId;

    @NotBlank(message = "应用秘钥 未上传")
    @ApiModelProperty(value = "应用秘钥",required = true)
    private String secret;
}
```

自定义认证类

```java
@Slf4j
@Component
public class AppIdSecretVerificationAdapter implements SecurityVoucherVerification<AppIdSecretVerification> {

    @Autowired
    private SysAppClientService appClientService;

    @Override
    public SecurityAuthority verification(AppIdSecretVerification verification) throws Exception {
        log.info("开始执行 AppIdSecretVerificationAdapter.{},参数:{}", "verification", JSONObject.toJSONString(verification));
        return appClientService.verification(verification);
    }
}


@Service
public class SysAppClientServiceImpl implements SysAppClientService {

    @Autowired
    private SysAppClientDao sysAppClientDao;

    @Autowired
    private TokenManager tokenManager;

    /**
     * 通过用户名密码 获取用户信息 并组装权限信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SecurityAuthority verification(AppIdSecretVerification param) throws Exception {
        log.info("开始 通过 APPID 和 Secret 并组装权限信息:{}", JSON.toJSONString(param));
        SecurityAuthority securityAuthority = new SecurityAuthority();
        securityAuthority.setSecurityUser(new SecurityUser(String.valueOf(sysAppClient.getAppId()), sysAppClient.getAppName()));
        securityAuthority.setSecurityResList(Lists.newArrayList()); // 客户端授权可用资源
        securityAuthority.setSecurityRoleList(Lists.newArrayList()); // 客户端授权赋予角色
        return securityAuthority; 
    }
}
```

完成认证后与用户授权使用方法一致。携带token访问资源即可。
