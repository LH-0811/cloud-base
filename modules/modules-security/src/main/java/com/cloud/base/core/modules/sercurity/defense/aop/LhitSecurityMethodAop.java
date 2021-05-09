package com.cloud.base.core.modules.sercurity.defense.aop;

import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.common.response.ResponseCode;
import com.cloud.base.core.common.response.ServerResponse;
import com.cloud.base.core.modules.sercurity.defense.adapter.LhitSecurityTokenManagerAdapter;
import com.cloud.base.core.modules.sercurity.defense.annotation.HasPermsCode;
import com.cloud.base.core.modules.sercurity.defense.pojo.entity.LhitSecurityPermission;
import com.cloud.base.core.modules.sercurity.defense.pojo.entity.LhitSecurityUserPerms;
import com.cloud.base.core.modules.sercurity.defense.properties.LhitSecurityProperties;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Aspect
@Component
public class LhitSecurityMethodAop {

    @Autowired
    private LhitSecurityTokenManagerAdapter tokenMgr;


    @Autowired
    private LhitSecurityProperties lhitSecurityProperties;


    @Pointcut("@annotation(com.cloud.base.core.modules.sercurity.defense.annotation.HasPermsCode)")
    public void annotationPoinCut() {
    }

    @Before("annotationPoinCut()")
    public void before(JoinPoint joinPoint) throws Exception {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        HasPermsCode annotation = signature.getMethod().getAnnotation(HasPermsCode.class);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(lhitSecurityProperties.getTokenkey());
        String[] needPermsCode = annotation.value();
        if (StringUtils.isEmpty(token)) {
            throw CommonException.create(ServerResponse.createByError(ResponseCode.NO_AUTH.getCode(),"未上传用户token，无法访问受保护的资源"));
        }
        Boolean hasPerms = false;
        LhitSecurityUserPerms permsByToken = tokenMgr.getPermsByToken(token);
        if (permsByToken == null) {
            throw CommonException.create(ServerResponse.createByError(ResponseCode.NO_AUTH.getCode(),"该方法需要访问权限，请登陆后尝试"));
        }
        List<LhitSecurityPermission> permissions = permsByToken.getPermissions();

        // 获取到所有的权限码
        List<String> codes = permissions.stream().map(ele -> ele.getPermsCode()).collect(Collectors.toList());

        // 判断是不是all权限
        if (codes.contains("All") || codes.contains("ALL") || codes.contains("all")) {
            hasPerms = true;
        } else {
            // 判断是不是有指定权限
            for (String needToken : needPermsCode) {
                if (codes.contains(needToken)) {
                    hasPerms = true;
                }
                break;
            }
        }
        if (!hasPerms)
            throw CommonException.create(ServerResponse.createByError(ResponseCode.ERR_AUTH.getCode(),"权限不足，无法访问该资源,请联系管理员"));
    }
}
