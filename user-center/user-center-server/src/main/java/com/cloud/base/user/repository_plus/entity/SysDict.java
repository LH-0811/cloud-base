package com.cloud.base.user.repository_plus.entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import io.swagger.annotations.ApiModelProperty;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 系统表-字典表(SysDict)实体类
 *
 * @author lh0811
 * @since 2021-11-03 22:28:17
 */
@Setter
@Getter
@TableName("sys_dict")
public class SysDict implements Serializable {

    /**
     * 字典id
     */
    @TableId(type = IdType.NONE)
    @ApiModelProperty(value="字典id")
    private Long id;

    /**
     * 字典类型
     */
    @ApiModelProperty(value="字典类型")
    private String type;
    /**
     * 字典键
     */
    @ApiModelProperty(value="字典键")
    private String dictKey;
    /**
     * 字典名
     */
    @ApiModelProperty(value="字典名")
    private String dictName;
    /**
     * 字典值
     */
    @ApiModelProperty(value="字典值")
    private String dictValue;


}