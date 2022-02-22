package com.cloud.base.common.core.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONPOJOBuilder;
import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * 供应商客户entity
 *
 * @author jiangzhengjun
 * @date 2021/12/07 19:18
 **/
@Data
public class ReverseFactoringCustomerInfoDto {

    /**
     * 核心企业组织机构代码(接口文档没要求，先在swagger文档中隐藏)
     */
    private String svCustIdno;

    /**
     * 授信主体组织机构代码证(接口文档没要求，先在swagger文档中隐藏)
     */
    private String jointCustIdno;

    /**
     * 第三方流水号(接口文档要求的是seqNo，先按这个来接收)
     */
    private String seqNo;

    /**
     * 本次记录数
     */
    private String pageSize;

    /**
     * 备用字段1
     */
    private String field1;

    /**
     * 备用字段2
     */
    private String field2;

    /**
     * 地区码(接口文档没要求，先在swagger文档中隐藏)
     */
    private String areCde;


    /**
     * "供应商客户列表"
     */
    private List<Cust> custList;




    @Data
    public static class Cust {
        /**
         * 客户组织机构代码
         */
        private String custIdno;

        /**
         * 客户平台编号
         */
        private String custErpid;

        /**
         * 客户名称
         */
        private String custName;

        /**
         * 营业执照编号
         */
        private String busiLicenseNo;

        /**
         * 税务登记证号
         */
        private String taxRegisterNo;

        /**
         * 法人姓名
         */
        private String lawPersonName;

        /**
         * 法人证件类型
         */
        private String lawPersonCertType;

        /**
         * 法人证件号
         */
        private String lawPersonCertNo;

        /**
         * 经办人姓名
         */
        private String personName;

        /**
         * 经办人身份证号
         */
        private String personNo;

        /**
         * 经办人手机号
         */
        private String personPhone;

        /**
         * 供应商结算账户
         */
        private String balanceAccount;

        /**
         * 申请提交时间
         */
        private String appDate;

        /**
         * 授信主体组织机构代码证
         */
        private String jointCustIdno;

        /**
         * 授信主体企业客户名称
         */
        private String jointCustName;

        /**
         * 供应商持有凭证的总金额
         */
        private String limitTotalAmt;

        /**
         * 供应商持有该授信主体开立凭证的总额度金额
         */
        private String limitSvAmt;

        /**
         * 备用字段1
         */
        private String field1;

        /**
         * 备用字段2
         */
        private String field2;

    }
}
