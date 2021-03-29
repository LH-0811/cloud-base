package com.cloud.base.example.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author lh0811
 * @date 2021/3/29
 */
@Getter
@Setter
public class ExampleParam {

    @NotEmpty(message = "未上传 姓名")
    @ApiModelProperty(value = "姓名",required = true)
    private String name ;


    @NotNull(message = "未上传 得分")
    @Range(min = 0,max = 100,message = "得分不合法 0-100")
    @ApiModelProperty(value = "得分",required = true)
    private Integer score ;
}
