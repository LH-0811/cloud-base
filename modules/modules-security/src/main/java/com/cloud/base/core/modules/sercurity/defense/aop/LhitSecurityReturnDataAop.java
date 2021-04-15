package com.cloud.base.core.modules.sercurity.defense.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.cloud.base.core.common.entity.CommonMethod;
import com.cloud.base.core.common.entity.ResponseCode;
import com.cloud.base.core.common.entity.ServerResponse;
import com.cloud.base.core.common.exception.CommonException;
import com.cloud.base.core.modules.sercurity.defense.adapter.LhitSecurityTokenManagerAdapter;
import com.cloud.base.core.modules.sercurity.defense.annotation.LhitDataIntercept;
import com.cloud.base.core.modules.sercurity.defense.pojo.entity.LhitSecurityDataRule;
import com.cloud.base.core.modules.sercurity.defense.pojo.entity.LhitSecurityPermission;
import com.cloud.base.core.modules.sercurity.defense.pojo.entity.LhitSecurityUserPerms;
import com.cloud.base.core.modules.sercurity.defense.properties.LhitSecurityProperties;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class LhitSecurityReturnDataAop {

    @Autowired
    private LhitSecurityTokenManagerAdapter tokenMgr;


    @Autowired
    private LhitSecurityProperties lhitSecurityProperties;


    @Pointcut("@annotation(com.cloud.base.core.modules.sercurity.defense.annotation.LhitDataIntercept)")
    public void annotationPoinCut() {
    }


    @Around("annotationPoinCut()")
    public Object distributedDoubleBarrierPoinCut(ProceedingJoinPoint pdj) throws Throwable {
        // 如果不是一个Controller类 就直接执行原方法 不做任何处理
        if (!checkIsControllerClass(pdj)) return pdj.proceed();

        // 获取到本次请求的路径
        String requestPath = getRequestPath(pdj);

        // 获取到用户权限
        List<LhitSecurityPermission> permissionList = getSecurityPermissionByToken();

        // 获取权限信息中的数据限制
        LhitSecurityDataRule dataRule = checkUrl(requestPath, permissionList);

        // 如果没有限制 不做任何处理
        if (dataRule == null) return pdj.proceed();

        // 如果有限制则 按照限制规则 处理返回值
        Object proceed = pdj.proceed();
        List<String> excludeFields = dataRule.getExcludeFields();
        List<String> includeFields = dataRule.getIncludeFields();

        // 如果不是ServerResponse的标准输出则不作处理
        boolean isServerResp = proceed instanceof ServerResponse;
        if (!isServerResp) return proceed;

        ServerResponse serverResponse = (ServerResponse) proceed;

        // 如果是数组
        if (serverResponse.getData() instanceof List) {
            // 数组为空 不处理 直接返回
            if (CollectionUtils.isEmpty((List) serverResponse.getData())) return serverResponse;
            JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(serverResponse.getData()));
            String jsonStr = jsonArray.toJSONString();
            for (String excludeField : excludeFields) {
                jsonStr = removeJsonObjKey(jsonStr, excludeField);
            }
            serverResponse.setData(JSONArray.parseArray(jsonStr, ((List) serverResponse.getData()).get(0).getClass()));
        } else {
            JSONObject jsonObj = JSON.parseObject(JSON.toJSONString(serverResponse.getData()));
            String jsonStr = JSON.toJSONString(jsonObj);
            for (String excludeField : excludeFields) {
                jsonStr = removeJsonObjKey(jsonStr, excludeField);
            }
            serverResponse.setData(JSONObject.parseObject(jsonStr, serverResponse.getData().getClass()));
        }
        return serverResponse;

    }

    private LhitSecurityDataRule checkUrl(String requestPath, List<LhitSecurityPermission> permissionList) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for (LhitSecurityPermission lhitSecurityPermission : permissionList) {
            for (LhitSecurityDataRule dataRule : lhitSecurityPermission.getDateRuleList()) {
                if (antPathMatcher.match(dataRule.getApiPath(), requestPath)) return dataRule;
            }
        }
        return null;
    }


    private List<LhitSecurityPermission> getSecurityPermissionByToken() throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(lhitSecurityProperties.getTokenkey());
        LhitSecurityUserPerms permsByToken = tokenMgr.getPermsByToken(token);
        if (permsByToken == null) {
            throw CommonException.create(ServerResponse.createByError(ResponseCode.NO_AUTH.getCode(), "该方法需要访问权限，请登陆后尝试"));
        }
        return permsByToken.getPermissions();
    }

    private boolean checkIsControllerClass(ProceedingJoinPoint pdj) throws Exception {
        // 判断方法所在类是不是一个Controller类 如果不是则跳过该次切面操作
        RestController restController = pdj.getTarget().getClass().getAnnotation(RestController.class);
        Controller controller = pdj.getTarget().getClass().getAnnotation(Controller.class);
        return (restController != null || controller != null);
    }

    private String getRequestPath(ProceedingJoinPoint pdj) throws Exception {
        StringBuilder sb = new StringBuilder();
        // 获取到注解传入的锁路径
        MethodSignature signature = (MethodSignature) pdj.getSignature();
        // 获取方法所在类的注解
        RequestMapping classReqMapping = pdj.getTarget().getClass().getAnnotation(RequestMapping.class);
        if (classReqMapping != null) sb.append(classReqMapping.value().length > 0 ? classReqMapping.value()[0] : "");
        RequestMapping requestMapping = signature.getMethod().getAnnotation(RequestMapping.class);
        if (requestMapping != null) sb.append(requestMapping.value().length > 0 ? requestMapping.value()[0] : "");
        GetMapping getMapping = signature.getMethod().getAnnotation(GetMapping.class);
        if (getMapping != null) sb.append(getMapping.value().length > 0 ? getMapping.value()[0] : "");
        PostMapping postMapping = signature.getMethod().getAnnotation(PostMapping.class);
        if (postMapping != null) sb.append(postMapping.value().length > 0 ? postMapping.value()[0] : "");
        PutMapping putMapping = signature.getMethod().getAnnotation(PutMapping.class);
        if (putMapping != null) sb.append(putMapping.value().length > 0 ? putMapping.value()[0] : "");
        DeleteMapping deleteMapping = signature.getMethod().getAnnotation(DeleteMapping.class);
        if (deleteMapping != null) sb.append(deleteMapping.value().length > 0 ? deleteMapping.value()[0] : "");
        return sb.toString();
    }

    public String removeJsonObjKey(String jsonStr, String key) {
        for (int index = -1; ; ) {
            index = jsonStr.indexOf("\"" + key);
            if (index == -1) break;
            ArrayList<String> endStrLis = Lists.newArrayList(",", "}", "]");
            List<Integer> endIndexList = Lists.newArrayList();
            for (String endStr : endStrLis) {
                int i = jsonStr.indexOf(endStr, index);
                if (i != -1) {
                    endIndexList.add(i);
                }
            }
            int endIndex = endIndexList.stream().min(Integer::compareTo).get();
            String substring = jsonStr.substring(index, endIndex + 1);
            jsonStr = jsonStr.replace(substring, "");
        }
        return jsonStr;
    }
}
