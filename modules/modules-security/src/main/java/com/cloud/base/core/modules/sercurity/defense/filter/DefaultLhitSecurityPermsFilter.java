package com.cloud.base.core.modules.sercurity.defense.filter;

import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ResponseCode;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.sercurity.defense.adapter.LhitSecurityResourceProtectAdapter;
import com.cloud.base.core.modules.sercurity.defense.adapter.LhitSecurityTokenManagerAdapter;
import com.cloud.base.core.modules.sercurity.defense.pojo.entity.LhitSecurityPermission;
import com.cloud.base.core.modules.sercurity.defense.pojo.entity.LhitSecurityRole;
import com.cloud.base.core.modules.sercurity.defense.pojo.entity.LhitSecurityUserPerms;
import com.cloud.base.core.modules.sercurity.defense.pojo.user.DefaultLhitSecurityUser;
import com.cloud.base.core.modules.sercurity.defense.pojo.user.LhitSecurityUser;
import com.cloud.base.core.modules.sercurity.defense.properties.LhitSecurityProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 默认的用户权限验证过滤器
 * <p>
 * 验证步骤
 * 1. 判断权限是否是配置文件中指定的不需要权限验证的 如果是 就放行
 * 2. 从lhitSecurityResourceProtectAdapter中获取到需要保护的 url资源路径
 * 3. 如果不是受保护的uri路径 就直接放行
 * 4. 从lhitSecurityTokenManagerAdapter中获取到token对应的 perms
 * 5. 判断用户是否有uri对应的权限 有就放行 否则不放行
 */
@Slf4j
public class DefaultLhitSecurityPermsFilter implements LhitSecurityPermsFilter {

    @Autowired
    private LhitSecurityProperties lhitSecurityProperties;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    // 受保护的资源
    List<String> protectUrlPatterns = new ArrayList<>();

    @Autowired
    private LhitSecurityResourceProtectAdapter lhitSecurityResourceProtectAdapter;

    @Autowired
    private LhitSecurityTokenManagerAdapter<DefaultLhitSecurityUser, LhitSecurityRole> lhitSecurityTokenManagerAdapter;


    @Getter
    @Setter
    private String urlPatterns = "/**";

    @PostConstruct
    private void init() {
        protectUrlPatterns = lhitSecurityResourceProtectAdapter.getProtectUrlPatterns();
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ///////////////////以下两个条件 一旦满足直接通过///////////////
        if (request.getMethod().equals("OPTIONS")){
            return true;
        }

        // 判断是不是忽略的请求
        boolean ignore = isIgnore(request.getRequestURI());
        if (ignore) return true;

        // 判断用户当前访问的url是否是受保护的url
        boolean isProtect = checkUrlIsProtect(lhitSecurityResourceProtectAdapter.getProtectUrlPatterns(), request.getRequestURI());
        if (!isProtect) return true;

        ///////////////////以下判断条件 一旦不符合 直接拦截///////////////////////////////////
        // 获取用户权限
        String token = getUserToken(request, response);

        // 根据token 获取到用户的权限
        checkUserToken(token);

        // 判断用户是否有访问资源的权限
        return checkUserPerms(token, request);
    }

    // 判断当前请求的uri是否是被忽略权限验证的
    private boolean isIgnore(String requestUri) throws Exception {
        // 排除swagger的相关请求
        if (requestUri.indexOf("swagger") > -1) return true;
        List<String> ignore = lhitSecurityProperties.getIgnore();
        for (String url : ignore) {
            if (antPathMatcher.match(url, requestUri))
                return true;
        }
        return false;
    }

    // 判断当前请求的uri是否是受保护的
    private boolean checkUrlIsProtect(List<String> protectUrlPatterns, String requestUri) throws Exception {
        for (String protectUrlPattern : protectUrlPatterns) {
            if (antPathMatcher.match(protectUrlPattern, requestUri)) {
                return true;
            }
        }
        return false;
    }

    // 获取用户token
    private String getUserToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 获取用户token
        String token = request.getHeader(lhitSecurityProperties.getTokenkey());
        if (StringUtils.isEmpty(token)) {
            throw CommonException.create(ServerResponse.createByError(ResponseCode.NO_AUTH.getCode(),"未上传用户token，无法访问受保护的资源"));
        }
        return token;
    }

    private void checkUserToken(String token) throws Exception {
        // 根据token 获取到用户的权限
        LhitSecurityUser currentUser = lhitSecurityTokenManagerAdapter.getUserInfoByToken(token);
        if (currentUser == null) {
            throw CommonException.create(ServerResponse.createByError(ResponseCode.NO_AUTH.getCode(),"用户token无效,请重新登录后访问"));
        }
        // 检查token是否已经失效
        if (!lhitSecurityTokenManagerAdapter.checkToken(token, currentUser.getUserId())) {
            throw CommonException.create(ServerResponse.createByError(ResponseCode.NO_AUTH.getCode(),"用户token无效,请重新登录后访问"));
        }
        // token检查通过后 续期
        lhitSecurityTokenManagerAdapter.delayExpired(token);
    }

    private boolean checkUserPerms(String token, HttpServletRequest request) throws Exception {
        // 获取用户权限
        LhitSecurityUserPerms<LhitSecurityRole, DefaultLhitSecurityUser> perms = lhitSecurityTokenManagerAdapter.getPermsByToken(token);
        for (LhitSecurityPermission permission : perms.getPermissions()) {
            if (antPathMatcher.match(permission.getUrl(), request.getRequestURI())) {
                return true;
            }
        }
        // 根据用户的权限 判断
        throw CommonException.create(ServerResponse.createByError(ResponseCode.ERR_AUTH.getCode(),"权限不足，无法访问该资源,请联系管理员"));
    }


}
