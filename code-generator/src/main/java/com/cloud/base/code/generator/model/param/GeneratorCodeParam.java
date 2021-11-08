package com.cloud.base.code.generator.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author lh0811
 * @date 2021/11/8
 */
@Valid
@Getter
@Setter
public class GeneratorCodeParam {
    @NotBlank(message = "未上传生成类的包路径")
    @ApiModelProperty(value = "生成类的包路径",required = true)
    private String packagePath;

    @NotEmpty(message = "未上传目标表列表")
    @ApiModelProperty(value = "目标表列表")
    private List<String> tableList;
}
