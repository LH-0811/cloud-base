package com.cloud.base.core.modules.rooster.code.annotation;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModelProperty;
import org.json.JSONObject;

import java.lang.annotation.*;
import java.util.List;

/**
 * 任务注解
 *
 * @author lh0811
 * @date 2021/11/13
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RoosterTask {

    /**
     * 任务名称
     *
     * @return
     */
    @ApiModelProperty(value = "任务名称")
    String taskName();

    /**
     * 任务编码
     *
     * @return
     */
    @ApiModelProperty(value = "任务编码")
    String taskNo();

    /**
     * 表达式 初始化阶段的默认表达式，再管理端修改后就被覆盖了
     *
     * @return
     */
    @ApiModelProperty(value = "表达式")
    String corn();

    /**
     * 是否可用 初始化阶段的默认配置，在管理端修改后就被覆盖了
     *
     * @return
     */
    @ApiModelProperty(value = "是否可用")
    boolean enable();


    /**
     * 参数列表，json字符串参数，多个入参时，保证与方法入参顺序一致
     *
     * @return
     */
    @ApiModelProperty(value = "参数列表，json字符串参数，多个入参时，保证与方法入参顺序一致")
    String[] params() default {};


    /**
     * 联系人
     * @return
     */
    @ApiModelProperty(value = "联系人")
    String contactsName() default "";

    /**
     * 联系人电话
     * @return
     */
    @ApiModelProperty(value = "联系人电话")
    String contactsPhone() default "";

    /**
     * 联系人邮箱
     * @return
     */
    @ApiModelProperty(value = "联系人邮箱")
    String contactsEmail() default "";

}
