package com.cloud.base.member.merchant.repository.entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import io.swagger.annotations.ApiModelProperty;
import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.*;
/**
 * 商户中心-商户vip用户关联表(MchtVipUser)实体类
 *
 * @author lh0811
 * @since 2021-06-08 22:10:40
 */
@Setter
@Getter
@Table(name="mcht_vip_user")
public class MchtVipUser implements Serializable {

    /**
     * 主键
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="主键")
    private Long id;

    /**
     * 商户id
     */
    @ApiModelProperty(value="商户id")
    private Long mchtId;
    /**
     * 用户id
     */
    @ApiModelProperty(value="用户id")
    private Long userId;


}