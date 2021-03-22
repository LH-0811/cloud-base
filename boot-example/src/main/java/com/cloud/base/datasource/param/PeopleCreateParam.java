package com.cloud.base.datasource.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author lh0811
 * @date 2020/12/9
 */
@Getter
@Setter
public class PeopleCreateParam extends TenantParam{

    /**
     * 姓名
     */
    @NotNull(message = "姓名不能为空")
    @ApiModelProperty(value = "姓名",required = true)
    private String name;

    /**
     * 年龄
     */
    @ApiModelProperty(value = "年龄")
    private Integer age;

}
