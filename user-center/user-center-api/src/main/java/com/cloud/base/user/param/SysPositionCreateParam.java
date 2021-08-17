package com.cloud.base.user.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户中心-岗位信息(SysPosition)实体类
 *
 * @author lh0811
 * @since 2021-08-10 21:55:23
 */
@Setter
@Getter
public class SysPositionCreateParam implements Serializable {

    /**
     * 岗位编号
     */
    @NotNull(message = "未上传 岗位编号")
    @ApiModelProperty(value="岗位编号",required = true)
    private String no;

    /**
     * 岗位名称
     */
    @NotNull(message = "未上传 岗位名称")
    @ApiModelProperty(value="岗位名称",required = true)
    private String name;
    /**
     * 岗位备注
     */
    @ApiModelProperty(value="岗位备注")
    private String notes;


}
