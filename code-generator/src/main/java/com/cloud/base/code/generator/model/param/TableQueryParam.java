package com.cloud.base.code.generator.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author lh0811
 * @date 2021/11/6
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TableQueryParam {

    @ApiModelProperty(value = "表名")
    private String tableName;

    @ApiModelProperty(value = "页码")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "每页条数")
    private Integer pageSize = 10;

}
