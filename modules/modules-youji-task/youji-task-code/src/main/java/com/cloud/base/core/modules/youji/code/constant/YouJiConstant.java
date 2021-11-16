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
        SINGLE_NODE("1","单节点执行"),
        ALL_NODE("2","全节点执行"),
        ;
//        1-单节点执行 2-全节点执行
        private String code;
        private String msg;


    }

}
