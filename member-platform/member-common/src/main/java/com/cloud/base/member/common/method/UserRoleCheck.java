package com.cloud.base.member.common.method;

import com.cloud.base.core.modules.lh_security.core.entity.SecurityAuthority;
import com.cloud.base.core.modules.lh_security.core.entity.SecurityRole;
import com.cloud.base.member.common.constant.RoleConstant;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * @author lh0811
 * @date 2021/5/28
 */
public class UserRoleCheck {

    /**
     * 判断是否是系统管理员
     */
    public static Boolean isSysAdmin(SecurityAuthority securityAuthority) throws Exception {
        List<SecurityRole> securityRoleList = securityAuthority.getSecurityRoleList();
        if (CollectionUtils.isEmpty(securityRoleList)) return false;
        for (SecurityRole securityRole : securityRoleList) {
            if (securityRole.getRoleId().equals(RoleConstant.SYS_ADMIN.getRoleId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是系统管理员
     */
    public static Boolean isMchtAdmin(SecurityAuthority securityAuthority) throws Exception {
        List<SecurityRole> securityRoleList = securityAuthority.getSecurityRoleList();
        if (CollectionUtils.isEmpty(securityRoleList)) return false;
        for (SecurityRole securityRole : securityRoleList) {
            if (securityRole.getRoleId().equals(RoleConstant.MCHT_ADMIN.getRoleId())) {
                return true;
            }
        }
        return false;
    }

}
