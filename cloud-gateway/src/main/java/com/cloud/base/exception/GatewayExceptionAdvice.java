package com.cloud.base.exception;

import org.springframework.context.annotation.Configuration;

/**
 * 异常处理类
 *
 * @auth lh0811
 * @date 2020/11/15
 */
@Configuration
public class GatewayExceptionAdvice {

    /**
     * 自定义异常处理[@@]注册Bean时依赖的Bean，会从容器中直接获取，所以直接注入即可
     */
//    @Primary
//    @Bean
//    @Order(Ordered.HIGHEST_PRECEDENCE)
//    public ErrorWebExceptionHandler errorWebExceptionHandler(ObjectProvider<List<ViewResolver>> viewResolversProvider,
//                                                             ServerCodecConfigurer serverCodecConfigurer) {
//        JsonExceptionHandler jsonExceptionHandler = new JsonExceptionHandler();
//        jsonExceptionHandler.setViewResolvers(viewResolversProvider.getIfAvailable(Collections::emptyList));
//        jsonExceptionHandler.setMessageWriters(serverCodecConfigurer.getWriters());
//        jsonExceptionHandler.setMessageReaders(serverCodecConfigurer.getReaders());
//        return jsonExceptionHandler;
//    }

}
