package com.cloud.base.example.cloud.account.repository.entity;

import java.math.BigDecimal;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import io.swagger.annotations.ApiModelProperty;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.*;
/**
 * (TAccount)实体类
 *
 * @author lh0811
 * @since 2021-03-25 17:32:00
 */
@Setter
@Getter
@Table(name="t_account")
public class TAccount implements Serializable {

    @Id
    @KeySql(useGeneratedKeys = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="主键")
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value="用户id")
    private Long userId;
    /**
     * 总额度
     */
    @ApiModelProperty(value="总额度")
    private BigDecimal total;
    /**
     * 已用额度
     */
    @ApiModelProperty(value="已用额度")
    private BigDecimal used;
    /**
     * 剩余可用额度
     */
    @ApiModelProperty(value="剩余可用额度")
    private BigDecimal residue;


}
