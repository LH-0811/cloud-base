package com.cloud.base.core.common.exception;

import com.cloud.base.core.common.entity.ServerResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;


@Slf4j
@ControllerAdvice
@AllArgsConstructor
@NoArgsConstructor
public class CommonExceptionAdvice {


    // 标志 是否使用sentinel进行限流 如果使用了 要用Trancer来统计错误数量
    private Boolean userSentinel;

    @ResponseBody
    @ExceptionHandler(value = {CommonException.class})
    public ServerResponse myError(CommonException e) {
        // 如果有err 则打印堆栈
//        if (e.getError() != null) {
//            log.error(CommonMethod.getTrace(e.getError()));
//        }
//        if (userSentinel){
//            Tracer.trace(e);
//        }
//        log.error(JSONObject.toJSONString(e.getServerResponse()));
        return e.getServerResponse();
    }


    @ResponseBody
    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public ServerResponse globalError(HttpServletRequest request, HttpMessageNotReadableException e) {
        HashMap<String,String> map = new HashMap<>();
        map.put("url",request.getRequestURI());
        return ServerResponse.createByError(400,"未上传请求体",map);
    }
////        if (userSentinel){
////            Tracer.trace(e);
////        }
//        log.error(CommonMethod.getTrace(e));
//        if (e.getClass().getSimpleName().equals("HttpMessageNotReadableException")) {
//            if (e.getLocalizedMessage().startsWith("Required request body is missing")) {
//
//                return ServerResponse.createByError(ResponseCode.ERROR.getCode(),"未上传请求body体");
//            }
//        }
//        if (e.getClass().getSimpleName().equals("MethodArgumentTypeMismatchException")) {
//            String startErrMsg = "Failed to convert value of type 'java.lang.String' to required type 'java.lang.Long'; nested exception is java.lang.NumberFormatException: For input string: ";
//            if (e.getLocalizedMessage().startsWith(startErrMsg)) {
//                String inputStr = e.getLocalizedMessage().replace(startErrMsg, "");
//                return ServerResponse.createByError(ResponseCode.ERROR.getCode(),inputStr + "不能转换成数字");
//            }
//        }
//        if (e.getClass().getSimpleName().equals("MaxUploadSizeExceededException")) {
//            return ServerResponse.createByError(ResponseCode.ERROR.getCode(),"上传文件最大不能超过10M");
//        }
//        if (e.getClass().getSimpleName().equals("FileSizeLimitExceededException")) {
//            return ServerResponse.createByError(ResponseCode.ERROR.getCode(),"上传文件最大不能超过10M");
//        }
//        return ServerResponse.createByError(ResponseCode.ERROR.getCode(),"抱歉!未知错误");
//    }



}
