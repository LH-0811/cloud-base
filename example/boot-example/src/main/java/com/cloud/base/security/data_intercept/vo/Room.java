package com.cloud.base.security.data_intercept.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author lh0811
 * @date 2021/4/15
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    @ApiModelProperty(value = "房间号")
    private String num;

    @ApiModelProperty(value = "学生")
    private List<Student> students;
}
