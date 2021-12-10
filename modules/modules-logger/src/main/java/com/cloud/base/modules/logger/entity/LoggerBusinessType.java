package com.cloud.base.modules.logger.entity;

import lombok.Getter;

@Getter
public enum LoggerBusinessType {
    NONE("none", "无业务类型"),
    INSERT("insert", "增加数据"),
    DELETE("delete", "删除数据"),
    UPDATE("update", "更新数据"),
    QUERY("query", "查询数据");

    String code;
    String msg;

    LoggerBusinessType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }


}
