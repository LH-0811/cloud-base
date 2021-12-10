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
public class TableQueryResultDto {

    @ApiModelProperty(value = "表名")
    private String tableName;

    @ApiModelProperty(value = "引擎")
    private String engine;

    @ApiModelProperty(value = "表注释")
    private String tableComment;

    @ApiModelProperty(value = "表创建时间")
    private String createTime;

}
