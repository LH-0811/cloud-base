package com.cloud.base.core.modules.youji.code.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author lh0811
 * @date 2021/11/13
 */
@Getter
@Setter
@Valid
public class YouJiWorkerReceiveTaskParam {

    /**
     * 任务编号
     */
    @NotBlank(message = "任务编号不能为空")
    @ApiModelProperty(value = "任务编号", required = true)
    private String taskNo;
    /**
     * 任务名称
     */
    @NotBlank(message = "任务名称不能为空")
    @ApiModelProperty(value = "任务名称", required = true)
    private String taskName;
    /**
     * 任务执行表达式
     */
    @NotBlank(message = "任务执行表达式不能为空")
    @ApiModelProperty(value = "任务执行表达式", required = true)
    private String corn;
    /**
     * 任务执行触发的url地址
     */
    @ApiModelProperty(value = "任务执行触发的url地址")
    private String taskUrl;

    /**
     * 应用上下文执行中对应的全限定类名
     */
    @ApiModelProperty(value = "应用上下文执行中对应的全限定类名")
    private String taskBeanName;

    /**
     * 应用上下文中对应的方法名，或者url的请求类型
     */
    @ApiModelProperty(value = "应用上下文中对应的方法名，或者url的请求类型")
    private String taskMethod;

    /**
     * 执行参数(JSON格式 多个入参用|分隔,保证入参的顺序与方法一致)
     */
    @ApiModelProperty(value = "执行参数(JSON格式 多个入参用|分隔,保证入参的顺序与方法一致)")
    private String taskParam;

    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String contactsName;

    /**
     * 联系人电话
     */
    @ApiModelProperty(value = "联系人电话")
    private String contactsPhone;

    /**
     * 联系人邮箱
     */
    @ApiModelProperty(value = "联系人邮箱")
    private String contactsEmail;


}
