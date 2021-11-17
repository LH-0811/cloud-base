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
public class YouJiWorkerRegisterTaskParam {


    @NotEmpty(message = "创建参数列表不能为空")
    @ApiModelProperty(value = "创建任务参数列表")
    private List<YouJiTaskForm> paramList;

    @Getter
    @Setter
    @Valid
    public static class YouJiTaskForm{
        /**
         * （1-通过URL触发 2-遍历容器中的bean触发）
         */
        @NotBlank(message = "任务触发方式不能为空")
        @ApiModelProperty(value="（1-通过URL触发 2-遍历容器中的bean触发）",required = true)
        private String taskType = "2";

        /**
         * 执行方式 (1-单节点执行 2-全节点执行)
         */
        @NotBlank(message = "任务执行方式不能为空")
        @ApiModelProperty(value="执行方式 (1-单节点执行 2-全节点执行)",required = true)
        private String execType;
        /**
         * 任务编号
         */
        @NotBlank(message = "任务编号不能为空")
        @ApiModelProperty(value="任务编号",required = true)
        private String taskNo;
        /**
         * 任务名称
         */
        @NotBlank(message = "任务名称不能为空")
        @ApiModelProperty(value="任务名称",required = true)
        private String taskName;
        /**
         * 任务执行表达式
         */
        @NotBlank(message = "任务执行表达式不能为空")
        @ApiModelProperty(value="任务执行表达式",required = true)
        private String corn;
        /**
         * 任务执行触发的url地址
         */
        @ApiModelProperty(value="任务执行触发的url地址")
        private String taskUrl;
        /**
         * 应用上下文执行中对应的全限定类名
         */
        @ApiModelProperty(value="应用上下文执行中对应的全限定类名")
        private String taskBeanName;
        /**
         * 应用上下文中对应的方法名，或者url的请求类型
         */
        @ApiModelProperty(value="应用上下文中对应的方法名，或者url的请求类型")
        private String taskMethod;

        /**
         * 执行 只支持字符串类型参数
         */
        @ApiModelProperty(value="执行参数(只支持字符串类型参数)")
        private String taskParam;

        /**
         * 联系人
         */
        @ApiModelProperty(value="联系人")
        private String contactsName;
        /**
         * 联系人电话
         */
        @ApiModelProperty(value="联系人电话")
        private String contactsPhone;
        /**
         * 联系人邮箱
         */
        @ApiModelProperty(value="联系人邮箱")
        private String contactsEmail;
        /**
         * 是否可用
         */
        @ApiModelProperty(value="是否可用 默认true",required = true)
        private Boolean enableFlag = true;
    }



}
