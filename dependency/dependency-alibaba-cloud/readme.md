# 使用说明

该模块提供一个配置类，一个降级父类。

## FeignConfiguration Feign统一配置

实现功能

1. 将请求头中的参数，全部作为 feign 请求头参数传递

## FeignFallbackFactory 降级工厂父类

实现功能
1. feign客户端调用远程服务失败时的errmsg处理

eg：
feign客户端声明 MchtBaseInfoApi由服务提供方编写
```
// 这里fallbackFactory配置用于降级 
// configuration配置用于传递请求头
@FeignClient(name = "merchant-center-server",contextId = "mchtBaseInfoApi",fallbackFactory = MchtBaseInfoApiClientFallbackFactory.class,configuration = FeignConfiguration.class)
public interface MchtBaseInfoApiClient extends MchtBaseInfoApi {

}
```
feign降级 泛型要指定为要降级处理的feign客户端，继承FeignFallbackFactory 获得 errMsg生成的能力
```
@Component
public class MchtBaseInfoApiClientFallbackFactory extends FeignFallbackFactory implements FallbackFactory<MchtBaseInfoApiClient> {

    @Override
    public MchtBaseInfoApiClient create(Throwable throwable) {
        return new MchtBaseInfoApiClient() {
            @Override
            public ServerResponse mchtBaseInfoCreate(MchtBaseInfoCreateParam param) throws Exception {
                return ServerResponse.createByError("商户中心异常,请稍后再试:" + errMsg(throwable));
            }

            @Override
            public ServerResponse<List<MchtBaseInfoVo>> getMchtBaseInfoByUserId(Long userId) throws Exception {
                return ServerResponse.createByError("商户中心异常,请稍后再试:" + errMsg(throwable));
            }
        };
    }

}
```
