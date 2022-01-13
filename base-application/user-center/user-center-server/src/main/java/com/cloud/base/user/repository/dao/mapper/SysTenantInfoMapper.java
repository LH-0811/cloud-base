package com.cloud.base.user.repository.dao.mapper;

import com.cloud.base.user.repository.entity.SysTenantInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.base.user.repository.entity.SysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 系统管理-租户信息管理
 *
 * @author lh0811
 * @email lh0811
 * @date 2022-01-05 18:01:20
 */
public interface SysTenantInfoMapper extends BaseMapper<SysTenantInfo> {

    @Select("SELECT\n" +
            "\tsys_user.* \n" +
            "FROM\n" +
            "\tsys_user , sys_role ,sys_user_role_rel\n" +
            "WHERE\n" +
            "\tsys_user.tenant_no = #{tenantNo}\n" +
            "\tAND\n" +
            "\tsys_role.`no` = 'ROLE001'\n" +
            "\tAND\n" +
            "\tsys_user_role_rel.user_id = sys_user.id\n" +
            "\tAND\n" +
            "\tsys_user_role_rel.role_id = sys_role.id")
    SysUser getTenantSysMgrUser(@Param("tenantNo") String tenantNo);

}

