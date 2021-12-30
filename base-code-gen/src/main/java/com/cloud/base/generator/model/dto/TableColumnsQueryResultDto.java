package com.cloud.base.generator.model.dto;

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
public class TableColumnsQueryResultDto {

    @ApiModelProperty(value = "列名")
    private String columnName;

    @ApiModelProperty(value = "数据类型")
    private String dataType;

    @ApiModelProperty(value = "列数据类型")
    private String columnType;

    @ApiModelProperty(value = "注释")
    private String columnComment;

    @ApiModelProperty(value = "列的key PRI-主键")
    private String columnKey;

    private String extra;

}
