package com.cloud.base.filter;

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

    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

//    @Autowired
//    private TokenStore tokenStore;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 获取到请求参数
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        log.info("****************进入自定义全局过滤器:request-uri-{}****************", request.getURI());

        //0 排除swagger请求
        if (antPathMatcher.match("/**/swagger-resources/**", request.getURI().getPath())) {
            return chain.filter(exchange);
        }
        if (antPathMatcher.match("/**/v2/api-docs", request.getURI().getPath())) {
            return chain.filter(exchange);
        }
        if (antPathMatcher.match("**/swagger-ui.html", request.getURI().getPath())) {
            return chain.filter(exchange);
        }
        //1 uaa服务所有放行
//        if (antPathMatcher.match("/**/auth-server/**", request.getURI().getPath())) {
//            return chain.filter(exchange);
//        }
        //2 检查token是否存在
//        String token = getToken(exchange);
//        if (StringUtils.isNotBlank(token)) {
//            // 判断是否是有效的token
//            OAuth2AccessToken oAuth2AccessToken;
//            try {
//                oAuth2AccessToken = tokenStore.readAccessToken(token);
//                Map<String, Object> additionalInformation = oAuth2AccessToken.getAdditionalInformation();
//                //取出用户身份信息
//                String principal = MapUtils.getString(additionalInformation, "user_name");
//                //获取用户权限
////                List<String> authorities = (List<String>) additionalInformation.get("authorities");
////                JSONObject jsonObject=new JSONObject();
////                jsonObject.put("principal",principal);
////                jsonObject.put("authorities",authorities);
//                //给header里面添加值
//                String base64 = EncryptUtil.encodeUTF8StringBase64(principal);
//                ServerHttpRequest tokenRequest = exchange.getRequest().mutate().header(CommonEntity.HeaderUserInfoKey, base64).build();
//                ServerWebExchange build = exchange.mutate().request(tokenRequest).build();
//                return chain.filter(build);
//            } catch (InvalidTokenException e) {
//                log.info("无效的token: {}", token);
//                return invalidTokenMono(exchange);
//            }
//        }
        return chain.filter(exchange);
    }


    @Override
    public int getOrder() {
        return 1;
    }
}
