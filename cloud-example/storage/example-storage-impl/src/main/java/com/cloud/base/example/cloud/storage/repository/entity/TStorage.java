package com.cloud.base.example.cloud.storage.repository.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import io.swagger.annotations.ApiModelProperty;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.*;
/**
 * (TStorage)实体类
 *
 * @author lh0811
 * @since 2021-03-25 20:20:57
 */
@Setter
@Getter
@Table(name="t_storage")
public class TStorage implements Serializable {

    @Id
    @KeySql(useGeneratedKeys = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="${column.comment}")
    private Long id;

    /**
     * 产品id
     */
    @ApiModelProperty(value="产品id")
    private Long productId;
    /**
     * 总库存
     */
    @ApiModelProperty(value="总库存")
    private Integer total;
    /**
     * 已用库存
     */
    @ApiModelProperty(value="已用库存")
    private Integer used;
    /**
     * 剩余库存
     */
    @ApiModelProperty(value="剩余库存")
    private Integer residue;

    /**
     * 剩余库存
     */
    @ApiModelProperty(value="库存单价")
    private BigDecimal price;


}
