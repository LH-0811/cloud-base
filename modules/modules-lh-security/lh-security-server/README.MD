# 安全组件---服务端

## 一、包说明
```
authentication: 授权相关组件封装
controller: 对外开放接口
service: 核心服务（token颁发 和 销毁）
token: token管理、token生成
voucher: 凭证类封装

SecurityServerAutoConfiguration 自动装配
```

## 二、流程说明

1. 调用方凭证类封装
2. 授权服务通过SecurityServer中提供的authorize()方法进行认证
    2.1 authorize()中调用SecurityVoucherVerificationProcess接口中提供的认证过程处理方法认证凭证并返回token
    2.2 将认证返回的token管理到TokenManager并与用户权限信息关联
3. 销毁token通过SecurityServer中提供的tokenDestroy()方法销毁token
    
