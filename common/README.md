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
