package com.cloud.base.security.data_intercept.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author lh0811
 * @date 2021/4/15
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "年龄")
    private String age;
    @ApiModelProperty(value = "得分")
    private String score;
}
