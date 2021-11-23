package com.cloud.base.core.modules.youji.code.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 常量类
 *
 * @author lh0811
 * @date 2021/11/16
 */
public class YouJiConstant {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum ExecType {
        SINGLE_NODE("1", "单节点执行"),
        ALL_NODE("2", "全节点执行"),
        ;
        //        1-单节点执行 2-全节点执行
        private String code;
        private String msg;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum YouJiErrorEnum {

        NOT_EXIST_TASK("1001", "任务不存在"),
        NOT_FIND_WORKER("1002", "未获取到工作节点"),
        FAIL_SEND_TASK_TO_WORKER("1003", "向工作节点发送任务失败"),
        TASK_EXEC_TYPE_ERR("1004", "任务执行方式不合法"),
        ;
        private String code;
        private String msg;

    }

}
