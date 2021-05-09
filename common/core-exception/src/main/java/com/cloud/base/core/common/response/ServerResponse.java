package com.cloud.base.core.common.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


public class ServerResponse<T> implements Serializable {

    @ApiModelProperty(value = "请求结果状态0成功 其他是失败")
    private int status;

    @ApiModelProperty(value = "附加消息")
    private String msg;

    @Getter
    @Setter
    @ApiModelProperty(value = "数据")
    private T data;

    public ServerResponse(){
        super();
    }

    public ServerResponse(int status){
        this.status = status;
    }
    public ServerResponse(int status,T data){
        this.status = status;
        this.data = data;
    }
    public ServerResponse(int status,String msg,T data){
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
    public ServerResponse(int status,String msg){
        this.status = status;
        this.msg = msg;
    }

    public boolean isSuccess(){
        return this.status == ResponseCode.SUCCESS.getCode();
    }
    public Integer getStatus(){
        return status;
    }
    public String getMsg(){
        return msg;
    }

    public static <T> ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccess(T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),data);
    }

    public static <T> ServerResponse<T> createBySuccess(String msg){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }

    public static <T> ServerResponse<T> createBySuccess(String msg, T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }


    public static <T> ServerResponse<T> createByError(){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
    }

    public static <T> ServerResponse<T> createByError(T data){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),data);
    }

    public static <T> ServerResponse<T> createByError(String errorMessage){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),errorMessage);
    }

    public static <T> ServerResponse<T> createByError(Integer errorCode, String errorMessage){
        return new ServerResponse<T>(errorCode,errorMessage);
    }

    public static <T> ServerResponse<T> createByError(Integer code, String errorMessage,T data){
        return new ServerResponse<T>(code,errorMessage,data);
    }

    public static <T> ServerResponse<T> createByError(String errorMessage,T data){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),errorMessage,data);
    }


}
