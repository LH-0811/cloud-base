package com.cloud.base.common.youji.cronjob.core.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 常量类
 *
 * @author lh0811
 * @date 2021/11/16
 */
public class Youji2Constant {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum ExecType {
        SINGLE_NODE("single", "单节点执行"),
        ALL_NODE("all", "全节点执行"),
        ;
        private String code;
        private String msg;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum TaskType {
        URL("url", "通过url调起服务"),
        BEAN("bean", "通过类反射调起服务"),
        ;
        private String code;
        private String msg;
    }



    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum YoujiTaskConfig {
        SCAN_MGR_TASK_LOCK("scan_mgr_task_lock", "是否有节点正在进行任务扫描"),
        ;
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
        WORKER_TASK_RESP_ERR("1005", "工作节点返回响应格式不正确"),
        ;
        private String code;
        private String msg;

    }

}
