package com.cloud.base.member.merchant.repository.entity;

import java.util.Date;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import io.swagger.annotations.ApiModelProperty;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.*;
/**
 * 商户中心-商户信息表(MchtBaseInfo)实体类
 *
 * @author lh0811
 * @since 2021-05-26 22:05:58
 */
@Setter
@Getter
@Table(name="mcht_base_info")
public class MchtBaseInfo implements Serializable {

    /**
     * 商户信息主键
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="商户信息主键")
    private Long id;

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
     * 商户描述
     */
    @ApiModelProperty(value="商户描述")
    private String mchtDesc;
    /**
     * 商户地址
     */
    @ApiModelProperty(value="商户地址")
    private String address;
    /**
     * 是可用
     */
    @ApiModelProperty(value="是可用")
    private Long enableFlag;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private Date createTime;
    /**
     * 创建人
     */
    @ApiModelProperty(value="创建人")
    private Long createBy;
    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间")
    private Date updateTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value="更新人")
    private Long updateBy;


}
