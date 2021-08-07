# common-sentinel 模块

与core-common 一致，只是在其基础上添加统一异常处理时对sentinel的支持。


# common提供的封装
```
entity: 
1. 通用实体类（默认分页参数值） 通用方法（堆栈打印到log，listToTree）
2. 统一服务端返回
exception:
1. 统一异常类
2. 控制器加强 处理全局异常 和 controller层入参校验异常
util：工具类

```

# 统一异常处理使用方法
需要校验的参数增加校验注解
```
/**
 * @author lh0811
 * @date 2021/3/29
 */
@Getter
@Setter
public class ExampleParam {

    @NotEmpty(message = "未上传 姓名")
    @ApiModelProperty(value = "姓名",required = true)
    private String name ;


    @NotNull(message = "未上传 得分")
    @Range(min = 0,max = 100,message = "得分不合法 0-100")
    @ApiModelProperty(value = "得分",required = true)
    private Integer score ;
}

```

对应接口方法使用注解@Validated
```
    @PostMapping("/name")
    @ApiOperation(value = "测试入参合法性")
    public ServerResponse nameTest(@Validated @RequestBody ExampleParam param) throws Exception {
        return ServerResponse.createBySuccess("校验通过",param);
    }
```

捕获异常后处理
```
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ServerResponse throwCustomException(MethodArgumentNotValidException methodArgumentNotValidException){
        return ServerResponse.createByError("非法参数",methodArgumentNotValidException.getBindingResult().getFieldError().getDefaultMessage());
    }
```
# 线程日志打印 ThreadLog
使用示例
```
@PostMapping("/query/dept_user")
@ApiOperation("测试查询部门用户")
@ApiImplicitParams({
        @ApiImplicitParam(paramType = "body", dataType = "SysDeptUserQueryParam", dataTypeClass = SysDeptUserQueryParam.class, name = "param", value = "参数")
})
public ServerResponse<PageInfo<DeptUserDto>> selectDeptUser(@RequestBody SysDeptUserQueryParam param) throws Exception {
    ThreadLog.info().input("开始 测试查询部门用户 TestController-selectDeptUser: param="+JSON.toJSONString(param));
    // 查询
    PageHelper.startPage(param.getPageNum(), param.getPageSize());
    List<DeptUserDto> deptUserDtos = deptUserCustomDao.selectDeptUser(param);
    PageInfo pageInfo = new PageInfo(deptUserDtos);
    PageHelper.clearPage();
    // 查询完成
    ThreadLog.info().input("完成 测试查询部门用户 TestController-selectDeptUser");

    // 输出当前线程日志
    ThreadLog.info().output();
    return ServerResponse.createBySuccess("查询成功",pageInfo);
}

输出示例：

2021-08-07 20:16:06.689  INFO [user-center-server,d2e5240a92fcf4c5,d2e5240a92fcf4c5,true] 2327 --- [nio-9301-exec-3] c.cloud.base.core.common.util.ThreadLog  : [ThreadId:81] 开始 测试查询部门用户 TestController-selectDeptUser: param={"createTimeLow":1628336537000,"deptId":1,"pageNum":1,"pageSize":15}
2021-08-07 20:16:06.689  INFO [user-center-server,d2e5240a92fcf4c5,d2e5240a92fcf4c5,true] 2327 --- [nio-9301-exec-3] c.cloud.base.core.common.util.ThreadLog  : [ThreadId:81] 完成 测试查询部门用户 TestController-selectDeptUser

```
