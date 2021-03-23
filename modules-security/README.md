# lh-springboot-starter-security
为简单的springboot web 项目封装安全框架。
Demo：https://gitee.com/lh_0811/lh-web-security-demo

[![yvd6k4.png](https://s3.ax1x.com/2021/02/25/yvd6k4.png)](https://imgtu.com/i/yvd6k4)

**流程图: https://gitee.com/lh_0811/lh-springboot-starter-security/blob/master/lh-security.jpg**

# 当前版本的存储 
没有使用任何的中间件，默认使用Guava来存储到内存中。 

## 集成
1. mvn clean install 项目本地安装
2. 在springboot-web项目中引用
```
   <dependency>
       <groupId>com.lhit.starter</groupId>
       <artifactId>lh-springboot-starter-security</artifactId>
       <version>1.0-SNAPSHOT</version>
   </dependency>
```
## 使用
#### 基础安全防护使用
1. 自定义 凭证类 eg:用户名+密码+验证码 或者 手机号+验证码
2. 自定义 凭证类对应的凭证验证适配器类 eg:用户名+密码+验证码的验证适配器（通过用户名密码登录，然后返回用户的权限信息）
3. 自定义 资源保护适配器 eg：提供需要保护的url(资源路径) eg: 保护全部资源路径 (/**)
4. 自定义 token生成器 当通过凭证验证适配器类后 生成用户token 返回给客户端
5. 修改配置文件 确定那些url是不需要权限认证的

## 注意点
1. 系统中的用户 应该实现LhitSecurityUser接口 用于提供token存储是提供getUserId();
2. 自定义 凭证类对应的凭证验证适配器类 （LhitSecurityUserVerificationAdapter<T extends LhitSecurityUserVerification>）应在类标注@LhitUserVerification注解。用于认证分发器(LhitSecurityVerificationDispatchAdapter)来 分发对应的凭证到凭证验证适配器
 
## permsCode方法级别的安全认证
用户通过凭证验证适配器后 会获取到LhitSecurityUserPerms对象，其中的属性LhitSecurityPermission 保存了用户用有的资源权限(url)和操作权限码(permsCode),框架通过url来判断用户能不能在controller层访问资源，通过permsCode判断用户是否有权限操作某个方法。
对于要进行权限判断的方法通过@HasPermsCode注解来标注 String[] value() default {}; 传入该方法需要的权限码permsCode，只要用户有其中一个权限码就可以调用该方法。

## 配置项说明

详见LhitSecurityProperties

```
# 启动基础安全防护
lhit.security.defense.enable=true

# 配置token在header中的key
lhit.security.defense.tokenkey=LH_TOKEN

# token有效期（分）
lhit.security.defense.expire="15"

# 配置要忽略安全认证的url
lhit.security.defense.ignore[0]=/role/query
lhit.security.defense.ignore[1]=/role/add
lhit.security.defense.ignore[2]=/default/**

# 无权限返回码
lhit.security.defense.un_authorized_code="401"
# 权限不足返回码
lhit.security.defense.un_enough_authorized_code="403"

```
