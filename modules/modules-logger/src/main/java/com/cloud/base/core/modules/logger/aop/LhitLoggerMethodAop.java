package com.cloud.base.core.modules.logger.aop;

import com.alibaba.fastjson.JSONObject;
import com.cloud.base.core.common.util.ip.AddressUtils;
import com.cloud.base.core.common.util.ip.IpUtils;
import com.cloud.base.core.modules.logger.adapter.LhitLoggerRoleInfoByUserAdapter;
import com.cloud.base.core.modules.logger.adapter.LhitLoggerStorageAdapter;
import com.cloud.base.core.modules.logger.adapter.LhitLoggerUserInfoFromRequestAdapter;
import com.cloud.base.core.modules.logger.annotation.LhitLogger;
import com.cloud.base.core.modules.logger.entity.*;
import com.cloud.base.core.modules.logger.properties.LhitLoggerProperties;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
public class LhitLoggerMethodAop implements MethodInterceptor {


    @Autowired
    private LhitLoggerUserInfoFromRequestAdapter lhitLoggerUserInfoFromRequestAdapter;

    @Autowired
    private LhitLoggerRoleInfoByUserAdapter lhitLoggerRoleInfoByUserAdapter;

    @Autowired
    private LhitLoggerStorageAdapter lhitLoggerStorageAdapter;

    @Autowired
    private LhitLoggerProperties lhitLoggerProperties;

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        log.debug("进入方法拦截器");

        LhitLogger annotation = methodInvocation.getMethod().getAnnotation(LhitLogger.class);

        // 如果注解方法不在 web项目的 controller 类上 则注解不起作用
        if (!checkClassIsController(methodInvocation)) {
            Object proceed = methodInvocation.proceed();
            return proceed;
        }

        log.debug(" 初始化 日志实体类 ");
        LhitLoggerEntity lhitLoggerEntity = initLoggerEntityAndExeMehtod(annotation, methodInvocation);
        log.debug("退出方法拦截器");

        // 调用存储器存储日志信息
        lhitLoggerStorageAdapter.storageLogger(lhitLoggerEntity);

        if (lhitLoggerEntity.getThrowable() != null) {
            throw lhitLoggerEntity.getThrowable();
        }
        return lhitLoggerEntity.getResponseObj();
    }

    // 生成整个的 日志对象
    private LhitLoggerEntity initLoggerEntityAndExeMehtod(LhitLogger annotation, MethodInvocation methodInvocation) throws Throwable {
        LhitLoggerEntity lhitLoggerEntity = new LhitLoggerEntity();

        // 保存日志标题和操作信息
        setLogTitleAndMsg(lhitLoggerEntity, annotation);

        // 保存请求参数数据
        setLogRequestParams(lhitLoggerEntity, annotation, methodInvocation);

        // 保存操作用户的信息
        setOperUserInfo(lhitLoggerEntity, annotation);

        // 保存操作用户的角色信息
        setOperUserRoleInfo(lhitLoggerEntity, annotation);

        // 保存本次操作的ip地址信息
        setIpAndAddreInfo(lhitLoggerEntity, annotation);
        // 执行原方法操作
        try {
            Object proceed = methodInvocation.proceed();
            // 保存方法返回值
            setMethodResponse(lhitLoggerEntity, annotation, proceed);
        } catch (Throwable e) {
            lhitLoggerEntity.setThrowable(e);
        }
        return lhitLoggerEntity;
    }

    // 保存方法返回值
    private void setMethodResponse(LhitLoggerEntity lhitLoggerEntity, LhitLogger annotation, Object proceed) {
        boolean isSave = lhitLoggerProperties.getSaveReturnData() && annotation.isSaveReturnData();
        if (isSave) {
            lhitLoggerEntity.setResponseObj(proceed);
            log.debug("返回值:{}", JSONObject.toJSONString(proceed));
        }
    }

    // 保存操作主机的ip和地址
    private void setIpAndAddreInfo(LhitLoggerEntity lhitLoggerEntity, LhitLogger annotation) {
        boolean isSave = lhitLoggerProperties.getSaveOperNetAddr() && annotation.isSaveOperNetAddr();
        if (isSave) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String realAddressByIP = AddressUtils.getRealAddressByIP(IpUtils.getIpAddr(request));
            String ipAddr = IpUtils.getIpAddr(request);
            lhitLoggerEntity.setAddressEntity(AddressEntity.builder().ip(ipAddr).address(realAddressByIP).build());
            log.debug("address ip :{}-{}", ipAddr, realAddressByIP);
        }
    }

    // 保存操作用户的角色信息
    private void setOperUserRoleInfo(LhitLoggerEntity lhitLoggerEntity, LhitLogger annotation) {
        boolean isSaveUser = lhitLoggerProperties.getSaveOperUserInfo() && annotation.isSaveOperUserInfo();
        boolean isSaveRole = false;
        if (isSaveUser){
            isSaveRole = lhitLoggerProperties.getSaveOperRoleInfo() && annotation.isSaveOperRoleInfo();
        }

        if (isSaveRole) {
            Object roleInfo = lhitLoggerRoleInfoByUserAdapter.getRoleInfoByUserInfo(lhitLoggerEntity.getUserInfo());
            lhitLoggerEntity.setRoleInfo(roleInfo);
            log.debug("role info :" + roleInfo);
        }
    }

    // 保存操作用户信息
    private void setOperUserInfo(LhitLoggerEntity lhitLoggerEntity, LhitLogger annotation) throws Exception {
        boolean isSave = lhitLoggerProperties.getSaveOperUserInfo() && annotation.isSaveOperUserInfo();
        if (isSave) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            Object userInfo = lhitLoggerUserInfoFromRequestAdapter.getUserInfoFromRequest(request);
            lhitLoggerEntity.setUserInfo(userInfo);
            log.debug("user info:" + JSONObject.toJSONString(userInfo));
        }
    }

    // 保存本次请求参数
    private void setLogRequestParams(LhitLoggerEntity lhitLoggerEntity, LhitLogger annotation, MethodInvocation methodInvocation) {

        boolean isSave = lhitLoggerProperties.getSaveRequestData() && annotation.isSaveRequestData();

        if (isSave) {
            List<RequestParam> paramList = new ArrayList<>();
            Object[] arguments = methodInvocation.getArguments();
            Parameter[] parameters = methodInvocation.getMethod().getParameters();
            for (int i = 0; i < parameters.length; i++) {
                String key = parameters[i].getName();
                Object value = arguments[i];
                paramList.add(RequestParam.builder().key(key).value(value).build());
            }
            lhitLoggerEntity.setParamList(paramList);
            log.debug("request param:{}", JSONObject.toJSONString(paramList));
        }
    }

    // 保存日志的title和msg
    private void setLogTitleAndMsg(LhitLoggerEntity lhitLoggerEntity, LhitLogger annotation) {
        // 获取到注解的标题和注解业务信息
        lhitLoggerEntity.setTitle(annotation.title());
        LoggerBusinessType type = annotation.businessType();
        lhitLoggerEntity.setMsg(type.getMsg());
    }


    // 检查当前的方法所在类是不是web controller
    private Boolean checkClassIsController(MethodInvocation methodInvocation) throws Throwable {
        boolean isOk = methodInvocation.getMethod().getDeclaringClass().isAnnotationPresent(Controller.class);
        boolean isOk2 = methodInvocation.getMethod().getDeclaringClass().isAnnotationPresent(RestController.class);
        return isOk || isOk2;
    }


}
