# 戌狗-安全组件

> 作者： LH0811 QQ邮箱:2329385085@qq.com (加qq好友请标注来意)

> 说明：
> 《七绝·咏狗》
> 伏卧门前体态躬，看家护院夜巡更。
> 胆略惊天吞日月，一片丹心卫主人。
> 该模块是第二个以十二生肖动物名命名的模块，狗子看家护院的职责与守护项目安全的能力十分贴合。

## 一、概述

组件分为服务端和客户端两个部分。

集成服务端的应用 要提供，用户认证，token鉴权服务。

集成客户端的应用 有主动请求服务端进行鉴权的能力。

## 二、简要使用步骤
### 2.1 服务端集成
#### 2.1.1 引入pom
```xml
<!-- 安全框架 服务端依赖 -->
<dependency>
    <groupId>com.cloud.modules</groupId>
    <artifactId>xugou-security-server</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```
#### 2.1.2 修改配置文件
```yaml
xugou:
  security:
    use-cloud: true
    server-name: authorize-center-server
    server-url-of-token-to-authority: /xugou/security/token_to_authority
    token-key: LHTOKEN
    expire: 9999999
```
#### 2.1.3 编写应用组件
具体使用开一参考本项目中的authorize-center实现。
##### 2.1.3.1 登录验证授权
###### 自定义凭证类
```java

import SecurityVoucher;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author lh0811
 * @date 2021/5/8
 */
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
```
###### 自定义凭证类认证器
这一步举例为最简单的认证方式。实际的应用中应该去数据库中查询用户信息。
```java

import CommonException;
import ServerResponse;
import DefaultUsernamePasswordVoucher;
import SecurityVoucherVerification;
import SecurityAuthority;
import SecurityRes;
import SecurityRole;
import SecurityUser;
import com.google.common.collect.Lists;

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
            securityAuthority.setSecurityRoleList(Lists.newArrayList(new SecurityRole(0L,"roleNo","管理员")));
            securityAuthority.setSecurityResList(Lists.newArrayList(SecurityRes.allCodeRes(), SecurityRes.allUrlRes()));
            return securityAuthority;
        }
        throw CommonException.create(ServerResponse.createByError("用户名或密码错误，默认admin-123456"));
    }
}
```
#### 2.1.4 框架应用示例-开放认证接口
```java
package com.cloud.base.authorize.controller;

import com.cloud.base.authorize.security.verification.UsernamePasswordVerification;
import ServerResponse;
import TokenParam;
import AuthenticationVo;
import SecurityServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author lh0811
 * @date 2021/5/23
 */
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
            @ApiImplicitParam(paramType = "header", dataType = "string", name = CommonConstant.TokenKey, value = "用户token"),
    })
    public ServerResponse logout(@RequestHeader(value = CommonConstant.TokenKey, defaultValue = "") String token) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 获取当前用户信息 接口 : SysUserController-logout");
        securityServer.tokenDestroy(new TokenParam(token));
        return ServerResponse.createBySuccess("退出成功");
    }

}
```

### 2.2 客户端集成
#### 2.2.1 引入pom
```xml
<!--   security     -->
<dependency>
    <groupId>com.cloud.modules</groupId>
    <artifactId>xugou-security-client</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```
#### 2.2.2 修改配置文件
无
#### 2.2.3 编写应用组件
无
#### 2.2.4 框架应用示例-鉴权
所有项目中 被@HasUrl标注的Controller类 或者 Controller类中的方法 都会验证当前用户是否有权限访问当前资源。

除了@HasUrl注解外，还提供了

HasPermsCode - 验证是否有权限码权限

HasStaticResPath - 验证是否有该静态资源的访问权限

TokenToAuthority - 将token转换为当前权限描述类，可用于检验用户是否登录

详细的使用示例 可以参考本项目中的user-center模块实现和使用。

```java
package com.cloud.base.user.controller;

import ServerResponse;
import HasUrl;
import SecurityAuthority;
import com.cloud.base.user.param.SysPositionCreateParam;
import com.cloud.base.user.param.SysPositionQueryParam;
import com.cloud.base.user.service.SysPositionService;
import com.cloud.base.user.vo.SysPositionVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @author lh0811
 * @date 2021/8/17
 */
@Slf4j
@Api(tags = "用户中心-岗位管理接口")
@RestController
@RequestMapping("/sys_positions")
@HasUrl
public class SysPositionController extends BaseController {

    @Autowired
    private SysPositionService sysPositionService;


    /**
     * 创建岗位信息
     */
    @PostMapping("/create")
    @ApiOperation("创建岗位信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = CommonConstant.TokenKey, value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysPositionCreateParam", dataTypeClass = SysPositionCreateParam.class, name = "param", value = "参数")
    })
    public ServerResponse createPosition(@Validated @RequestBody SysPositionCreateParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 创建岗位信息 接口 : SysPositionController-createPosition ");
        sysPositionService.createPosition(param, getCurrentSysUser(securityAuthority));
        return ServerResponse.createBySuccess("创建成功");
    }

    /**
     * 删除岗位信息
     */
    @DeleteMapping("/delete/{positionId}")
    @ApiOperation("删除岗位信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = CommonConstant.TokenKey, value = "用户token"),
            @ApiImplicitParam(paramType = "path", dataType = "Long", dataTypeClass = Long.class, name = "positionId", value = "岗位id")
    })
    public ServerResponse deletePosition(@PathVariable(value = "positionId") Long positionId, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 删除岗位信息 接口 : SysPositionController-deletePosition ");
        sysPositionService.deletePosition(positionId, getCurrentSysUser(securityAuthority));
        return ServerResponse.createBySuccess("删除成功");
    }

    /**
     * 查询岗位信息
     */
    @PostMapping("/query")
    @ApiOperation("查询岗位信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = CommonConstant.TokenKey, value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysPositionQueryParam", dataTypeClass = SysPositionQueryParam.class, name = "param", value = "参数")
    })
    public ServerResponse<PageInfo<SysPositionVo>> queryPosition(@Validated @RequestBody SysPositionQueryParam param, @ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 查询岗位信息 接口 : SysPositionController-queryPosition ");
        PageInfo<SysPositionVo> sysPositionPageInfo = sysPositionService.queryPosition(param, getCurrentSysUser(securityAuthority));
        return ServerResponse.createBySuccess("获取成功", sysPositionPageInfo);
    }

    /**
     * 查询全部岗位列表
     */
    @GetMapping("/query/all")
    @ApiOperation("查询岗位信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "string", name = CommonConstant.TokenKey, value = "用户token"),
            @ApiImplicitParam(paramType = "body", dataType = "SysPositionQueryParam", dataTypeClass = SysPositionQueryParam.class, name = "param", value = "参数")
    })
    public ServerResponse<List<SysPositionVo>> queryAllPosition(@ApiIgnore SecurityAuthority securityAuthority) throws Exception {
        log.info("|-----------------------------------------------|");
        log.info("进入 查询岗位信息 接口 : SysPositionController-queryPosition ");
        List<SysPositionVo> sysPositionVoList = sysPositionService.queryAllPosition(getCurrentSysUser(securityAuthority));
        return ServerResponse.createBySuccess("获取成功", sysPositionVoList);
    }
}
```




