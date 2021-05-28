package com.cloud.base.memeber.merchant.param;

import com.cloud.base.core.common.entity.CommonEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 商户中心-商户信息表(MchtBaseInfo)实体类
 *
 * @author lh0811
 * @since 2021-05-26 22:05:58
 */
@Setter
@Getter
@ApiModel(description = "商户基本信息查询参数")
public class MchtBaseInfoQueryParam implements Serializable {

    /**
     * 商户用户id
     */
    @ApiModelProperty(value="商户用户id")
    private Long mchtUserId;

    /**
     * 商户名称
     */
    @ApiModelProperty(value="商户名称")
    private String mchtName;

    /**
     * 是可用
     */
    @ApiModelProperty(value="是可用")
    private Boolean enableFlag;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间（起）")
    private Date createTimeLow;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间（止）")
    private Date createTimeUp;

    /**
     * 页码
     */
    @ApiModelProperty(value="页码")
    private Integer pageNum = CommonEntity.pageNum;

    /**
     * 每页条数
     */
    @ApiModelProperty(value="每页条数")
    private Integer pageSize = CommonEntity.pageSize;
}
