package com.cloud.base.datasource.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author lh0811
 * @date 2020/12/9
 */
@Setter
@Getter
@Table(name = "t_people")
public class People {
    /**
     * 属性id
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "属性id")
    private Long id;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String name;

    /**
     * 年龄
     */
    @ApiModelProperty(value = "年龄")
    private Integer age;

}
