package com.cloud.base.gateway.filter;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class CustomerGlobalFilter implements GlobalFilter, Ordered {

//    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取到请求参数
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        log.info("****************进入自定义全局过滤器:request-uri-{}****************", request.getURI());
        // 从请求头中获取到token
//        if (request.getHeaders().containsKey(securityProperties.getTokenKey())){
//            String token  = request.getHeaders().get(securityProperties.getTokenKey()).get(0);
//            String path = request.getURI().getPath();
//            securityClientService.hasUrl(token,path);
//        }
        return chain.filter(exchange);
    }


    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
