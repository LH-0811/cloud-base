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
## 2021.4.15 增加数据权限配置
思路：
```
客户端获取数据都是从后端的api接口中获取到的。
所以想要控制用户的数据权限可以从api接口入手。
在用户的权限信息中添加描述类： api接口地址、排除哪些字段。（这个封装为LhitSecurityDataRule类）
然后只需要在controller方法返回值时将对应的字段移除掉就可以了。

具体步骤
1. 自定义注解 LhitDataIntercept ，凡是被这个注解标记的controller方法的返回值，都会做数据过滤
2. 增加AOP切面操作，LhitSecurityReturnDataAop。在满足条件的方法的环绕通知中处理返回结果后在返回到controller方法。
```

具体使用:


```
1. 维护用户权限信息 ，即在用户登录成功后，将数据过滤规则加载到用户权限信息中
@LhitUserVerification
public class MyUserVerificationAdapter implements LhitSecurityUserVerificationAdapter<UsernamePasswordUserVerification> {


    @Override
    public LhitSecurityUserPerms verification(UsernamePasswordUserVerification verification) throws Exception {
        if (!"user".equals(verification.getUsername())) {
            throw CommonException.create(ServerResponse.createByError("用户名错误，默认用户：user"));
        } else if (!"123456".equals(verification.getPassword())) {
            throw CommonException.create(ServerResponse.createByError("密码不正确：默认密码123456"));
        } else {
            LhitSecurityRole role = new LhitSecurityRole("admin");

            LhitSecurityDataRule dataRule1 = new LhitSecurityDataRule();
            dataRule1.setApiPath("/data_intercept");
            dataRule1.setRoleId("1");
            dataRule1.setExcludeFields(Lists.newArrayList("score"));

            LhitSecurityDataRule dataRule2 = new LhitSecurityDataRule();
            dataRule2.setApiPath("/data_intercept/student");
            dataRule2.setRoleId("1");
            dataRule2.setExcludeFields(Lists.newArrayList("score"));

            LhitSecurityPermission permission = new LhitSecurityPermission("/**", "dept", "all", Lists.newArrayList(dataRule1,dataRule2));
            DefaultLhitSecurityUser user = DefaultLhitSecurityUser.builder().userId("default").password("user").username("user").build();
            return new LhitSecurityUserPerms(Lists.newArrayList(new LhitSecurityRole[]{role}), Lists.newArrayList(new LhitSecurityPermission[]{permission}), user.getUserId(), user);
        }

    }
}



2. 标记需要过滤数据的接口
@RestController
@RequestMapping("/data_intercept")
@Api(tags = "测试数据拦截")
public class DataInterceptController {


    @LhitDataIntercept
    @GetMapping("/student")
    @ApiOperation("测试数据拦截")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
    })
    public ServerResponse<List<Room>> getStudentInfo(@RequestHeader(value = "LHTOKEN", defaultValue = "") String token) throws Exception {
        ArrayList<Student> students = Lists.newArrayList();
        students.add(new Student("1", "1", "1"));
        students.add(new Student("2", "2", "2"));
        students.add(new Student("3", "3", "3"));
        students.add(new Student("3", "3", "3"));
        students.add(new Student("3", "3", "3"));

        Room room2 = new Room("12121",students);
        Room room3 = new Room("122",students);
        Room room4 = new Room("11122",students);

        return ServerResponse.createBySuccess(Lists.newArrayList(room2,room3,room4));
    }


    @LhitDataIntercept
    @GetMapping
    @ApiOperation("测试数据拦截")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = "LHTOKEN", value = "用户token"),
    })
    public ServerResponse<Room> getStudentInfo2(@RequestHeader(value = "LHTOKEN", defaultValue = "") String token) throws Exception {
        ArrayList<Student> students = Lists.newArrayList();
        students.add(new Student("1", "1", "1"));
        students.add(new Student("2", "2", "2"));
        students.add(new Student("3", "3", "3"));
        students.add(new Student("3", "3", "3"));
        students.add(new Student("3", "3", "3"));
        Room room = new Room("12121",students);
        return ServerResponse.createBySuccess(room);
    }

}

```

