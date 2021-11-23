package com.cloud.base.core.modules.youji.code.exception;

import com.cloud.base.core.modules.youji.code.constant.YouJiConstant;
import com.cloud.base.core.modules.youji.code.repository.entity.TaskInfo;
import com.cloud.base.core.modules.youji.code.repository.entity.TaskWorker;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author lh0811
 * @date 2021/11/23
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class YouJiException extends Exception {
    private String code;
    private String errMsg;
    private TaskInfo taskInfo;
    private TaskWorker taskWorker;


    public YouJiException(String code,String errMsg){
        this.code = code;
        this.errMsg = errMsg;
    }

    public YouJiException(YouJiConstant.YouJiErrorEnum errorEnum){
        this.code = errorEnum.getCode();
        this.errMsg = errorEnum.getMsg();
    }
    public YouJiException(YouJiConstant.YouJiErrorEnum errorEnum,TaskInfo taskInfo){
        this.code = errorEnum.getCode();
        this.errMsg = errorEnum.getMsg();
        this.taskInfo = taskInfo;
    }

    public YouJiException(YouJiConstant.YouJiErrorEnum errorEnum,TaskInfo taskInfo,TaskWorker taskWorker){
        this.code = errorEnum.getCode();
        this.errMsg = errorEnum.getMsg();
        this.taskInfo = taskInfo;
        this.taskWorker = taskWorker;
    }
}
